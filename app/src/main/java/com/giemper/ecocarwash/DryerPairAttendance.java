package com.giemper.ecocarwash;

public class DryerPairAttendance extends DryerAttendance
{
    private boolean isComplete;
    private long StartTime;
    private long EndTime;

    public DryerPairAttendance(DryerAttendance attendance)
    {
        setDryerID(attendance.getDryerID());
        setDryerName(attendance.getDryerName());

        setComplete(false);
        setStartTime(0L);
        setEndTime(0L);
    }

    public boolean getComplete()
    {
        return isComplete;
    }
    private void setComplete(boolean state)
    {
        isComplete = state;
    }

    public long getStartTime()
    {
        return StartTime;
    }
    public void setStartTime(long start)
    {
        StartTime = start;

        if(StartTime != 0 && EndTime != 0 && StartTime < EndTime)
            setComplete(true);
    }

    public long getEndTime()
    {
        return EndTime;
    }
    public void setEndTime(long end)
    {
        EndTime = end;

        if(StartTime != 0 && EndTime != 0 && StartTime < EndTime)
            setComplete(true);
    }
}
