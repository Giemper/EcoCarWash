package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by guillermo.magdaleno on 4/20/2018.
 */

public class DialogCreateDryer
{
    public Dialog dialog;
    public String DryerName;
    public Calendar StartTime;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_createdryer);

        StartTime = Calendar.getInstance();

        TextView TextName = dialog.findViewById(R.id.Dryer_Name);
        DryerName = TextName.getText().toString();

        Chronometer chrono = dialog.findViewById(R.id.Dialog_CreateDryer_Chronometer);
        chrono.start();

        Button quit = dialog.findViewById(R.id.Dialog_CreateDryer_Button_Quit);
        quit.setOnClickListener((View v) ->
        {
            dialog.dismiss();
        });

        dialog.show();
    }
}
