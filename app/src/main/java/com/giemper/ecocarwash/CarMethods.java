package com.giemper.ecocarwash;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CarMethods
{

//    public static String getFullDate()
//    {
////        return getYear() + "-" + getMonth() + "-" + getDay();
//        return "";
//    }

    public static String getMonth(int month)
    {
        if(month == 1)
            return "Enero";
        else if(month == 2)
            return "Febrero";
        else if(month == 3)
            return "Marzo";
        else if(month == 4)
            return "Abril";
        else if(month == 5)
            return "Mayo";
        else if(month == 6)
            return "Junio";
        else if(month == 7)
            return "Julio";
        else if(month == 8)
            return "Agosto";
        else if(month == 9)
            return "Septiembre";
        else if(month == 10)
            return "Octubre";
        else if(month == 11)
            return "Noviembre";
        else
            return "Diciembre";
    }

    public static String getDayOfWeek(int week)
    {
        if(week == 1)
            return "Domingo";
        else if(week == 2)
            return "Lunes";
        else if(week == 3)
            return "Martes";
        else if(week == 4)
            return "Miercoles";
        else if(week == 5)
            return "Jueves";
        else if(week == 6)
            return "Viernes";
        else
            return "Sabado";
    }

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

    public static String getMillisToString(long millis)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int week = cal.get(Calendar.DAY_OF_WEEK);

        return String.format("%s %d de %s %d", getDayOfWeek(week), day, getMonth(month), year);
    }

    public static String getHHMMSS(long start, long end)
    {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(end - start);

        return new SimpleDateFormat("HH:mm:ss").format(date.getTime());
    }
    private int pxToDp(int x, Context context)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) x * density);
    }
}