package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.Dialog;
import android.os.SystemClock;
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
    public Dialog dialog;

    private DatabaseReference ecoDatabase;
    private Dryer nextDryer;
    private Calendar StartTime;

    public DialogCreateCarDryer(DatabaseReference eco)
    {
        ecoDatabase = eco;
    }

    public void AddDialog(Activity activity)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_createcardryer);

    }

    public void getNextInQueue()
    {
        Query queryQueue = ecoDatabase.child("Dryers").orderByChild("workStatus").equalTo("available");
        queryQueue.keepSynced(true);
        queryQueue.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override public void onCancelled(DatabaseError databaseError) {}
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                nextDryer = null;
                long i = dataSnapshot.getChildrenCount();
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Dryer dryer = snap.getValue(Dryer.class);
                    if(dryer.getQueue() != 0)
                    {
                        if (nextDryer == null || (nextDryer.getQueue() > dryer.getQueue()))
                            nextDryer = dryer;
                    }
                }
                if(nextDryer != null)
                {
                    goodAlert();
                }
                else
                {
                    badAlert();
                }
            }
        });

    }

    public void getDryerInQueue(Clocks clock)
    {
        Query queryQueue = ecoDatabase.child("Dryers").orderByKey().equalTo(clock.getDryerID()).limitToFirst(1);
        queryQueue.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override public void onCancelled(DatabaseError databaseError){}
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                nextDryer = dataSnapshot.child(clock.getDryerID()).getValue(Dryer.class);
                goodAlert();
            }
        });
    }

    private void goodAlert()
    {
        StartTime = Calendar.getInstance();
        Chronometer chrono = dialog.findViewById(R.id.Dialog_CreateCarDryer_Chronometer);
        chrono.start();

        TextView TextName = dialog.findViewById(R.id.Dryer_Name);
        TextName.setText(nextDryer.fullName());

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

    public void setDialogCreateCarDryerListener(CardChronometer countdown)
    {
        Button add = dialog.findViewById(R.id.Dialog_CreateCarDryer_Button_Add);
        add.setOnClickListener((View view) ->
        {
            countdown.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - countdown.MidTime.getTimeInMillis()));
            countdown.chrono2.start();

            nextDryer.addCarWashed();
            nextDryer.setWorkStatus("busy");
            Clocks clock = countdown.clock;
            clock.setMidTime(countdown.MidTime.getTimeInMillis());
            if(clock.getDryerID() == null)
                clock.setDryer(nextDryer.getDryerID(), nextDryer.getFirstName(), nextDryer.getLastNameFather() + " " + nextDryer.getLastNameMother());

            String queryClock = getTodayInMillisString() + "/" + clock.getTransactionID() + "/";
            String queryDryer = clock.getDryerID() + "/";
            Map hash = new HashMap<>();
            hash.put("Clocks/Active/" + queryClock + "dryerID", clock.getDryerID());
            hash.put("Clocks/Active/" + queryClock + "dryerFirstName", clock.getDryerFirstName());
            hash.put("Clocks/Active/" + queryClock + "dryerLastName", clock.getDryerLastName());
            hash.put("Clocks/Active/" + queryClock + "midTime", clock.getMidTime());
            hash.put("Dryers/" + queryDryer + "workStatus", nextDryer.getWorkStatus());
            hash.put("Dryers/" + queryDryer + "queue", 0);
            hash.put("Dryers/" + queryDryer + "carWashed", nextDryer.getCarWashed());

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