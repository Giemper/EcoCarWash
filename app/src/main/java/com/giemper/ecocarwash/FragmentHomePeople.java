package com.giemper.ecocarwash;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FragmentHomePeople extends Fragment
{
    private DatabaseReference ecoDatabase;
    private FirebaseUser ecoUser;
    private String ecoUserType;
    private View rootView;
    private LinearLayout layoutRegistry;
    private LinearLayout layoutActive;
    private CardView CardRegistry;
    private CardView CardActive;
    private Context mContext;

    public FragmentHomePeople() {}

    public void setFirebase(DatabaseReference db, FirebaseUser user, String userType)
    {
        ecoDatabase = db;
        ecoUser = user;
        ecoUserType = userType;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView =  inflater.inflate(R.layout.fragment_home_people, container, false);
        layoutRegistry = rootView.findViewById(R.id.Card_Layout);
        layoutActive = rootView.findViewById(R.id.Card_Layout_Active);
        CardRegistry = rootView.findViewById(R.id.Card_Regitry);
        CardActive = rootView.findViewById(R.id.Card_Active);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setEnabled(false);

//        setDatabaseListener();
//        setFloatingListener();

        return rootView;
    }

    public void setCheckboxes()
    {
        CardRegistry.setVisibility(View.VISIBLE);
        Query queryList = ecoDatabase.child("Dryers/List").orderByChild("active").equalTo(true);
        queryList.addChildEventListener(new ChildEventListener()
        {
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s){}
            @Override public void onCancelled(DatabaseError databaseError){}
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Dryer dryer = dataSnapshot.getValue(Dryer.class);
                CardCheckbox cardCheckbox = new CardCheckbox(mContext, dryer);
                cardCheckbox.setTag(dryer.getDryerID());
                cardCheckbox.Box.setOnCheckedChangeListener(null);

                if(dryer.getWorkStatus().equals("available"))
                {
                    cardCheckbox.Box.setChecked(true);
                }
                else if(dryer.getWorkStatus().equals("busy"))
                {
                    cardCheckbox.Box.setChecked(true);
                    cardCheckbox.Box.setEnabled(false);
                }

                cardCheckbox.setCheckBoxListener(dryer, ecoDatabase, getActivity());
                layoutRegistry.addView(cardCheckbox);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                Dryer dryer = dataSnapshot.getValue(Dryer.class);
                CardCheckbox cardCheckbox = findCardCheckbox(dryer.getDryerID());
                cardCheckbox.Box.setOnCheckedChangeListener(null);

                if(dryer.getWorkStatus().equals("available"))
                {
                    cardCheckbox.Box.setChecked(true);
                    cardCheckbox.Box.setEnabled(true);
                }
                else if(dryer.getWorkStatus().equals("busy"))
                {
                    cardCheckbox.Box.setChecked(true);
                    cardCheckbox.Box.setEnabled(false);
                }
                else if(dryer.getWorkStatus().equals("none"))
                {
                    cardCheckbox.Box.setChecked(false);
                    cardCheckbox.Box.setEnabled(true);
                }

                cardCheckbox.setCheckBoxListener(dryer, ecoDatabase, getActivity());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                Dryer dryer = dataSnapshot.getValue(Dryer.class);
                CardCheckbox cardCheckbox = findCardCheckbox(dryer.getDryerID());
                layoutRegistry.removeView(cardCheckbox);
            }
        });

    }

    public void setActiveList()
    {
        CardActive.setVisibility(View.VISIBLE);
        Query queryList = ecoDatabase.child("Dryers/List").orderByChild("queue").startAt(1);
        queryList.addChildEventListener(new ChildEventListener()
        {
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s){}
            @Override public void onCancelled(DatabaseError databaseError){}
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s){}
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Dryer dryer = dataSnapshot.getValue(Dryer.class);
                CardActiveList cardActiveList = new CardActiveList(mContext, dryer);
                cardActiveList.setTag(dryer.getDryerID());
                layoutActive.addView(cardActiveList);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                Dryer dryer = dataSnapshot.getValue(Dryer.class);
                CardActiveList cardActiveList = findCardActiveList(dryer.getDryerID());
                layoutActive.removeView(cardActiveList);
            }
        });

    }

    public void setFloatingListener()
    {
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setEnabled(true);
        fab.setOnClickListener((View view) ->
        {
            final DialogCreateDryer dcd = new DialogCreateDryer();
            dcd.AddDialog(getActivity(), view);
            dcd.setListeners(ecoDatabase);
        });
    }

    private CardCheckbox findCardCheckbox(String tag)
    {
        for(int i = 0; i < layoutRegistry.getChildCount(); i++)
        {
            CardCheckbox cardCheckbox = (CardCheckbox) layoutRegistry.getChildAt(i);
            if(tag.equals(cardCheckbox.getTag()))
                return cardCheckbox;
        }
        return null;
    }

    private CardActiveList findCardActiveList(String tag)
    {
        for(int i = 0; i < layoutActive.getChildCount(); i++)
        {
            CardActiveList cardActiveList = (CardActiveList) layoutActive.getChildAt(i);
            if(tag.equals(cardActiveList.getTag()))
                return cardActiveList;
        }
        return null;
    }
}