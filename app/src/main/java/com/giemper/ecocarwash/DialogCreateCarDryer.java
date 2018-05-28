package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.Dialog;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.giemper.ecocarwash.CarMethods.getFullDate;
import static com.giemper.ecocarwash.CarMethods.getTodayInMillis;
import static com.giemper.ecocarwash.CarMethods.getTodayInMillisString;

public class DialogCreateCarDryer
{
    public Dialog dialog;
    public String DryerName;
    public Calendar StartTime;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_createcardryer);

        StartTime = Calendar.getInstance();

        TextView TextName = dialog.findViewById(R.id.Dryer_Name);
        DryerName = TextName.getText().toString();

        Chronometer chrono = dialog.findViewById(R.id.Dialog_CreateCarDryer_Chronometer);
        chrono.start();

        Button quit = dialog.findViewById(R.id.Dialog_CreateCarDryer_Button_Quit);
        quit.setOnClickListener((View v) ->
        {
            dialog.dismiss();
        });

        dialog.show();
    }

    public void setDialogCreateDryerListener(Countdown countdown, DatabaseReference ecoDatabase)
    {
        Button add = dialog.findViewById(R.id.Dialog_CreateCarDryer_Button_Add);
        add.setOnClickListener((View view) ->
        {
            Clocks clock = countdown.clock;
            countdown.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - countdown.MidTime.getTimeInMillis()));
            countdown.chrono2.start();
            countdown.textName.setText(clock.getDryerName());
            countdown.textName.setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.colorAccent3));

            clock.setDryer(0, DryerName);
            clock.setMidTime(countdown.MidTime.getTimeInMillis());

            Map hash = new HashMap<>();
            hash.put("dryerID", clock.getDryerID());
            hash.put("dryerName", clock.getDryerName());
            hash.put("midTime", clock.getMidTime());

            ecoDatabase.child("Clocks/Active").child(getTodayInMillisString()).child(clock.getTransactionID()).updateChildren(hash);

            dialog.dismiss();
        });
    }
}