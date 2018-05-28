package com.giemper.ecocarwash;

import android.content.Context;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CarMethods
{
    public static String getYear()
    {
        return Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    }

    public static String getMonth()
    {
        return Integer.toString(Calendar.getInstance().get(Calendar.MONTH));
    }

    public static String getDay()
    {
        return Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    }

    public static String getFullDate()
    {
        return getYear() + "-" + getMonth() + "-" + getDay();
    }

//    public static String getStringDate(long millis)
//    {
//        String date = "";
//
//    }

    public static long getTodayInMillis()
    {
        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    public static String getTodayInMillisString()
    {
        return Long.toString(getTodayInMillis());
    }

    public static String getTodaySmallInString()
    {
        return Long.toString(Calendar.getInstance().getTimeInMillis() - getTodayInMillis());
    }

    private int pxToDp(int x, Context context)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) x * density);
    }
}