package com.kch.spicydatesimulator;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kch.spicydatesimulator.xmlparser.SDSGame;
import com.kch.spicydatesimulator.xmlparser.SDSGameSave;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class MessagingActivity extends AppCompatActivity {
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private SDSGameSave gameSave = null;
    private SDSGame game = null;
    private Reader xmlReader;
    private Activity currentActivity = this;
    private DialogListener dialogListener;

    private String response;
    private String[] choices;
    private int score;

    private ListView messagesList;
    private ListView choicesList;
    ArrayAdapter<String> messageToPush;
    ArrayAdapter<String> choiceToPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        dialogListener = new DialogListener();
        try {
            xmlReader = new InputStreamReader(getAssets().open("game_data.xml"));
        } catch (IOException ioe) {
            Log.e("Messaging", ioe + "cannot read");
            finish();
        }

        messagesList = (ListView) findViewById(R.id.messaging_list_view);
        messageToPush = new ArrayAdapter<>(currentActivity, android.R.layout.simple_list_item_1);
        messagesList.setAdapter(messageToPush);

        choicesList = (ListView) findViewById(R.id.choices_list_view);
        choiceToPush = new ArrayAdapter<>(currentActivity, android.R.layout.simple_list_item_1);
        choicesList.setAdapter(choiceToPush);
        choicesList.setOnItemClickListener(new choiceOnItemClickListener());

        game = new SDSGame(xmlReader, gameSave);

        updateScreen();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, MediaService.class);
        bindService(i, mConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        unbindService(mConn);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        ConfirmationDialog dialogBox = new ConfirmationDialog();
        dialogBox.show(getFragmentManager(), "backButtonPressed");
    }

    private void updateScreen() {
        response = game.getResponse();
        //when response is null, we've reached the end of the game
        if(response == null) {
            EndGameDialog dialogBox = new EndGameDialog();
            dialogBox.show(getFragmentManager(), "gameEnded");
        } else {
            score++;
            messageToPush.add(response);
            messageToPush.notifyDataSetChanged();
        }
        choices = game.getChoices();
        //when choices are null, we've reached the end of the game
        if(choices == null) {

        } else {
            choiceToPush.clear();
            for(String choice : choices) {
                choiceToPush.add(choice);
            }
            choiceToPush.notifyDataSetChanged();
        }


    }

    private class choiceOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            game.makeChoice(position);
            messageToPush.add(choices[position]);
            messageToPush.notifyDataSetChanged();
            updateScreen();
        }

    }

    public DialogListener getDialogListener() {
        return dialogListener;
    }

    class DialogListener {

        private DialogListener(){/*no-op*/}

        public void onDialogPositiveClick(DialogFragment dialog) {
            // User touched the dialog's positive button
            if(dialog.getClass() == ConfirmationDialog.class) {
                finish();
            } else {
                finish();
            }

        }
        public void onDialogNegativeClick(DialogFragment dialog) {
            // User touched the dialog's negative button
            if(dialog.getClass() == ConfirmationDialog.class) {
                // do nothing, the user wants to continue playing
            } else {
                game.restart();
                messageToPush.clear();
                choiceToPush.clear();
                messagesList.setAdapter(messageToPush);
                choicesList.setAdapter(choiceToPush);
                updateScreen();
            }
        }
    }
}
