package com.giemper.ecocarwash;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import static com.giemper.ecocarwash.CarMethods.getMillisToString;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomeRegistry extends Fragment
{

    private DatabaseReference ecoDatabase;
    private View rootView;
    private LinearLayout layout;

    public FragmentHomeRegistry() {
        // Required empty public constructor
    }

    public void setFirebase(DatabaseReference db)
    {
        ecoDatabase = db;
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
        Query queryReport = ecoDatabase.child("Clocks").limitToLast(14);
//        queryReport.addChildEventListener(new ChildEventListener()
//        {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s)
//            {
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s)
//            {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot)
//            {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s)
//            {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError)
//            {
//
//            }
//        });


        queryReport.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot snapDay : dataSnapshot.getChildren())
                {
                    String date = getMillisToString(Long.parseLong(snapDay.getKey()));
                    CardDayReport cardDayReport = new CardDayReport(getContext());
                    cardDayReport.setDayTitle(date);

                    for(DataSnapshot snapClock : snapDay.getChildren())
                    {
                        boolean snapActive = snapClock.child("active").equals(false);
                        if(snapActive)
                        {
                            Clocks clock = snapClock.getValue(Clocks.class);
                            cardDayReport.addClockLine(clock);
                        }
                    }

                    layout.addView(cardDayReport);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void setFloatingListener()
    {
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener((View view) ->
        {
            final DialogSendReport dialog = new DialogSendReport();
            dialog.AddDialog(getActivity(), view);
        });
    }

}