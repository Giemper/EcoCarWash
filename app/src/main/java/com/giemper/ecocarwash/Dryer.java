package com.giemper.ecocarwash;

import java.util.Calendar;

/**
 * Created by guillermo.magdaleno on 4/27/2018.
 */

public class Dryer
{
    private int ID;
    private String FirstName;
    private String MiddleName;
    private String LastNameFather;
    private String LastNameMother;
    private Calendar StartTime;
    private Calendar EndTime;
    private boolean isActive;

    public Dryer(String first, String middle, String lastFather, String lastMother, Calendar start)
    {
        FirstName = first;
        MiddleName = middle;
        LastNameFather = lastFather;
        LastNameMother = lastMother;
        StartTime = start;
        isActive = true;
    }

    public int getID()
    {
        return ID;
    }

    public String getFirstName()
    {
        return FirstName;
    }

    public String getMiddleName()
    {
        return MiddleName;
    }

    public String getLastNameFather()
    {
        return LastNameFather;
    }

    public String getLastNameMother()
    {
        return LastNameMother;
    }

    public Calendar getStartTime()
    {
        return StartTime;
    }

    public Calendar getEndTime()
    {
        return EndTime;
    }
    public void setEndTime(Calendar end)
    {
        EndTime = end;
        isActive = false;
    }

    public boolean getActive()
    {
        return isActive;
    }


}
