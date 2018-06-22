package com.giemper.ecocarwash;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import static com.giemper.ecocarwash.EcoMethods.getTodayInMillisString;
import static com.giemper.ecocarwash.EcoMethods.getTodaySmallInMillis;
import static com.giemper.ecocarwash.EcoMethods.getTodaySmallInString;

public class CardCheckbox extends LinearLayout
{
    public CheckBox Box;
    public ImageButton InfoButton;
    private String Tag;

    public CardCheckbox(Context context, Dryer dryer)
    {
        super(context);

        LinearLayout ll = (LinearLayout) inflate(context, R.layout.card_checkbox, this);
        Box = ll.findViewById(R.id.Dryer_Checkbox);
        Box.setText(dryer.getFirstName() + " " + dryer.getLastNameFather());

        InfoButton = ll.findViewById(R.id.Dryer_InfoButton);

    }

    public void setCheckBoxListener(Dryer dryer, DatabaseReference ecoDatabase, Activity activity)
    {
        Box.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) ->
        {
            Map hash = new HashMap<>();
            DryerAttendance atten = new DryerAttendance();
            atten.setDryerID(dryer.getDryerID());
            atten.setDryerName(dryer.fullName());
            atten.setDate(Calendar.getInstance().getTimeInMillis());

            if(isChecked)
            {
                atten.setAction("Enter");

                hash.put("List/" + dryer.getDryerID() + "/workStatus", "available");
                hash.put("List/" + dryer.getDryerID() + "/queue", getTodaySmallInMillis());
//                hash.put("List/" + dryer.getDryerID() + "/attendance", atten);

                hash.put("Attendance/" + getTodayInMillisString() + "/" + getTodaySmallInString(), atten);
            }
            else
            {
                atten.setAction("Exit");

                hash.put("List/" + dryer.getDryerID() + "/workStatus", "none");
                hash.put("List/" + dryer.getDryerID() + "/queue", 0);
                hash.put("Attendance/" + getTodayInMillisString() + "/" + getTodaySmallInString(), atten);
            }

            ecoDatabase.child("Dryers").updateChildren(hash);
        });

        InfoButton.setOnClickListener((View view) ->
        {
            final DialogInfoDryer did = new DialogInfoDryer();
            did.AddDialog(activity, view);
            did.setValues(dryer.fullName(), dryer.getStartTime(), dryer.getCarWashed());
            did.setListeners(ecoDatabase, dryer);
        });
    }

    public void setTag(String tag)
    {
        Tag = tag;
    }
    public String getTag()
    {
        return Tag;
    }
}