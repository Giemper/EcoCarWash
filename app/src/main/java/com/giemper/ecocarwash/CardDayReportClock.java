package com.giemper.ecocarwash;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import static com.giemper.ecocarwash.CarMethods.getHHMMSS;

public class CardDayReportClock extends LinearLayout
{
    View rootView;

    public CardDayReportClock(Context context)
    {
        super(context);
        rootView = inflate(context, R.layout.card_dayreport_clock, this);
    }

    public void setName(String firstName, String lastName)
    {
        TextView column = rootView.findViewById(R.id.Card_DayReport_ColumnName);
        column.setText(firstName);
    }

    public void setLicense(String license)
    {
        TextView column = rootView.findViewById(R.id.Card_DayReport_ColumnLicense);
        column.setText(license);
    }

    public void setClockHour(long starttime)
    {
        TextView column  = rootView.findViewById(R.id.Card_DayReport_ColumnClockHour);
        String hhmmss = getHHMMSS(starttime);
        column.setText(hhmmss);
    }

    public void setClockTotal(long starttime, long endtime)
    {
        TextView column = rootView.findViewById(R.id.Card_DayReport_ColumnClockTotal);
        String hhmmss = getHHMMSS(starttime, endtime);
        column.setText(hhmmss);
    }

    public void setClockDry(long midtime, long endtime)
    {
        TextView column = rootView.findViewById(R.id.Card_DayReport_ColumnClockDryTime);
        String hhmmss = getHHMMSS(midtime, endtime);
        column.setText(hhmmss);
    }


}
