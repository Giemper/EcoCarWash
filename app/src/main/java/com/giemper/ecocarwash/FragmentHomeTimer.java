package com.giemper.ecocarwash;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.giemper.ecocarwash.CarMethods.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomeTimer extends Fragment
{
    private DatabaseReference ecoDatabase;
    private View rootView;
    LinearLayout layout;

    public FragmentHomeTimer() {
        // Required empty public constructor
    }

    public void setFirebase(DatabaseReference db)
    {
        ecoDatabase = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_timer, container, false);
        layout = rootView.findViewById(R.id.Card_Layout);

        setFloatingListener();
        setDatabaseSingleListener();
        setDatabaseListener();

        return rootView;
    }

    private void setDatabaseListener()
    {
        Query queryChronometers = ecoDatabase.child("Clocks").child(getTodayInMillisString()).orderByChild("active").equalTo(true);
        queryChronometers.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Clocks clock = dataSnapshot.getValue(Clocks.class);
                Countdown cd = new Countdown(getActivity(), clock);
                cd.setTag(clock.getTransactionID());

                if(clock.getMidTime() > 0)
                {
                    cd.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - clock.getMidTime()));
                    cd.chrono2.start();

                    cd.textName.setText("");
                    cd.textName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));
                    cd.nextButton.setVisibility(View.GONE);
                    cd.stopButton.setVisibility(View.VISIBLE);
                }

                setCountdownButtonListener(cd);

                layout.addView(cd);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                Clocks clock = dataSnapshot.getValue(Clocks.class);
                Countdown cd = findCountdown(clock.getTransactionID());

                if(clock.getMidTime() > 0)
                {
                    cd.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - clock.getMidTime()));
                    cd.chrono2.start();

                    cd.textName.setText("");
                    cd.textName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));
                    cd.nextButton.setVisibility(View.GONE);
                    cd.stopButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                Clocks clock = dataSnapshot.getValue(Clocks.class);
                Countdown cd = findCountdown(clock.getTransactionID());
                layout.removeView(cd);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void setDatabaseSingleListener()
    {
        Query querySingle = ecoDatabase.child("Clocks");
        querySingle.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot snap: dataSnapshot.getChildren())
                {
                    String snapKey = snap.getKey();
                    boolean snapDate = (snapKey).equals(getTodayInMillisString());

                    if(!snapDate)
                    {
                        for(DataSnapshot snap2: snap.getChildren())
                        {
                            ecoDatabase.child("Clocks").child(snapKey).child(snap2.getKey()).child("active").setValue(false);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void setFloatingListener()
    {
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener((View view) ->
        {
            final DialogCreateCar dcc = new DialogCreateCar();
            dcc.AddDialog(getActivity(), view);
            dcc.setDialogCreateCarListener(ecoDatabase);
        });
    }

    private void setCountdownButtonListener(Countdown cd)
    {
        cd.nextButton.setOnClickListener((View view) ->
        {
            cd.MidTime = Calendar.getInstance();
            final DialogCreateCarDryer dialogCarDryer = new DialogCreateCarDryer(ecoDatabase);
            dialogCarDryer.AddDialog(getActivity(), view);
            dialogCarDryer.setDialogCreateCarDryerListener(cd);
        });

        cd.stopButton.setOnClickListener((View view) ->
        {
            Snackbar.make(view, cd.clock.Car.getLicense() + " lavado por " + cd.clock.getDryerFirstName() + " fue terminado.",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();

            cd.EndTime = Calendar.getInstance();
            cd.clock.setEndTime(cd.EndTime.getTimeInMillis());

            String queryClock = getTodayInMillisString() + "/" + cd.clock.getTransactionID() + "/";
            String queryDryer = cd.clock.getDryerID() + "/";
            Map hash = new HashMap<>();
            hash.put("Clocks/" + queryClock + "endTime", cd.clock.getEndTime());
            hash.put("Clocks/" + queryClock + "active", cd.clock.getActive());
            hash.put("Dryers/" + queryDryer + "workStatus", "Available");

//            ecoDatabase.child("Clocks").child(getTodayInMillisString()).child(cd.clock.getTransactionID()).updateChildren(hash);
            ecoDatabase.updateChildren(hash);
        });
    }

    private Countdown findCountdown(String tag)
    {
        for(int i = 0; i < layout.getChildCount(); i++)
        {
            Countdown cd = (Countdown) layout.getChildAt(i);
            if(tag.equals(cd.getTag()))
                return cd;
        }
        return null;
    }
}