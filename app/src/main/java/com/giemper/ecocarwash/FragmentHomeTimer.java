package com.giemper.ecocarwash;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import static com.giemper.ecocarwash.EcoMethods.*;

public class FragmentHomeTimer extends Fragment
{
    private DatabaseReference ecoDatabase;
    private FirebaseUser ecoUser;
    private String ecoUserType;
    private boolean CountdownAccess = false;
    private Context mContext;
    private View rootView;
    private LinearLayout layout;

    private View.OnClickListener nextListener;
    private View.OnClickListener fabListener;

    public FragmentHomeTimer() {}

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mContext = context;
    }

    public void setFirebase(DatabaseReference db, FirebaseUser user, String userType)
    {
        ecoDatabase = db;
        ecoUser = user;
        ecoUserType = userType;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_home_timer, container, false);
        layout = rootView.findViewById(R.id.Card_Layout);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setEnabled(false);

        setLayoutInfoListener();
        setDatabaseSingleListener();
        setDatabaseListener();
//        setFloatingListener();

        return rootView;
    }

    private void setDatabaseListener()
    {
        Query queryChronometers = ecoDatabase.child("Clocks/Active").child(getTodayInMillisString());
        queryChronometers.addChildEventListener(new ChildEventListener()
        {
            @Override public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s){}
            @Override public void onCancelled(@NonNull DatabaseError databaseError){}
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s)
            {
                Clocks clock = dataSnapshot.getValue(Clocks.class);
                CardChronometer cd = new CardChronometer(mContext, clock);
                cd.setTag(clock.getTransactionID());

                if(clock.getDryerID() != null)
                {
                    cd.textName.setText(clock.getDryerFirstName() + " " + clock.getDryerLastName().charAt(0) + ".");
                }
                if(clock.getMidTime() > 0)
                {
                    cd.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - clock.getMidTime()));
                    cd.chrono2.start();

                    cd.textName.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent3));
                    cd.nextButton.setVisibility(View.GONE);
                    cd.stopButton.setVisibility(View.VISIBLE);
                }

                setClickListeners(cd);
                layout.addView(cd);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s)
            {
                Clocks clock = dataSnapshot.getValue(Clocks.class);
                CardChronometer cd = findCountdown(clock.getTransactionID());

                if(clock.getMidTime() > 0)
                {
                    cd.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - clock.getMidTime()));
                    cd.chrono2.start();

                    cd.textName.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent3));
                    cd.textName.setText(clock.getDryerFirstName() + " " + clock.getDryerLastName().charAt(0) + ".");
                    cd.nextButton.setVisibility(View.GONE);
                    cd.stopButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
            {
                Clocks clock = dataSnapshot.getValue(Clocks.class);
                CardChronometer cd = findCountdown(clock.getTransactionID());
                layout.removeView(cd);
            }
        });
    }

    private void setDatabaseSingleListener()
    {
        Query querySingle = ecoDatabase.child("Clocks/Active");
        querySingle.keepSynced(true);
        querySingle.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override public void onCancelled(DatabaseError databaseError){}
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getChildrenCount() > 0)
                {
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

                                    if (clock.getDryerFirstName() == null)
                                    {
                                        clock.setDryerFirstName("Sin Asignación");
                                        clock.setDryerLastName(" ");
                                        clock.setDryerID("0");
                                    }

                                    ecoDatabase.child("Clocks/Active").child(snapKey).child(snap2.getKey()).removeValue();
                                    ecoDatabase.child("Clocks/Archive").child(snapKey).child(snap2.getKey()).setValue(clock);
                                }
                            }
                    }
                }
            }
        });
    }

    public void setFloatingListener()
    {
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setEnabled(true);
        fabListener = ((View view) ->
        {
            fab.setOnClickListener(null);
            final DialogCreateCar dcc = new DialogCreateCar();
            dcc.AddDialog(getActivity(), view);
            dcc.setSpinners(ecoDatabase);
            dcc.setDialogCreateCarListener(ecoDatabase);
            dcc.dialog.setOnDismissListener((DialogInterface dialogInterface) ->
            {
                fab.setOnClickListener(fabListener);
            });
            dcc.dialog.setOnCancelListener((DialogInterface dialogInterface) ->
            {
                fab.setOnClickListener(fabListener);
            });
        });
        fab.setOnClickListener(fabListener);
    }

    private void setClickListeners(CardChronometer cd)
    {
        if(CountdownAccess)
        {
            nextListener = ((View view) ->
            {
                cd.nextButton.setOnClickListener(null);
                cd.MidTime = Calendar.getInstance();
                final DialogCreateCarDryer dialogCarDryer = new DialogCreateCarDryer(ecoDatabase);

                if (cd.clock.getDryerID() == null)
                    dialogCarDryer.getNextInQueue();
                else
                    dialogCarDryer.getDryerInQueue(cd.clock);

                dialogCarDryer.AddDialog(getActivity());
                dialogCarDryer.setDialogCreateCarDryerListener(cd);
                dialogCarDryer.resumeClickListener(cd.nextButton, nextListener);
            });

            View.OnClickListener stopListener = ((View view) ->
            {
                cd.stopButton.setOnClickListener(null);
                cd.EndTime = Calendar.getInstance();
                cd.clock.setEndTime(cd.EndTime.getTimeInMillis());

                String queryClock = getTodayInMillisString() + "/" + cd.clock.getTransactionID() + "/";
                String queryDryer = cd.clock.getDryerID() + "/";

                Map hash = new HashMap<>();
                hash.put("Clocks/Archive/" + queryClock, cd.clock);
                hash.put("Dryers/List/" + queryDryer + "workStatus", "available");
                hash.put("Dryers/List/" + queryDryer + "queue", getTodaySmallInMillis());

                ecoDatabase.updateChildren(hash);
                ecoDatabase.child("Clocks/Active").child(getTodayInMillisString()).child(cd.clock.getTransactionID()).removeValue();
                Snackbar.make(view, cd.clock.Car.getLicense() + " lavado por " + cd.clock.getDryerFirstName() + " fue terminado.",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            });

            cd.nextButton.setOnClickListener(nextListener);
            cd.stopButton.setOnClickListener(stopListener);
        }
        else
        {
            cd.nextButton.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.grayLight));
            cd.stopButton.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.grayLight));
        }
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

    public void setCountdownAccess()
    {
        CountdownAccess = true;
    }

    private CardChronometer findCountdown(String tag)
    {
        for(int i = 0; i < layout.getChildCount(); i++)
        {
            CardChronometer cd = (CardChronometer) layout.getChildAt(i);
            if(tag.equals(cd.getTag()))
                return cd;
        }
        return null;
    }
}