package com.giemper.ecocarwash;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class CardCheckbox extends LinearLayout
{
    private Context mContext;
    public CheckBox Box;

    public CardCheckbox(Context context, Dryer _dryer)
    {
        super(context);
        mContext = context;
        LinearLayout ll = (LinearLayout) inflate(mContext, R.layout.card_checkbox, this);
        Box = ll.findViewById(R.id.Recycler_Checkbox);
        Box.setText(_dryer.getFirstName() + " " + _dryer.getLastNameFather());
    }
}
