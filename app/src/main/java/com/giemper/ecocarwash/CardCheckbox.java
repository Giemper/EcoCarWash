package com.giemper.ecocarwash;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;

public class CardCheckbox extends LinearLayout
{
    private Context mContext;
    public CheckBox Box;
    public SquareButton InfoButton;

    public CardCheckbox(Context context, Dryer dryer)
    {
        super(context);

        LinearLayout ll = (LinearLayout) inflate(context, R.layout.card_checkbox, this);
        Box = ll.findViewById(R.id.Dryer_Checkbox);
        Box.setText(dryer.getFirstName() + " " + dryer.getLastNameFather());

        InfoButton = ll.findViewById(R.id.Dryer_InfoButton);
    }
}
