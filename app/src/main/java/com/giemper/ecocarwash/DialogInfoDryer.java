package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import static com.giemper.ecocarwash.CarMethods.getMillisToStringSmall;

public class DialogInfoDryer
{
    public Dialog dialog;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_infodryer);
        dialog.show();
    }

    public void setValues(String Name, long Date, int CarWashed)
    {
        TextView textName = dialog.findViewById(R.id.Dialog_InfoDryer_Name);
        textName.setText(Name);

        TextView textCount = dialog.findViewById(R.id.Dialog_InfoDryer_WasherCount);
        textCount.setText(Integer.toString(CarWashed));
    }

    public void setListeners(DatabaseReference ecoDatabase, Dryer dryer)
    {
        Button accept = dialog.findViewById(R.id.Dialog_InfoDryer_Accept);
        accept.setOnClickListener((View v) ->
        {
            dialog.dismiss();
        });

        Button delete = dialog.findViewById(R.id.Dialog_InfoDryer_Delete);
        delete.setOnClickListener((View v) ->
        {
            dryer.setEndTime(Calendar.getInstance().getTimeInMillis());
            Map hash = new HashMap<>();
            hash.put("active", dryer.getActive());
            hash.put("workStatus", dryer.getWorkStatus());
            hash.put("endTime", dryer.getEndTime());
            hash.put("queue", 0);

            ecoDatabase.child("Dryers/List").child(dryer.getDryerID()).updateChildren(hash);
            dialog.dismiss();
        });
    }
}