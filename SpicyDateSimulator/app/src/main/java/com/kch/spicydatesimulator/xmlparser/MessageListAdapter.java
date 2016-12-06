package com.kch.spicydatesimulator.xmlparser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kch.spicydatesimulator.R;

/**
 * Created by evan on 12/1/2016.
 */

public class MessageListAdapter extends ArrayAdapter {
    private static int RES_COLOR;
    private static int CHOICE_COLOR;

    public MessageListAdapter(Context context) {
        super(context, R.layout.response_item);
        RES_COLOR = context.getResources().getColor(R.color.colorPrimary);
        CHOICE_COLOR = context.getResources().getColor(R.color.colorPrimaryDark);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView,parent);
        if (position % 2 == 0) {
            v.setBackgroundColor(RES_COLOR);
        }
        else {
            v.setBackgroundColor(CHOICE_COLOR);
            v.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        }
        return v;
    }
}
