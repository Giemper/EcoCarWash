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

public class CardCheckbox extends LinearLayout
{
    public CheckBox Box;
    public SquareButton InfoButton;

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
            if(isChecked)
            {
                Map status = new HashMap();
                status.put("Status", "Available");
                status.put("Queue", (Calendar.getInstance().getTimeInMillis() - getTodayInMillis()));

                ecoDatabase.child("Dryers/List").child(dryer.getDryerID()).updateChildren(status);

            }
            else
            {
                ecoDatabase.child("Dryers/List").child(dryer.getDryerID()).child("Status").setValue("None");
            }
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
                ecoDatabase.child("Dryers/List").child(dryer.getDryerID()).child("active").setValue(false);
                did.dialog.dismiss();
            });
        });
    }
}
