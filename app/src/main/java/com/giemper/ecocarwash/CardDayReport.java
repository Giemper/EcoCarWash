package com.giemper.ecocarwash;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardDayReport extends LinearLayout
{
    View rootView;
    LinearLayout layout;
    String Tag;

    public CardDayReport(Context context)
    {
        super(context);
        rootView = inflate(context, R.layout.card_dayreport, this);
        layout = rootView.findViewById(R.id.Card_DayReport_Layout);
    }

    public void setDayTitle(String date)
    {
        TextView title = rootView.findViewById(R.id.Card_DayReport_Date);
        title.setText(date);
    }

    public void addClockLine(Clocks clock)
    {
        CardDayReportClock cardDayReportClock = new CardDayReportClock(getContext());
        cardDayReportClock.setName(clock.getDryerFirstName(), clock.getDryerLastName());
        cardDayReportClock.setLicense(clock.Car.getLicense());
        cardDayReportClock.setClockHour(clock.getStartTime());
        cardDayReportClock.setClockTotal(clock.getStartTime(), clock.getEndTime());
        cardDayReportClock.setClockDry(clock.getMidTime(), clock.getEndTime());
        layout.addView(cardDayReportClock);
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


