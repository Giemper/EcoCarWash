package com.giemper.ecocarwash;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;

public class Countdown extends LinearLayout
{
    private Context mContext;
    public SquareButton nextButton;
    public SquareButton stopButton;
    public Chronometer chrono1;
    public Chronometer chrono2;
    public TextView textName;
    public TextView textPackage;
    public TextView textSize;
    public TextView textColor;
    public TextView textLicense;


    public CarValues values;
    public LinearLayout secondLayout2;


    public String ClockID;

    public Clocks clock;

    private Calendar StartTime;
    public Calendar MidTime;
    public Calendar EndTime;


    public Countdown(Context context, Clocks _clock)
    {
        super(context);

        mContext = context;
        clock = _clock;

        StartTime = Calendar.getInstance();
        StartTime.setTimeInMillis(clock.getStartTime());
        values = clock.Car;
        ClockID = clock.getTransactionID();

        FillCountdown();
    }

    private void FillCountdown()
    {
        LinearLayout card = (LinearLayout) inflate(getContext(), R.layout.card_chronometer, this);
        textName = card.findViewById(R.id.Card_Chronometer_Name);
        chrono1 = card.findViewById(R.id.Card_Chronometer_ChronoEnter);
        chrono2 = card.findViewById(R.id.Card_Chronometer_ChronoDry);
        setChronometerTick(chrono1);
        setChronometerTick(chrono2);

        chrono1.setBase(SystemClock.elapsedRealtime() - ((Calendar.getInstance().getTimeInMillis() - StartTime.getTimeInMillis())));
        chrono1.start();

        textSize = card.findViewById(R.id.Card_Chronometer_Slot1);
        textSize.setText(values.getSize());

        textPackage = card.findViewById(R.id.Card_Chronometer_Slot2);
        textPackage.setText(Integer.toString(values.getPackage()));

        textColor = card.findViewById(R.id.Card_Chronometer_Slot3);
        textColor.setText(values.getColor());

        textLicense = card.findViewById(R.id.Card_Chronometer_Slot4);
        textLicense.setText(values.getLicense());

        nextButton = card.findViewById(R.id.Card_Chronometer_NextButton);
        stopButton = card.findViewById(R.id.Card_Chronometer_StopButton);
    }

    private void setChronometerTick(Chronometer chrono)
    {
        chrono.setOnChronometerTickListener((Chronometer c) ->
        {
            long elapsedMillis = SystemClock.elapsedRealtime() -c.getBase();
            if(elapsedMillis > 3600000L){
                c.setFormat("0%s");
            }else{
                c.setFormat("00:%s");
            }
        });
    }


}