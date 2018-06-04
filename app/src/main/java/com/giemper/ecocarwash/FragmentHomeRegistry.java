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
        Query queryReport = ecoDatabase.child("Clocks/Archive").limitToLast(14);
        queryReport.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                String date = getMillisToString(Long.parseLong(dataSnapshot.getKey()));
                CardDayReport cardDayReport = new CardDayReport(getContext());
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

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setFloatingListener()
    {
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener((View view) ->
        {
            final DialogSendReport dialogSendReport = new DialogSendReport();
            dialogSendReport.AddDialog(getActivity());
            dialogSendReport.setButtonListeners(ecoDatabase);
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