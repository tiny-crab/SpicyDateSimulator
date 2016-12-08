package com.kch.spicydatesimulator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

public class EndGameDialog extends DialogFragment {

    private int score;

    MessagingActivity.DialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Resources res = getActivity().getResources();
        String message = res.getString(R.string.end_game_notice)
                + res.getString(R.string.score_prefix)  + Integer.toString(score)
                + res.getString(R.string.score_suffix);
        builder.setMessage(message)
                .setPositiveButton(R.string.end_game_quit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(EndGameDialog.this);
                    }
                })
                .setNegativeButton(R.string.end_game_again, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(EndGameDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MessagingActivity currentActivity = (MessagingActivity) getActivity();
        try {
            mListener = currentActivity.getDialogListener();
            score = currentActivity.getScore();
        } catch (ClassCastException classCast) {
            throw new ClassCastException(currentActivity.toString()
                    + " must implement ConfirmationDialogListener");
        }
    }

}
