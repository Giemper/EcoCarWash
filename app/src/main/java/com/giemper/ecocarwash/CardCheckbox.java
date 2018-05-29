package com.giemper.ecocarwash;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.google.firebase.database.DatabaseReference;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.giemper.ecocarwash.CarMethods.getTodayInMillis;
import static com.giemper.ecocarwash.CarMethods.getTodaySmallInMillis;

public class CardCheckbox extends LinearLayout
{
    public CheckBox Box;
    public SquareButton InfoButton;
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
            if(isChecked)
            {
                hash.put("workStatus", "Available");
                hash.put("queue", getTodaySmallInMillis());


            }
            else
            {
                hash.put("workStatus", "None");
                hash.put("queue", 0);
            }

            ecoDatabase.child("Dryers").child(dryer.getDryerID()).updateChildren(hash);
        });

        InfoButton.setOnClickListener((View view) ->
        {
            final DialogInfoDryer did = new DialogInfoDryer();
            did.AddDialog(activity, view);

            did.Name.setText(dryer.getFirstName() + " " + dryer.getLastNameFather() + " " + dryer.getLastNameMother());
            did.Date.setText(dryer.getStartTime());
//            did.Count.setText("" + dryer.CarWashed);

            did.Delete.setOnClickListener((View v) ->
            {
                dryer.setEndTime(Calendar.getInstance().getTimeInMillis());
                Map hash = new HashMap<>();
                hash.put("active", dryer.getActive());
                hash.put("workStatus", dryer.getWorkStatus());
                hash.put("endTime", dryer.getEndTime());

                ecoDatabase.child("Dryers").child(dryer.getDryerID()).updateChildren(hash);

                did.dialog.dismiss();
            });
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