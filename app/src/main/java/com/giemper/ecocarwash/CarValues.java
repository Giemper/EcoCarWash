package com.giemper.ecocarwash;

/**
 * Created by guillermo.magdaleno on 4/25/2018.
 */

public class CarValues
{
    private int Package;
    private String Size;
    private String Color;
    private String License;

    public void setPackage(int _package)
    {
        Package = _package;
    }
    public int getPackage()
    {
        return Package;
    }

    public void setSize (String _size)
    {
        Size = _size;
    }
    public String getSize()
    {
        return Size;
    }

    public void setColor(String _color)
    {
        Color = _color;
    }
    public String getColor()
    {
        return Color;
    }

    public void setLicense(String _license)
    {
        License = _license;
    }
    public String getLicense()
    {
        return License;
    }
}
