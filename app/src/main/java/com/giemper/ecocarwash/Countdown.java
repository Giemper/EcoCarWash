package com.giemper.ecocarwash;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class Countdown extends CardView {
    private Context mContext;
    public SquareButton stopButton;

    public Countdown(Context context) {
        super(context);
        mContext = context;
        CreateCard();
    }

    public Countdown(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        CreateCard();

    }

    private void CreateCard() {
        this.setLayoutParams(new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                pxToDp(160)
        ));
        CardView.LayoutParams Card_Layout = (CardView.LayoutParams) this.getLayoutParams();
        Card_Layout.setMargins(pxToDp(15), pxToDp(15), pxToDp(15), 0);
        this.requestLayout();
        this.setRadius(4);
        this.setId(View.generateViewId());


        LinearLayout bigLayout = CreateLinearLayout(1f, LinearLayout.HORIZONTAL, null);
        bigLayout.setPadding(pxToDp(15), pxToDp(15), pxToDp(15), pxToDp(15));
        LinearLayout secondLayout1 = CreateLinearLayout(1f, LinearLayout.VERTICAL, bigLayout);

        LinearLayout thirdLayout1 = CreateLinearLayout(1f, LinearLayout.HORIZONTAL, secondLayout1);
        TextView Name = CreateTextView("Luis " + Calendar.getInstance().get(Calendar.SECOND), 36, true, thirdLayout1);
        TextView Time = CreateTextView("00:00:00", 36, false, thirdLayout1);

        LinearLayout thirdLayout2 = CreateLinearLayout(1f, LinearLayout.VERTICAL, secondLayout1);
        LinearLayout forthLayout1 = CreateLinearLayout(1f, LinearLayout.HORIZONTAL, thirdLayout2);
        TextView title_marca = CreateTextView("Marca", 18, true, forthLayout1);
        TextView title_modelo = CreateTextView("Modelo", 18, true, forthLayout1);
        TextView title_ano = CreateTextView("Año", 18, true, forthLayout1);
        TextView title_placa = CreateTextView("Placas", 18, true, forthLayout1);
        LinearLayout forthLayout2 = CreateLinearLayout(1f, LinearLayout.HORIZONTAL, thirdLayout2);
        TextView info_marca = CreateTextView("Chevrolet", 18, false, forthLayout2);
        TextView info_modelo = CreateTextView("Chevy", 18, false, forthLayout2);
        TextView info_ano = CreateTextView("2007", 18, false, forthLayout2);
        TextView info_placa = CreateTextView("AK-4785", 18, false, forthLayout2);

        LinearLayout secondLayout2 = CreateLinearLayout(3f, LinearLayout.HORIZONTAL, bigLayout);
        secondLayout2.setPadding(pxToDp(15), pxToDp(15), pxToDp(15), pxToDp(15));
        secondLayout2.setGravity(Gravity.CENTER);

        stopButton = new SquareButton(mContext);
        stopButton.setId(View.generateViewId());
        stopButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        stopButton.setBackgroundResource(R.drawable.ic_stop);
        secondLayout2.addView(stopButton);
        this.addView(bigLayout);
    }

    private LinearLayout CreateLinearLayout(float weight, int orientation, LinearLayout ParentLayout) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, weight));
        layout.setOrientation((int) orientation);

        if (ParentLayout != null)
            ParentLayout.addView(layout);

        return layout;
    }

    private TextView CreateTextView(String text, int textSize, Boolean isBold, LinearLayout lay) {
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize((float) pxToDp(textSize));
        if (isBold)
            textView.setTypeface(null, Typeface.BOLD);
        textView.setText(text);

        if (lay != null)
            lay.addView(textView);

        return textView;
    }

    private int pxToDp(int x) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round((float) x * density);
    }
}
