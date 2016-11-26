package com.kch.spicydatesimulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        final Intent toMessaging = new Intent(this, MessagingActivity.class);

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
}
