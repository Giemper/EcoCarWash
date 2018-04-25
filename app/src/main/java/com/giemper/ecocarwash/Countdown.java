package com.giemper.ecocarwash;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Countdown extends CardView
{
    private Context mContext;
    public SquareButton nextButton;
    public SquareButton stopButton;
    public CarValues values;
    public LinearLayout secondLayout2;
    public Chronometer chrono2;
    public int ClockID;

    private Calendar StartTime;
    public Calendar MidTime;
    public Calendar EndTime;


    public Countdown(Context context, Calendar _start, CarValues _values, int ID)
    {
        super(context);
        mContext = context;
        values = _values;
        StartTime = _start;
        ClockID = ID;

//                Snackbar.make(getRootView(), "Small", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        switch (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
        {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                CreateCardMDPI();
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                CreateCardMDPI();
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                CreateCardHDPI();
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                CreateCardHDPI();
                break;
            default:
                CreateCardMDPI();
                break;
        }
    }

    private void CreateCardMDPI()
    {
        this.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, pxToDp(160)));
        CardView.LayoutParams Card_Layout = (CardView.LayoutParams) this.getLayoutParams();
        Card_Layout.setMargins(0, pxToDp(1), 0, 0);
        this.requestLayout();
        this.setRadius(4);
        this.setId(View.generateViewId());

        LinearLayout bigLayout = CreateLinearLayout(1f, LinearLayout.VERTICAL, null);
        LinearLayout upperLayout = CreateLinearLayout(4f, LinearLayout.HORIZONTAL, bigLayout);
        LinearLayout lowerLayout = CreateLinearLayout(1f, LinearLayout.HORIZONTAL, bigLayout);
        LinearLayout secondLayout1 = CreateLinearLayout(3f, LinearLayout.VERTICAL, upperLayout);
        secondLayout2 = CreateLinearLayout(1f, LinearLayout.VERTICAL, upperLayout);
        LinearLayout thirdLayout1 = CreateLinearLayout(4f, LinearLayout.HORIZONTAL, secondLayout1);
        LinearLayout thirdLayout2 = CreateLinearLayout(5f, LinearLayout.HORIZONTAL, secondLayout1);
        LinearLayout forthLayout1 = CreateLinearLayout(2f, LinearLayout.VERTICAL, thirdLayout2);
        LinearLayout forthLayout2 = CreateLinearLayout(1f, LinearLayout.VERTICAL, thirdLayout2);
        bigLayout.setPadding(pxToDp(15), pxToDp(10), pxToDp(15), pxToDp(10));
        LinearLayout.LayoutParams lowerLayoutParams = (LinearLayout.LayoutParams) lowerLayout.getLayoutParams();
        lowerLayoutParams.setMargins(0, pxToDp(10), 0, 0);
        secondLayout2.setGravity(Gravity.CENTER);

        TextView textName = CreateTextView("Esperando Asignacíon", 36, Gravity.CENTER_VERTICAL, 1f, true, thirdLayout1);
        TextView textEntry = CreateTextView("Entrada", 14, Gravity.BOTTOM, 2f, true, forthLayout1);
        TextView textDry = CreateTextView("Secado", 14, Gravity.BOTTOM, 2f, true, forthLayout2);
        TextView title_pack = CreateTextView(values.getPackage() + "", 13, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 1f, false, lowerLayout);
        TextView title_size = CreateTextView(values.getSize(), 13, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 1f, false, lowerLayout);
        TextView title_color = CreateTextView(values.getColor(), 13, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 1f,false, lowerLayout);
        TextView title_license = CreateTextView(values.getLicense(), 13, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 1f,false, lowerLayout);
        textName.setTextColor(ContextCompat.getColor(mContext, R.color.grayDark));

        Chronometer chrono = CreateChronometer(24, 1f, forthLayout1);
        chrono2 = CreateChronometer(24, 1f, forthLayout2);
        chrono.setBase(SystemClock.elapsedRealtime() - ((Calendar.getInstance().getTimeInMillis() - StartTime.getTimeInMillis())));
        chrono.start();

        View middleSeparator = CreateSeparator(LinearLayout.HORIZONTAL, 1,0, 5, bigLayout);
        LinearLayout.LayoutParams middleSeparatorParams = (LinearLayout.LayoutParams)middleSeparator.getLayoutParams();
        middleSeparatorParams.setMargins(0,pxToDp(10),0,0);
        View lowerSeparator1 = CreateSeparator(LinearLayout.VERTICAL, 1,3,3, lowerLayout);
        View lowerSeparator2 = CreateSeparator(LinearLayout.VERTICAL,3, 3,3, lowerLayout);
        View lowerSeparator3 = CreateSeparator(LinearLayout.VERTICAL, 5,3,3, lowerLayout);

        nextButton = CreateSquareButton(R.drawable.ic_next, secondLayout2);
        nextButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimary)));
        stopButton = CreateSquareButton(R.drawable.ic_stop, null);
        this.addView(bigLayout);
    }

    private void CreateCardHDPI()
    {
        this.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, pxToDp(200)));
        CardView.LayoutParams Card_Layout = (CardView.LayoutParams) this.getLayoutParams();
        Card_Layout.setMargins(pxToDp(15), pxToDp(15), pxToDp(15), 0);
        this.requestLayout();
        this.setRadius(4);
        this.setId(View.generateViewId());

        LinearLayout bigLayout = CreateLinearLayout(1f, LinearLayout.VERTICAL, null);
        LinearLayout upperLayout = CreateLinearLayout(5f, LinearLayout.HORIZONTAL, bigLayout);
        LinearLayout lowerLayout = CreateLinearLayout(2f, LinearLayout.HORIZONTAL, bigLayout);
        LinearLayout secondLayout1 = CreateLinearLayout(3f, LinearLayout.VERTICAL, upperLayout);
        secondLayout2 = CreateLinearLayout(1f, LinearLayout.VERTICAL, upperLayout);
        LinearLayout thirdLayout1 = CreateLinearLayout(4f, LinearLayout.HORIZONTAL, secondLayout1);
        LinearLayout thirdLayout2 = CreateLinearLayout(5f, LinearLayout.HORIZONTAL, secondLayout1);
        LinearLayout forthLayout1 = CreateLinearLayout(2f, LinearLayout.VERTICAL, thirdLayout2);
        LinearLayout forthLayout2 = CreateLinearLayout(1f, LinearLayout.VERTICAL, thirdLayout2);
        bigLayout.setPadding(pxToDp(15), pxToDp(10), pxToDp(15), pxToDp(10));
        secondLayout2.setGravity(Gravity.CENTER);

        TextView textName = CreateTextView("Taco " + Calendar.getInstance().get(Calendar.SECOND), 36,Gravity.CENTER_VERTICAL, 1f, true,thirdLayout1);
        TextView textEntry = CreateTextView("Entrada", 14, Gravity.BOTTOM, 2f, true, forthLayout1);
        TextView textDry = CreateTextView("Secado", 14, Gravity.BOTTOM, 2f, true, forthLayout2);
        TextView title_pack = CreateTextView(values.getPackage() + "", 13, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 1f, false, lowerLayout);
        TextView title_size = CreateTextView(values.getSize(), 13, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 1f, false, lowerLayout);
        TextView title_color = CreateTextView(values.getColor(), 13, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 1f, false, lowerLayout);
        TextView title_license = CreateTextView(values.getLicense(), 13, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 1f, false, lowerLayout);
        textName.setTextColor(ContextCompat.getColor(mContext, R.color.grayDark));

        Chronometer chrono = CreateChronometer(36, 1f, forthLayout1);
        chrono2 = CreateChronometer(36, 1f, forthLayout2);
        chrono.setBase(SystemClock.elapsedRealtime() - ((Calendar.getInstance().getTimeInMillis() - StartTime.getTimeInMillis())));
        chrono.start();

        View middleSeparator = CreateSeparator(LinearLayout.HORIZONTAL, 1,0, 5, bigLayout);
        View lowerSeparator1 = CreateSeparator(LinearLayout.VERTICAL, 1,3,3, lowerLayout);
        View lowerSeparator2 = CreateSeparator(LinearLayout.VERTICAL, 3,3,3, lowerLayout);
        View lowerSeparator3 = CreateSeparator(LinearLayout.VERTICAL, 5,3,3, lowerLayout);

        nextButton = CreateSquareButton(R.drawable.ic_next, secondLayout2);
        stopButton = CreateSquareButton(R.drawable.ic_stop, null);
        this.addView(bigLayout);
    }

    private LinearLayout CreateLinearLayout(float weight, int orientation, LinearLayout ParentLayout)
    {
        LinearLayout layout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params;

        if(ParentLayout == null)
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, weight);
        else if((int)orientation == 0)
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, weight);
        else
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, weight);
        layout.setLayoutParams(params);
        layout.setOrientation((int) orientation);

        if (ParentLayout != null)
            ParentLayout.addView(layout);

        return layout;
    }

    private TextView CreateTextView(String text, int textSize, int gravity, float weight, Boolean isBold,  LinearLayout lay)
    {
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
        textView.setGravity(gravity);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        if (isBold)
            textView.setTypeface(null, Typeface.BOLD);
        textView.setText(text);

        if (lay != null)
            lay.addView(textView);

        return textView;
    }

    private Chronometer CreateChronometer(int textSize, float weight, LinearLayout ParentLayout)
    {
        Chronometer chrono = new Chronometer(mContext);
        chrono.setGravity(Gravity.CENTER_VERTICAL);
        chrono.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        chrono.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, weight));
        chrono.setTextColor(ContextCompat.getColor(mContext, R.color.grayDark));
        chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer c)
            {
                long elapsedMillis = SystemClock.elapsedRealtime() -c.getBase();
                if(elapsedMillis > 3600000L){
                    c.setFormat("0%s");
                }else{
                    c.setFormat("00:%s");
                }

            }
        });

        ParentLayout.addView(chrono);
        return chrono;
    }

    private View CreateSeparator(int orientation, int index, int marginTop, int marginBottom, LinearLayout ParentLayout)
    {
        View view = new View(mContext);
        LayoutParams params;
        if(orientation == LinearLayout.HORIZONTAL)
            params = new LayoutParams(LayoutParams.MATCH_PARENT, pxToDp(1));
        else
            params = new LayoutParams(pxToDp(1), LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0,pxToDp(marginTop),0, pxToDp(marginBottom));
        view.setLayoutParams(params);
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.separator));

        ParentLayout.addView(view, index);
        return view;
    }

    private SquareButton CreateSquareButton(int draw, LinearLayout ParentLayout)
    {
        SquareButton button = new SquareButton(mContext);
        button.setId(View.generateViewId());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.setMargins(pxToDp(10), pxToDp(10), pxToDp(10), pxToDp(10));
        button.setLayoutParams(params);
        button.setBackgroundResource(draw);
        button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.grayDark)));

        if(ParentLayout != null)
            ParentLayout.addView(button);

        return button;
    }

    private int pxToDp(int x)
    {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round((float) x * density);
    }
}
