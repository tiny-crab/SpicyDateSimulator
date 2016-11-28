package com.kch.spicydatesimulator;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MessagingActivity extends AppCompatActivity implements ConfirmationDialog.ConfirmationDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

        }
        return super.onOptionsItemSelected(item);
    }

    //TODO: Back button press pops up a confirmation to exit game (If you leave, your progress will be lost!)
    //TODO: Press a button and the correct text appears in the listview
    //TODO: After message is sent, buttons update with new prompts

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
        //do nothing if the user wants to continue playing
    }
}
