package com.kch.spicydatesimulator;

import android.app.Activity;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kch.spicydatesimulator.xmlparser.MessageListAdapter;
import com.kch.spicydatesimulator.xmlparser.SDSGame;
import com.kch.spicydatesimulator.xmlparser.SDSGameSave;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class MessagingActivity extends AppCompatActivity implements ConfirmationDialog.ConfirmationDialogListener {

    private SDSGameSave gameSave = null;
    private SDSGame game = null;
    private Activity currentActivity = this;

    private String response;
    private String[] choices;

    private ListView messagesList;
    private ListView choicesList;
    MessageListAdapter messageToPush;
    ArrayAdapter<String> choiceToPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        Reader xmlReader = null;
        try {
            xmlReader = new InputStreamReader(getAssets().open("game_data.xml"));
        } catch (IOException ioe) {
            Log.e("Messaging", ioe + "cannot read");
            finish();
        }

        messagesList = (ListView) findViewById(R.id.messaging_list_view);
        messageToPush = new MessageListAdapter(currentActivity);
        messagesList.setAdapter(messageToPush);

        choicesList = (ListView) findViewById(R.id.choices_list_view);
        choiceToPush = new ArrayAdapter<>(currentActivity, android.R.layout.simple_list_item_1);
        choicesList.setAdapter(choiceToPush);
        choicesList.setOnItemClickListener(new choiceOnItemClickListener());

        game = new SDSGame(xmlReader, gameSave);

        updateScreen();
    }

    @Override
    public void onBackPressed() {
        ConfirmationDialog dialogBox = new ConfirmationDialog();
        dialogBox.show(getFragmentManager(), "backButtonPressed");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        // do nothing if the user wants to continue playing
    }

    private void updateScreen() {
        response = game.getResponse();
        //when response is null, we've reached the end of the game
        if(response == null) {
            finish();
        } else {
            messageToPush.add(response);
            messageToPush.notifyDataSetChanged();
        }
        choices = game.getChoices();
        //when choices are null, we've reached the end of the game
        if(choices == null) {
            finish();
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
}
