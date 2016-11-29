package com.kch.spicydatesimulator;

import android.app.Activity;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

    private Button option1Button, option2Button, option3Button, option4Button;
    private ListView messagesList;
    ArrayAdapter<String> messageToPush;

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
        messageToPush = new ArrayAdapter<>(currentActivity, android.R.layout.simple_list_item_1);
        messagesList.setAdapter(messageToPush);

        game = new SDSGame(xmlReader, gameSave);

        option1Button = (Button) findViewById(R.id.option1);
        option2Button = (Button) findViewById(R.id.option2);
        option3Button = (Button) findViewById(R.id.option3);
        option4Button = (Button) findViewById(R.id.option4);

        option1Button.setOnClickListener(new choiceOnClickListener(0));
        option2Button.setOnClickListener(new choiceOnClickListener(1));
        option3Button.setOnClickListener(new choiceOnClickListener(2));
        option4Button.setOnClickListener(new choiceOnClickListener(3));

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
        if(response == null) {
            finish();
        } else {
            messageToPush.add(response);
            messageToPush.notifyDataSetChanged();
        }
        choices = game.getChoices();
        //reached the end of the game
        if(choices == null) {
            finish();
        } else {
            option1Button.setText(choices[0]);
            option2Button.setText(choices[1]);
            option3Button.setText(choices[2]);
            option4Button.setText(choices[3]);
        }


    }

    private class choiceOnClickListener implements View.OnClickListener{

        private int choiceIndex;

        choiceOnClickListener(int choiceIndex) {
            this.choiceIndex = choiceIndex;
        }

        @Override
        public void onClick(View v) {
            game.makeChoice(choiceIndex);

            messageToPush.add(choices[choiceIndex]);
            messageToPush.notifyDataSetChanged();

            updateScreen();
        }
    }
}
