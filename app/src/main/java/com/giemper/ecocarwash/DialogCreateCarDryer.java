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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import static com.giemper.ecocarwash.CarMethods.getTodayInMillisString;

public class DialogCreateCarDryer
{
    public Dialog dialog;
    public String FirstName;
    public String LastName;
    public Calendar StartTime;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_createcardryer);

        StartTime = Calendar.getInstance();

        TextView TextName = dialog.findViewById(R.id.Dryer_Name);
        FirstName = TextName.getText().toString();
        LastName = " ";

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
            countdown.textName.setText(clock.getDryerShortName());
            countdown.textName.setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.colorAccent3));

            clock.setDryer(0, FirstName, LastName);
            clock.setMidTime(countdown.MidTime.getTimeInMillis());

            Map hash = new HashMap<>();
            hash.put("dryerID", clock.getDryerID());
            hash.put("dryerFirstName", clock.getDryerFirstName());
            hash.put("dryerLastName", clock.getDryerLastName());
            hash.put("midTime", clock.getMidTime());

            ecoDatabase.child("Clocks/Active").child(getTodayInMillisString()).child(clock.getTransactionID()).updateChildren(hash);

            dialog.dismiss();
        });
    }
}