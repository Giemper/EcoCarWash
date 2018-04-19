package com.giemper.ecocarwash;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Countdown extends CardView
{
    private Context mContext;
    private TextView TimeView;
//    public CountDownTimer Timer;
//    public Chronometer Timer;
    public long currentMillis;
    public SquareButton stopButton;
    public CreateCarValue values;

    private Calendar StartTime;

    public Countdown(Context context, CreateCarValue _values, Calendar _start)
    {
        super(context);
        mContext = context;
        values = _values;
        StartTime = _start;

        switch (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
        {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
//                Snackbar.make(getRootView(), "Small", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                CreateCardMDPI();
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
//                Snackbar.make(getRootView(), "Normal", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                CreateCardMDPI();
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
//                Snackbar.make(getRootView(), "Large", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                CreateCardHDPI();
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
//                Snackbar.make(getRootView(), "XLarge", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                CreateCardHDPI();
                break;
            default:
//                Snackbar.make(getRootView(), "Default", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                CreateCardMDPI();
                break;
        }
    }

    private void CreateCardMDPI()
    {
        this.setLayoutParams(new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                pxToDp(160)
        ));
        CardView.LayoutParams Card_Layout = (CardView.LayoutParams) this.getLayoutParams();
        Card_Layout.setMargins(pxToDp(0), pxToDp(1), pxToDp(0), 0);
        this.requestLayout();
        this.setRadius(4);
        this.setId(View.generateViewId());


        LinearLayout bigLayout = CreateLinearLayout(1f, LinearLayout.HORIZONTAL, null);
        bigLayout.setPadding(pxToDp(15), pxToDp(15), pxToDp(15), pxToDp(15));
        LinearLayout secondLayout1 = CreateLinearLayout(1f, LinearLayout.VERTICAL, bigLayout);

        LinearLayout thirdLayout1 = CreateLinearLayout(1f, LinearLayout.HORIZONTAL, secondLayout1);
        TextView Name = CreateTextView("Taco " + Calendar.getInstance().get(Calendar.SECOND), 30, true, thirdLayout1);
//        TimeView = CreateTextView("00:00:00", 28, false, thirdLayout1);
        Chronometer chrono = CreateChronometer(28, thirdLayout1);
        chrono.setBase(SystemClock.elapsedRealtime() - ((Calendar.getInstance().getTimeInMillis() - StartTime.getTimeInMillis())));

        LinearLayout thirdLayout2 = CreateLinearLayout(2f, LinearLayout.VERTICAL, secondLayout1);
        LinearLayout forthLayout1 = CreateLinearLayout(1f, LinearLayout.HORIZONTAL, thirdLayout2);
        TextView title_marca = CreateTextView("Paquete", 13, true, forthLayout1);
        TextView title_modelo = CreateTextView("Tamaño", 13, true, forthLayout1);
        TextView title_ano = CreateTextView("Color", 13, true, forthLayout1);
        TextView title_placa = CreateTextView("Placas", 13, true, forthLayout1);
        LinearLayout forthLayout2 = CreateLinearLayout(1f, LinearLayout.HORIZONTAL, thirdLayout2);
        TextView info_marca = CreateTextView(values.Package + "", 13, false, forthLayout2);
        TextView info_modelo = CreateTextView(values.Size + "", 13, false, forthLayout2);
        TextView info_ano = CreateTextView(values.Color, 13, false, forthLayout2);
        TextView info_placa = CreateTextView(values.License, 13, false, forthLayout2);

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

    private void CreateCardHDPI()
    {
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
        TextView Name = CreateTextView("Taco " + Calendar.getInstance().get(Calendar.SECOND), 36, true, thirdLayout1);
//        TimeView = CreateTextView("00:00:00", 36, false, thirdLayout1);
        Chronometer chrono = CreateChronometer(36, thirdLayout1);


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

    private LinearLayout CreateLinearLayout(float weight, int orientation, LinearLayout ParentLayout)
    {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, weight));
        layout.setOrientation((int) orientation);

        if (ParentLayout != null)
            ParentLayout.addView(layout);

        return layout;
    }

    private TextView CreateTextView(String text, int textSize, Boolean isBold, LinearLayout lay)
    {
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        if (isBold)
            textView.setTypeface(null, Typeface.BOLD);
        textView.setText(text);

        if (lay != null)
            lay.addView(textView);

        return textView;
    }

    private Chronometer CreateChronometer(int textSize, LinearLayout ParentLayout)
    {
        Chronometer chrono = new Chronometer(mContext);
        //        chrono.setFormat("HH:MM:SS");
        chrono.setGravity(Gravity.CENTER_VERTICAL);
        chrono.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        chrono.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer chronometer)
            {
//                Snackbar.make(getRootView(), (int)(SystemClock.elapsedRealtime() - chronometer.getBase()) + "", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        chrono.start();
        ParentLayout.addView(chrono);

        return chrono;
    }

    private int pxToDp(int x)
    {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round((float) x * density);
    }
}
