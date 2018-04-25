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
    private Calendar StartTime;
    private Calendar MidTime;
    private Calendar EndTime;
    private boolean isActive;

    public Clocks(int ID)
    {
        TransactionID = ID;
        isActive = true;
    }

    public void setCarValues(Dialog dialog)
    {
        ToggleGroup Group_Pack = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_Pack);
        ToggleGroup Group_Size = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_Size);
        Spinner Spinner_Color = dialog.findViewById(R.id.Dialog_CreateCar_Spinner);
        EditText Text_Licence =dialog.findViewById(R.id.Dialog_CreateCar_Text_Licence);

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

        int sizeInt = 0;
        for(int i = 0; i < Group_Size.getChildCount(); i++)
        {
            ToggleButton temp = (ToggleButton)Group_Size.getChildAt(i);
            if(temp.isChecked())
            {
                if(sizeInt == 0)
                    Car.setSize("Pequeño");
                else if(sizeInt == 1)
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
        StartTime = Start;
    }
    public Calendar getStartTime()
    {
        return StartTime;
    }

    public void setMidTime(Calendar Mid)
    {
        MidTime = Mid;
    }
    public Calendar getMidTime()
    {
        return MidTime;
    }

    public void setEndTime(Calendar End)
    {
        EndTime = End;
    }
    public Calendar getEndTime()
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