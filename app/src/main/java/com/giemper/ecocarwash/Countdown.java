package com.giemper.ecocarwash;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import java.util.Calendar;

/**
 * Created by gmoma on 2/1/2018.
 */

public class Countdown
{
    Context AppContext;
    public CardView card;

    public Countdown(Context context)
    {
        AppContext = context;
        card = new CardView(AppContext);
        card.setLayoutParams(new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                pxToDp(100)
        ));
        CardView.LayoutParams Card_Layout = (CardView.LayoutParams) card.getLayoutParams();
        Card_Layout.setMargins(pxToDp(15), pxToDp(15), pxToDp(15), 0);
        card.requestLayout();
        card.setRadius(4);
        card.setContentPadding(15,15,15,15);

        TextView tv = new TextView(AppContext);
        tv.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        ));
        tv.setText("Taco" + Calendar.getInstance().get(Calendar.SECOND));

        card.addView(tv);
    }

    private int pxToDp(int x)
    {
        float density = AppContext.getResources().getDisplayMetrics().density;
        return Math.round((float) x * density);
    }
}
