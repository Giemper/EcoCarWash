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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.giemper.ecocarwash.CarMethods.getTodayInMillisString;

public class DialogCreateCarDryer
{
    private DatabaseReference ecoDatabase;
    private Dryer nextQueue;
    public Dialog dialog;

    public String FirstName;
    public String LastName;
    public Calendar StartTime;

    public DialogCreateCarDryer(DatabaseReference eco)
    {
        ecoDatabase = eco;
    }

    public void AddDialog(Activity activity, View view)
    {
        getNextInQueue();

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_createcardryer);

    }

    private void getNextInQueue()
    {
        Query queryQueue = ecoDatabase.child("Dryers").orderByChild("workStatus").equalTo("Available");

        queryQueue.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                nextQueue = null;
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Dryer dryer = snap.getValue(Dryer.class);
                    if(dryer.getQueue() != 0)
                    {
                        if (nextQueue == null || (nextQueue.getQueue() > dryer.getQueue()))
                            nextQueue = dryer;
                    }
                }
                if(nextQueue != null)
                {
                    goodAlert();
                }
                else
                    badAlert();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void goodAlert()
    {
        StartTime = Calendar.getInstance();
        Chronometer chrono = dialog.findViewById(R.id.Dialog_CreateCarDryer_Chronometer);
        chrono.start();

        TextView TextName = dialog.findViewById(R.id.Dryer_Name);
        TextName.setText(nextQueue.getFirstName() + " " + nextQueue.getLastNameFather());

        dialog.show();
    }

    private void badAlert()
    {
        TextView TextName = dialog.findViewById(R.id.Dryer_Name);
        TextName.setText("No hay secadores disponibles.");

        Button add = dialog.findViewById(R.id.Dialog_CreateCarDryer_Button_Add);
        add.setEnabled(false);

        dialog.show();
    }

    public void setDialogCreateCarDryerListener(Countdown countdown)
    {
        Button add = dialog.findViewById(R.id.Dialog_CreateCarDryer_Button_Add);
        add.setOnClickListener((View view) ->
        {
            countdown.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - countdown.MidTime.getTimeInMillis()));
            countdown.chrono2.start();

            Clocks clock = countdown.clock;
            clock.setDryer(nextQueue.getDryerID(), nextQueue.getFirstName(), nextQueue.getLastNameFather());
            clock.setMidTime(countdown.MidTime.getTimeInMillis());

            countdown.textName.setText(clock.getDryerFirstName() + " " + clock.getDryerFirstName().charAt(0) + ".");
            countdown.textName.setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.colorAccent3));

            String queryClock = getTodayInMillisString() + "/" + clock.getTransactionID() + "/";
            String queryDryer = clock.getDryerID() + "/";
            Map hash = new HashMap<>();
            hash.put("Clocks/" + queryClock + "dryerID", clock.getDryerID());
            hash.put("Clocks/" + queryClock + "dryerFirstName", clock.getDryerFirstName());
            hash.put("Clocks/" + queryClock + "dryerLastName", clock.getDryerLastName());
            hash.put("Clocks/" + queryClock + "midTime", clock.getMidTime());
            hash.put("Dryers/" + queryDryer + "workStatus", "Busy");
            hash.put("Dryers/" + queryDryer + "queue", 0);

//            ecoDatabase.child("Clocks").child(getTodayInMillisString()).child(clock.getTransactionID()).updateChildren(hash);
            ecoDatabase.updateChildren(hash);


            dialog.dismiss();
        });

        Button quit = dialog.findViewById(R.id.Dialog_CreateCarDryer_Button_Quit);
        quit.setOnClickListener((View v) ->
        {
            dialog.dismiss();
        });
    }
}