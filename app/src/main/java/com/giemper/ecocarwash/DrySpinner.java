package com.giemper.ecocarwash;

public class DrySpinner
{
    private String ID;
    private String FirstName;
    private String LastName;

    public DrySpinner(String id, String first, String last)
    {
        ID = id;
        FirstName = first;
        LastName = last;
    }

    public String toString()
    {
        return FirstName + " " + LastName;
    }
    public String getTag()
    {
        return ID;
    }
    public String getFirstName()
    {
        return FirstName;
    }
    public String getLastName()
    {
        return LastName;
    }
}
