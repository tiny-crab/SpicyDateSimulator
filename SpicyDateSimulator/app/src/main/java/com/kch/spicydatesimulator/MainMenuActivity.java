package com.kch.spicydatesimulator;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.kch.spicydatesimulator.download.MainDownloadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainMenuActivity extends AppCompatActivity {

    private final ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {}

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    private static boolean running; // used by MainDownloadTask to make sure activity is still active
    private Intent musicIntent;
    private List<Pair<String,String>> gamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        running = true;
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
        MainDownloadTask t = new MainDownloadTask(this);
        // takes care of loading game files from online and updating the screen
        Executors.newSingleThreadExecutor().submit(t);
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

    @Override
    protected  void onPause() {
        running = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        running = true;
        super.onResume();
    }

    /**
     * To be used by MainDownloadTask, fills the dropdown with game options, leaving buttons disabled and
     * displaying a message if no games were found
     * @param games list of games to populate dropdown with. If it is empty, will display that no games were found
     */
    public void fillGameDropDown(List<Pair<String,String>> games) {
        this.gamesList = games;
        final Spinner dropDown = (Spinner) findViewById(R.id.gameDropdown);
        final TextView loadingText = (TextView) findViewById(R.id.gameSelectText);
        if (games.size() > 0) { // if there are games
            ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
            for (Pair<String,String> g : games) {
                dropDownAdapter.add(g.second); // Add game names to adapter
            }
            dropDown.setAdapter(dropDownAdapter);
            loadingText.setVisibility(View.INVISIBLE);
            findViewById(R.id.male_start_button).setEnabled(true);
            findViewById(R.id.female_start_button).setEnabled(true);
        }
        else { // games list is empty, tell the user
            loadingText.setText(R.string.no_games);
        }
    }

    public boolean isRunning() {
        return running;
    }
}
