package com.kch.spicydatesimulator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmationDialog extends DialogFragment {

    MessagingActivity.DialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_exit_question)
                .setPositiveButton(R.string.dialog_exit_confirmation, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(ConfirmationDialog.this);
                    }
                })
                .setNegativeButton(R.string.dialog_exit_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(ConfirmationDialog.this);
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
        } catch (ClassCastException classCast) {
            throw new ClassCastException(currentActivity.toString()
                    + " must implement ConfirmationDialogListener");
        }
    }

}
