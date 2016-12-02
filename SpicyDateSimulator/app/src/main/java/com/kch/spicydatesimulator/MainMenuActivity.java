package com.kch.spicydatesimulator;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    private final ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {}

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    private Intent musicIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        final Intent toMessaging = new Intent(this, MessagingActivity.class);
        musicIntent = new Intent(this, MediaService.class);
        Button maleStartButton = (Button) findViewById(R.id.male_start_button);
        maleStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toMessaging);
            }
        });

        Button femaleStartButton = (Button) findViewById(R.id.female_start_button);
        femaleStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toMessaging);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // start service, music auto starts playing
        startService(musicIntent);
        bindService(musicIntent, mConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        // stops service, wont actually destroy it until
        // Messaging Activity also unbinds
        stopService(musicIntent);
        unbindService(mConn);
        super.onPause();
    }
}
