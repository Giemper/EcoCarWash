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
    }

    public void setLicense(String license)
    {
        TextView column = rootView.findViewById(R.id.Card_DayReport_ColumnLicense);
        column.setText(license);
    }

    public void setClockEnter(long starttime, long endtime)
    {
        TextView column = rootView.findViewById(R.id.Card_DayReport_ColumnClock1);
        String hhmmss = getHHMMSS(starttime, endtime);
        column.setText(hhmmss);
    }

    public void setClockDry(long midtime, long endtime)
    {
        TextView column = rootView.findViewById(R.id.Card_DayReport_ColumnClock2);
        String hhmmss = getHHMMSS(midtime, endtime);
        column.setText(hhmmss);
    }


}
