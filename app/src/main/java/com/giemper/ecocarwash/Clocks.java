package com.giemper.ecocarwash;

import android.app.Dialog;
import android.support.v7.widget.ToggleButton;
import android.support.v7.widget.ToggleGroup;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class Clocks
{
    public CarValues Car;
    private String TransactionID;
    private String DryerID;
    private String DryerFirstName;
    private String DryerLastName;
    private long StartTime;
    private long MidTime;
    private long EndTime;
    private boolean Active;

    public Clocks()
    {
        Car = new CarValues();
        StartTime = 0;
        Active = true;
    }

    public Clocks(String id)
    {
        TransactionID = id;
        Car = new CarValues();
        StartTime = 0;
        setActive(true);
    }

    public void setCarValues(Dialog dialog)
    {
        ToggleGroup Group_Pack = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_Pack);
        ToggleGroup Group_Size = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_Size);
        Spinner Spinner_Color = dialog.findViewById(R.id.Dialog_CreateCar_Spinner);
        EditText Text_Licence = dialog.findViewById(R.id.Dialog_CreateCar_Text_Licence);

        Car.setColor(Spinner_Color.getSelectedItem().toString());

        Car.setLicense(Text_Licence.getText().toString());

        for(int i = 0; i < Group_Pack.getChildCount(); i++)
        {
            ToggleButton temp = (ToggleButton)Group_Pack.getChildAt(i);
            if(temp.isChecked())
            {
                Car.setPackage(i + 1);
                break;
            }
        }

        for(int i = 0; i < Group_Size.getChildCount(); i++)
        {
            ToggleButton temp = (ToggleButton)Group_Size.getChildAt(i);
            if(temp.isChecked())
            {
                if(i == 0)
                    Car.setSize("Pequeño");
                else if(i == 1)
                    Car.setSize("Mediano");
                else
                    Car.setSize("Grande");
                break;
            }
        }
    }

    public void setTransactionID(String id)
    {
        TransactionID = id;
    }
    public String getTransactionID()
    {
        return TransactionID;
    }

    public void setDryerID(String id)
    {
        DryerID = id;
    }
    public String getDryerID()
    {
        return DryerID;
    }

    public void setDryerFirstName(String first)
    {
        DryerFirstName = first;
    }
    public String getDryerFirstName()
    {
        return DryerFirstName;
    }

    public void setDryerLastName(String name)
    {
        DryerLastName = name;
    }
    public String getDryerLastName()
    {
        return DryerLastName;
    }

    public void setStartTime(long Start)
    {
        StartTime = Start;
    }
    public long getStartTime()
    {
        return StartTime;
    }

    public void setMidTime(long Mid)
    {
        MidTime = Mid;
    }
    public long getMidTime()
    {
        return MidTime;
    }

    public void setEndTime(long End)
    {
        EndTime = End;
        setActive(false);
    }
    public long getEndTime()
    {
        return EndTime;
    }

    public void setActive(boolean act)
    {
        Active = act;
    }
    public boolean getActive()
    {
        return Active;
    }

    public void setDryer(String ID, String firstName, String lastName)
    {
        setDryerID(ID);
        setDryerFirstName(firstName);
        setDryerLastName(lastName);
    }
}