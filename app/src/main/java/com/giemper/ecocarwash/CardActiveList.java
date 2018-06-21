package com.giemper.ecocarwash;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardActiveList extends LinearLayout
{
    private String Tag;

    public CardActiveList(Context context, Dryer dryer)
    {
        super(context);

        LinearLayout ll = (LinearLayout) inflate(context, R.layout.card_activelist, this);

        TextView text = ll.findViewById(R.id.Dryer_ActiveList);
        text.setText(dryer.getFirstName() + " " + dryer.getLastNameFather());
    }

    public void setTag(String tag)
    {
        Tag = tag;
    }
    public String getTag()
    {
        return Tag;
    }
}
