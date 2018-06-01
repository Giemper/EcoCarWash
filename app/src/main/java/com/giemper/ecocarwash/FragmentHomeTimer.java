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
import android.widget.RelativeLayout;

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

        setLayoutInfoListener();
        setFloatingListener();
        setDatabaseSingleListener();
        setDatabaseListener();

        return rootView;
    }

    private void setDatabaseListener()
    {
        Query queryChronometers = ecoDatabase.child("Clocks/Active").child(getTodayInMillisString());
        queryChronometers.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Clocks clock = dataSnapshot.getValue(Clocks.class);
                Countdown cd = new Countdown(getActivity(), clock);
                cd.setTag(clock.getTransactionID());

                if(clock.getDryerID() != null)
                {
                    cd.textName.setText(clock.getDryerFirstName() + " " + clock.getDryerLastName().charAt(0) + ".");
                }
                if(clock.getMidTime() > 0)
                {
                    cd.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - clock.getMidTime()));
                    cd.chrono2.start();

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

                    cd.textName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));
                    cd.textName.setText(clock.getDryerFirstName() + " " + clock.getDryerLastName().charAt(0) + ".");
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
        Query querySingle = ecoDatabase.child("Clocks/Active");
        querySingle.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override public void onCancelled(DatabaseError databaseError){}
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                long l = dataSnapshot.getChildrenCount();

                for (DataSnapshot snap : dataSnapshot.getChildren())
                {
                    String snapKey = snap.getKey();
                    boolean snapDate = (snapKey).equals(getTodayInMillisString());

                    if (!snapDate)
                    {
                        for (DataSnapshot snap2 : snap.getChildren())
                        {
                            long time = Calendar.getInstance().getTimeInMillis();
                            Clocks clock = snap2.getValue(Clocks.class);
                            if (clock.getMidTime() == 0)
                                clock.setMidTime(time);
                            clock.setEndTime(time);

                            if(clock.getDryerFirstName().length() > 0)
                            {
                                clock.setDryerFirstName("Sin Asignación");
                                clock.setDryerLastName(" ");
                            }

                            ecoDatabase.child("Clocks/Active").child(snapKey).child(snap2.getKey()).removeValue();
                            ecoDatabase.child("Clocks/Archive").child(snapKey).child(snap2.getKey()).setValue(clock);

                        }
                    }
                }
            }
        });
    }

    private void setFloatingListener()
    {
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener((View view) ->
        {
            final DialogCreateCar dcc = new DialogCreateCar();
            dcc.AddDialog(getActivity(), view);
            dcc.setSpinners(ecoDatabase);
            dcc.setDialogCreateCarListener(ecoDatabase);
        });
    }

    private void setCountdownButtonListener(Countdown cd)
    {
        cd.nextButton.setOnClickListener((View view) ->
        {
            cd.MidTime = Calendar.getInstance();
            final DialogCreateCarDryer dialogCarDryer = new DialogCreateCarDryer(ecoDatabase);

            if(cd.clock.getDryerID() == null)
                dialogCarDryer.getNextInQueue();
            else
                dialogCarDryer.getDryerInQueue(cd.clock);

            dialogCarDryer.AddDialog(getActivity());
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
            hash.put("Clocks/Archive/" + queryClock, cd.clock);
            hash.put("Dryers/" + queryDryer + "workStatus", "Available");
            hash.put("Dryers/" + queryDryer + "queue", getTodaySmallInMillis());

            ecoDatabase.updateChildren(hash);
            ecoDatabase.child("Clocks/Active").child(getTodayInMillisString()).child(cd.clock.getTransactionID()).removeValue();
        });
    }

    private void setLayoutInfoListener()
    {
        RelativeLayout info = rootView.findViewById(R.id.Fragment_Home_Timer_InfoLayout);
        layout.addOnLayoutChangeListener((View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) ->
        {
            if(((LinearLayout)view).getChildCount() > 0)
                info.setVisibility(View.GONE);
            else
                info.setVisibility(View.VISIBLE);
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