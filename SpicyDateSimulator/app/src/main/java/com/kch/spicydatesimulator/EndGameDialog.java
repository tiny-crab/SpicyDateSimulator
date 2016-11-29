package com.kch.spicydatesimulator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class EndGameDialog extends DialogFragment {

    public interface EndGameDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    ConfirmationDialog.ConfirmationDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_exit_question)
                .setPositiveButton(R.string.dialog_exit_confirmation, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(EndGameDialog.this);
                    }
                })
                .setNegativeButton(R.string.dialog_exit_cancel, new DialogInterface.OnClickListener() {
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
        Activity currentActivity = getActivity();
        try {
            //mListener = (EndGameDialog.EndGameDialogListener) currentActivity;
        } catch (ClassCastException classCast) {
            throw new ClassCastException(currentActivity.toString()
                    + " must implement ConfirmationDialogListener");
        }
    }

}
