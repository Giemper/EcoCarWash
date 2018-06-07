package com.giemper.ecocarwash;

public class Dryer
{
    private String DryerID;
    private String FirstName;
    private String LastNameFather;
    private String LastNameMother;
    private long StartTime;
    private long EndTime;
    private boolean Active;
    private String WorkStatus;
    private long Queue;
    private int CarWashed;

    public Dryer()
    {
        setActive(true);
        CarWashed = 0;
    }

    public String getDryerID()
    {
        return DryerID;
    }
    public void setDryerID(String id)
    {
        DryerID = id;
    }

    public String getFirstName()
    {
        return FirstName;
    }
    public void setFirstName(String name)
    {
        FirstName = name;
    }

    public String getLastNameFather()
    {
        return LastNameFather;
    }
    public void setLastNameFather(String name)
    {
        LastNameFather = name;
    }

    public String getLastNameMother()
    {
        return LastNameMother;
    }
    public void setLastNameMother(String name)
    {
        LastNameMother = name;
    }

    public long getStartTime()
    {
        return StartTime;
    }
    public void setStartTime(long start)
    {
        StartTime = start;
    }

    public long getEndTime()
    {
        return EndTime;
    }
    public void setEndTime(long end)
    {
        EndTime = end;

        if(end > 0)
        {
            setActive(false);
            setWorkStatus("None");
        }
    }

    public boolean getActive()
    {
        return Active;
    }
    public void setActive(boolean act)
    {
        Active = act;
        if(act)
            setEndTime(0);
    }

    public String getWorkStatus()
    {
        return WorkStatus;
    }
    public void setWorkStatus(String status)
    {
        WorkStatus = status;
    }

    public long getQueue()
    {
        return Queue;
    }
    public void setQueue(long queue)
    {
        Queue = queue;
    }

    public int getCarWashed()
    {
        return CarWashed;
    }
    public void setCarWashed(int washed)
    {
        CarWashed = washed;
    }
    public void addCarWashed()
    {
        CarWashed++;
    }

    public String fullName()
    {
        return FirstName + " " + LastNameFather + " " + LastNameMother;
    }
    public String fullLastName()
    {
        return LastNameFather + " " + LastNameMother;
    }
}