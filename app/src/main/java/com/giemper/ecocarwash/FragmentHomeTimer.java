package com.giemper.ecocarwash;

import android.graphics.PorterDuff;
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
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.giemper.ecocarwash.CarMethods.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomeTimer extends Fragment
{
    private DatabaseReference ecoDatabase;
    private View rootView;
    List<Countdown> CountdownList = new ArrayList<Countdown>();
    List<Clocks> ClockList = new ArrayList<Clocks>();
    LinearLayout layout;
    int ClockCount;

    public FragmentHomeTimer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_timer, container, false);
        layout = rootView.findViewById(R.id.Card_Layout);
        ecoDatabase = FirebaseDatabase.getInstance().getReference();

        setFloatingListener();
        setDatabaseListener();
        setDatabaseSingleListener();

        return rootView;
    }

    private void setDatabaseListener()
    {
        ecoDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                ClockCount = dataSnapshot.child("Clocks").child("ClockCount").getValue(int.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void setDatabaseSingleListener()
    {
        ecoDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
//

                for (DataSnapshot postSnapshot: dataSnapshot.child("Clocks").child("Active").getChildren())
                {


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

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

            setDialogCreateCarListener(dcc);
        });
    }

    private void setDialogCreateCarListener(DialogCreateCar dcc)
    {
        final DialogCreateCar dialogCreateCar = dcc;
        Button add = dialogCreateCar.dialog.findViewById(R.id.Dialog_CreateCar_Button_Add);
        add.setOnClickListener((View view) ->
        {
            Clocks clock = new Clocks(ClockCount);
            clock.setCarValues(dialogCreateCar.dialog);
            clock.setStartTime(dialogCreateCar.StartTime);
            ClockList.add(clock);

            CountdownList.add(new Countdown(getActivity(), clock.getStartTime(), clock.Car, clock.getTransactionID()));
            int last = CountdownList.size() - 1;
            layout.addView(CountdownList.get(last), layout.getChildCount() - 1);
            dialogCreateCar.dialog.dismiss();

            setCountdownButtonListener(CountdownList.get(last));

            ecoDatabase.child("Clocks").child("Active").child(getFullDate()).child(clock.getTransactionID_String()).setValue(clock);
            ecoDatabase.child("Clocks").child("ClockCount").setValue(ClockCount + 1);
        });
    }

    private void setDialogCreateDryerListener(DialogCreateDryer dcd, int _index)
    {
        final int index = _index;
        final DialogCreateDryer dialogDryer = dcd;
        Button add = dialogDryer.dialog.findViewById(R.id.Dialog_CreateDryer_Button_Add);

        add.setOnClickListener((View view) ->
        {


            Countdown c = CountdownList.get(index);
            c.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - c.MidTime.getTimeInMillis()));
            c.chrono2.start();

            int ClockIndex = findClockIndex(c.ClockID);
            Clocks clock = ClockList.get(ClockIndex);
            clock.setDryer(0, dialogDryer.DryerName);
            clock.setMidTime(c.MidTime);

            c.textName.setText(ClockList.get(ClockIndex).getDryerName());
            c.textName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));

            CountdownList.get(index).secondLayout2.removeView(c.nextButton);
            CountdownList.get(index).secondLayout2.addView(c.stopButton);
            dialogDryer.dialog.dismiss();

            ecoDatabase.child("Clocks").child("Active").child(getFullDate()).child(clock.getTransactionID_String()).setValue(clock);
        });
    }

    private void setCountdownButtonListener(Countdown cd)
    {
        cd.nextButton.setOnClickListener((View view) ->
        {
            final int index = findCountdownIndex(view.getId());

            CountdownList.get(index).MidTime = Calendar.getInstance();
            final DialogCreateDryer dialogDryer = new DialogCreateDryer();
            dialogDryer.AddDialog(getActivity(), view);

            setDialogCreateDryerListener(dialogDryer, index);
        });

        cd.stopButton.setOnClickListener((View view) ->
        {
            int index = findCountdownIndex(view.getId());
            Countdown c = CountdownList.get(index);
            c.EndTime = Calendar.getInstance();
            Snackbar.make(view, "Countdown at " + index + " was removed.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            layout.removeView(CountdownList.get(index));
            CountdownList.remove(index);

            int ClockIndex = findClockIndex(c.ClockID);
            Clocks clock = ClockList.get(ClockIndex);
            clock.setEndTime(c.EndTime);
            clock.setActive(false);

            ecoDatabase.child("Clocks").child("Active").child(getFullDate()).child(clock.getTransactionID_String()).removeValue();
            ecoDatabase.child("Clocks").child("Archive").child(getYear() + "/" + getMonth() + "/" +  getDay()).child(clock.getTransactionID_String()).setValue(clock);
        });
    }

    public int findCountdownIndex(int id)
    {
        for(int i=0; i < CountdownList.size(); i++)
        {
            if (CountdownList.get(i).stopButton.getId() == id)
                return i;
            else if (CountdownList.get(i).nextButton.getId() == id)
                return i;
        }
        return -1;
    }

    public int findClockIndex(int id)
    {
        for(int i=0; i < ClockList.size(); i++)
        {
            if(ClockList.get(i).getTransactionID() == id)
                return i;
        }

        return -1;
    }
}
