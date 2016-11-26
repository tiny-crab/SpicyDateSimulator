package com.example.evan.spicydatingsim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SDSGame game = new SDSGame(new InputStreamReader(getAssets().open("game1.xml")));
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
}
