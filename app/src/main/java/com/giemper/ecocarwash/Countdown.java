package com.giemper.ecocarwash;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Countdown
{
    Context mContext;
    CardView card;

    public Countdown(Context context)
    {
        mContext = context;

        card = CreateCard();

        LinearLayout bigLayout = CreateLinearLayout((float) 1.0, LinearLayout.HORIZONTAL);
        bigLayout.setPadding(pxToDp(15), pxToDp(15), pxToDp(15), pxToDp(15));
        LinearLayout secondLayout1 = CreateLinearLayout((float) 1, LinearLayout.VERTICAL);
//        LinearLayout thirdLayout1 = CreateLinearLayout((float) 0.5, LinearLayout.HORIZONTAL);
//        TextView Name = CreateTextView("Luis", 36, true);
//        TextView Time = CreateTextView("00:00:00", 36, false);
//        thirdLayout1.addView(Time);
//        thirdLayout1.addView(Name);
//
//
//        LinearLayout thirdLayout2 = CreateLinearLayout((float) 0.5, LinearLayout.VERTICAL);
//        secondLayout1.addView(thirdLayout2);
//        secondLayout1.addView(thirdLayout1);
        secondLayout1.setBackgroundColor(Color.BLUE);






        LinearLayout secondLayout2 = CreateLinearLayout((float) 3, LinearLayout.HORIZONTAL);
//        secondLayout2.setPadding(15, 15, 15,15);
//        SquareButton stopButton = new SquareButton(mContext);
//        stopButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        stopButton.setBackgroundResource(R.drawable.ic_stop);
//        SquareButton sb = new SquareButton(mContext);
//        sb.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        sb.setBackgroundResource(R.drawable.ic_add);
//        secondLayout2.addView(stopButton);
//        secondLayout1.setBackgroundColor(Color.BLUE);
//        secondLayout1.addView(sb);
        secondLayout2.setBackgroundColor(Color.GREEN);


        bigLayout.addView(secondLayout1);
        bigLayout.addView(secondLayout2);
        card.addView(bigLayout);

//        TextView tv = new TextView(mContext);
//        tv.setLayoutParams(new LayoutParams(
//                LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT
//        ));
//        tv.setText("Taco" + Calendar.getInstance().get(Calendar.SECOND));
//
//        card.addView(tv);
    }

    private CardView CreateCard()
    {
        CardView cardView = new CardView(mContext);
        cardView.setLayoutParams(new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                pxToDp(160)
        ));
        CardView.LayoutParams Card_Layout = (CardView.LayoutParams) cardView.getLayoutParams();
        Card_Layout.setMargins(pxToDp(15), pxToDp(15), pxToDp(15), 0);
        cardView.requestLayout();
        cardView.setRadius(4);

        return cardView;
    }

    private LinearLayout CreateLinearLayout(float weight, int orientation)
    {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, weight));
        layout.setOrientation((int) orientation);

        return layout;
    }

    private TextView CreateTextView(String text, int textSize, Boolean isBold)
    {
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize((float) textSize);
        if(isBold)
            textView.setTypeface(null, Typeface.BOLD);
        textView.setText(text);

        return textView;
    }

    private int pxToDp(int x)
    {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round((float) x * density);
    }
}
