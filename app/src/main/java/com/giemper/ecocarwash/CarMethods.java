package com.giemper.ecocarwash;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CarMethods
{
    public static String getMonth(int month)
    {
        if(month == 0)
            return "Enero";
        else if(month == 1)
            return "Febrero";
        else if(month == 2)
            return "Marzo";
        else if(month == 3)
            return "Abril";
        else if(month == 4)
            return "Mayo";
        else if(month == 5)
            return "Junio";
        else if(month == 6)
            return "Julio";
        else if(month == 7)
            return "Agosto";
        else if(month == 8)
            return "Septiembre";
        else if(month == 9)
            return "Octubre";
        else if(month == 10)
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

    public static long getTodaySmallInMillis()
    {
        return Calendar.getInstance().getTimeInMillis() - getTodayInMillis();
    }

    public static String getTodaySmallInString()
    {
        return Long.toString(getTodaySmallInMillis());
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

    public static String getMillisToStringSmall(long millis)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return String.format("%s %d, %d", getMonth(month), day, year);
    }

    public static String getHHMMSS(long time)
    {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        return new SimpleDateFormat("hh:mm:ss a", Locale.US).format(date.getTime());
    }

    public static String getHHMMSS(long start, long end)
    {
        String date = "";
        long difference = end - start;

        long hours =   difference / (60 * 60 * 1000) % 24;
        long minutes = difference / (60 * 1000) % 60;
        long seconds = difference / 1000 % 60;

        if(hours < 10)
            date += "0" + hours + ":";
        else
            date += hours + ":";

        if(minutes < 10)
            date += "0" + minutes + ":";
        else
            date += minutes + ":";

        if(seconds < 10)
            date += "0" + seconds;
        else
            date += seconds;

        return date;
    }

    public static String getDDMMYYYY(long time)
    {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);
        return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date.getTime());
    }
}