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

import static com.giemper.ecocarwash.CarMethods.getFullDate;

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
            countdown.secondLayout2.removeView(countdown.nextButton);
            countdown.secondLayout2.addView(countdown.stopButton);

            clock.setDryer(0, DryerName);
            clock.setMidTime(countdown.MidTime.getTimeInMillis());


            ecoDatabase.child("Clocks/Active").child(getFullDate()).child(clock.getTransactionID()).setValue(clock);

            dialog.dismiss();
        });
    }
}
