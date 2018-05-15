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
    private int TransactionID;
    private int DryerID;
    private String DryerName;
    private long StartTime;
    private long MidTime;
    private long EndTime;
    private boolean isActive;

    public Clocks(int ID)
    {
        TransactionID = ID;
        isActive = true;
        Car = new CarValues();
        StartTime = 0;
        DryerName = "TBA";
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

    public int getTransactionID()
    {
        return TransactionID;
    }
    public String getTransactionID_String()
    {
        return Integer.toString(TransactionID);
    }

    public void setDryer(int ID, String Name)
    {
        DryerID = ID;
        DryerName = Name;
    }
    public int getDryerID()
    {
        return DryerID;
    }
    public String getDryerName()
    {
        return DryerName;
    }


    public void setStartTime(Calendar Start)
    {
        StartTime = Start.getTimeInMillis();
    }
    public long getStartTime()
    {
        return StartTime;
    }

    public void setMidTime(Calendar Mid)
    {
        MidTime = Mid.getTimeInMillis();
    }
    public long getMidTime()
    {
        return MidTime;
    }

    public void setEndTime(Calendar End)
    {
        EndTime = End.getTimeInMillis();
    }
    public long getEndTime()
    {
        return EndTime;
    }

    public void setActive(Boolean Active)
    {
        isActive = Active;
    }
    public Boolean getActive()
    {
        return isActive;
    }
}