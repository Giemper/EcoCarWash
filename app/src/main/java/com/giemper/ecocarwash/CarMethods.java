package com.giemper.ecocarwash;

import java.util.Calendar;

public class CarMethods
{
    public static String getYear()
    {
        return Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    }

    public static  String getMonth()
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
}
