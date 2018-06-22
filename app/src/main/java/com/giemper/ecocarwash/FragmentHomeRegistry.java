package com.giemper.ecocarwash;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import static com.giemper.ecocarwash.EcoMethods.getMillisToString;

public class FragmentHomeRegistry extends Fragment
{

    private DatabaseReference ecoDatabase;
    private FirebaseUser ecoUser;
    private String ecoUserType;
    private View rootView;
    private LinearLayout layout;
    private Context mContext;

    public FragmentHomeRegistry() {}

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_home_registry, container, false);
        layout = rootView.findViewById(R.id.Card_Layout);

        setFloatingListener();
        setDatabaseListener();

        return rootView;
    }

    private void setDatabaseListener()
    {
        Query queryReport = ecoDatabase.child("Clocks/Archive").limitToLast(7);
        queryReport.addChildEventListener(new ChildEventListener()
        {
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s){}
            @Override public void onCancelled(DatabaseError databaseError){}
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                String date = getMillisToString(Long.parseLong(dataSnapshot.getKey()));
                CardDayReport cardDayReport = new CardDayReport(mContext);
                cardDayReport.setDayTitle(date);
                cardDayReport.setTag(dataSnapshot.getKey());

                for(DataSnapshot snapClock : dataSnapshot.getChildren())
                {
                    Clocks clock = snapClock.getValue(Clocks.class);
                    cardDayReport.addClockLine(clock);
                }

                layout.addView(cardDayReport, 0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                CardDayReport cardDayReport = findCardDayReport(dataSnapshot.getKey());
//                cardDayReport.layout.removeAllViews();
                cardDayReport.layout.removeViews(1, cardDayReport.layout.getChildCount() - 1);
                for(DataSnapshot snapClock : dataSnapshot.getChildren())
                {
                    boolean snapActive = snapClock.child("active").getValue(boolean.class);
                    if(!snapActive)
                    {
                        Clocks clock = snapClock.getValue(Clocks.class);
                        cardDayReport.addClockLine(clock);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                CardDayReport cardDayReport = findCardDayReport(dataSnapshot.getKey());
                layout.removeView(cardDayReport);
            }
        });
    }

    private void setFloatingListener()
    {
        // Reference: https://github.com/leinardi/FloatingActionButtonSpeedDial

        SpeedDialActionItem.Builder fabClocks = new SpeedDialActionItem.Builder(R.id.fab_reportClocks, R.drawable.ic_timer)
                .setFabBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent3))
                .setLabel("Reporte de Lavados");

        SpeedDialActionItem.Builder fabDryers = new SpeedDialActionItem.Builder(R.id.fab_reportDryers, R.drawable.ic_people)
                .setFabBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent3))
                .setLabel("Reporte de Secadores");

        SpeedDialView speedFab = rootView.findViewById(R.id.speed_fab);
        speedFab.addActionItem(fabClocks.create());
        speedFab.addActionItem(fabDryers.create());
        speedFab.setOnActionSelectedListener((SpeedDialActionItem actionItem) ->
        {
            if(actionItem.getId() == R.id.fab_reportClocks)
            {
                final DialogSendReportClocks dialogSendReportClocks = new DialogSendReportClocks();
                dialogSendReportClocks.AddDialog(getActivity());
                dialogSendReportClocks.setButtonListeners(ecoDatabase);
                return false;
            }
            else if(actionItem.getId() == R.id.fab_reportDryers)
            {
                DialogSendReportDryers dialogSendReportDryers = new DialogSendReportDryers();
                dialogSendReportDryers.AddDialog(getActivity());
                dialogSendReportDryers.setButtonListeners(ecoDatabase);
                return false;
            }
            else
                return false;
        });
    }

    private CardDayReport findCardDayReport(String tag)
    {
        for(int i = 0; i < layout.getChildCount(); i++)
        {
            CardDayReport cardDayReport = (CardDayReport) layout.getChildAt(i);
            if(tag.equals(cardDayReport.getTag()))
                return cardDayReport;
        }
        return null;
    }

}