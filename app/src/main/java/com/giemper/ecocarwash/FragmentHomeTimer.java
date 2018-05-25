package com.giemper.ecocarwash;

import android.os.Bundle;
import android.os.SystemClock;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_timer, container, false);
        layout = rootView.findViewById(R.id.Card_Layout);
        ecoDatabase = FirebaseDatabase.getInstance().getReference();

        setFloatingListener();

        setDatabaseSingleListener();
        setDatabaseListener();


        return rootView;
    }

    private void setDatabaseListener()
    {
        Query queryChronometers = ecoDatabase.child("Clocks/Active").child(getFullDate()).orderByKey();
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

                        cd.textName.setText(clock.getDryerName());
                        cd.textName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));
                        cd.secondLayout2.removeView(cd.nextButton);
                        cd.secondLayout2.addView(cd.stopButton);

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
                    boolean snapDate = (snapKey).equals(getFullDate());

                    if(!snapDate)
                    {
                        String[] dates = snapKey.split("-");

                        ecoDatabase.child("Clocks/Archive").child(dates[0]).child(dates[1]).child(dates[2]).setValue(snap.getValue());
                        ecoDatabase.child("Clocks/Active").child(snapKey).removeValue();
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
            Snackbar.make(view, cd.clock.Car.getLicense() + " lavado por " + cd.clock.getDryerName() + " fue terminado.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            cd.clock.setEndTime(cd.EndTime.getTimeInMillis());

            ecoDatabase.child("Clocks/Active").child(getFullDate()).child(cd.clock.getTransactionID()).removeValue();
            ecoDatabase.child("Clocks/Archive").child(getYear() + "/" + getMonth() + "/" +  getDay()).child(cd.clock.getTransactionID()).setValue(cd.clock);
        });
    }
}
