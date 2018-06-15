package com.giemper.ecocarwash;

public class DryerAttendance
{
    private String DryerID;
    private String DryerName;
    private long Date;
    private String Action;

    public String getDryerID()
    {
        return DryerID;
    }
    public void setDryerID(String id)
    {
        DryerID = id;
    }

    public String getDryerName()
    {
        return DryerName;
    }
    public void setDryerName(String name)
    {
        DryerName = name;
    }

    public long getDate()
    {
        return Date;
    }
    public void setDate(long start)
    {
        Date = start;
    }

    public String getAction()
    {
        return Action;
    }
    public void setAction(String act)
    {
        Action = act;
    }
}
