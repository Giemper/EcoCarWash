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
        Query queryChronometers = ecoDatabase.child("Clocks/Active").child(getTodayInMillisString()).orderByChild("active").equalTo(true);
        queryChronometers.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                layout.removeAllViews();
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Clocks clock = snap.getValue(Clocks.class);
                    Countdown cd = new Countdown(getActivity(), clock);

                    if(clock.getMidTime() > 0)
                    {
                        cd.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - clock.getMidTime()));
                        cd.chrono2.start();

                        cd.textName.setText(clock.getDryerShortName());
                        cd.textName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));
                        cd.nextButton.setVisibility(View.GONE);
                        cd.stopButton.setVisibility(View.VISIBLE);
                    }

                    setCountdownButtonListener(cd);

                    layout.addView(cd);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    private void setDatabaseSingleListener()
    {
        Query temp = ecoDatabase.child("Clocks").child("Active");
        temp.addListenerForSingleValueEvent(new ValueEventListener()
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
                            ecoDatabase.child("Clocks/Active").child(snapKey).child(snap2.getKey()).child("active").setValue(false);
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
            final DialogCreateCarDryer dialogCarDryer = new DialogCreateCarDryer();
            dialogCarDryer.AddDialog(getActivity(), view);
            dialogCarDryer.setDialogCreateDryerListener(cd, ecoDatabase);
        });

        cd.stopButton.setOnClickListener((View view) ->
        {
            cd.EndTime = Calendar.getInstance();
            Snackbar.make(view, cd.clock.Car.getLicense() + " lavado por " + cd.clock.getDryerShortName() + " fue terminado.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            cd.clock.setEndTime(cd.EndTime.getTimeInMillis());

//            ecoDatabase.child("Clocks/Active").child(getTodayInMillisString()).child(cd.clock.getTransactionID()).removeValue();

            Map hash = new HashMap<>();
            hash.put("endTime", cd.clock.getEndTime());
            hash.put("active", cd.clock.getActive());

            ecoDatabase.child("Clocks/Active").child(getTodayInMillisString()).child(cd.clock.getTransactionID()).updateChildren(hash);
        });
    }
}