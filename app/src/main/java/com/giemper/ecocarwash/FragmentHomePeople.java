package com.giemper.ecocarwash;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import static com.giemper.ecocarwash.CarMethods.*;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomePeople extends Fragment {

    private DatabaseReference ecoDatabase;
    private View rootView;
    LinearLayout layout;

    public FragmentHomePeople() {
        // Required empty public constructor
    }

    public void setFirebase(DatabaseReference db)
    {
        ecoDatabase = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_home_people, container, false);
        layout = rootView.findViewById(R.id.Card_Layout);
        ecoDatabase = FirebaseDatabase.getInstance().getReference();

        setDatabaseListener();
        setFloatingListener();

        return rootView;
    }

    private void setDatabaseListener()
    {
        Query queryList = ecoDatabase.child("Dryers").orderByChild("active").equalTo(true);
        queryList.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Dryer dryer = dataSnapshot.getValue(Dryer.class);
                CardCheckbox cardCheckbox = new CardCheckbox(getActivity(), dryer);
                cardCheckbox.setTag(dryer.getDryerID());

                if(dryer.getWorkStatus().equals("Available"))
                {
                    cardCheckbox.Box.setChecked(true);
                }
                else if(dryer.getWorkStatus().equals("Busy"))
                {
                    cardCheckbox.Box.setChecked(true);
                    cardCheckbox.Box.setEnabled(false);
                }

                cardCheckbox.setCheckBoxListener(dryer, ecoDatabase, getActivity());
                layout.addView(cardCheckbox);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                Dryer dryer = dataSnapshot.getValue(Dryer.class);
                CardCheckbox cardCheckbox = findCardCheckbox(dryer.getDryerID());

                if(dryer.getWorkStatus().equals("Available"))
                {
                    cardCheckbox.Box.setChecked(true);
                    cardCheckbox.Box.setEnabled(true);
                }
                else if(dryer.getWorkStatus().equals("Busy"))
                {
                    cardCheckbox.Box.setChecked(true);
                    cardCheckbox.Box.setEnabled(false);
                }
                else if(dryer.getWorkStatus().equals("None"))
                {
                    cardCheckbox.Box.setChecked(false);
                    cardCheckbox.Box.setEnabled(true);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                Dryer dryer = dataSnapshot.getValue(Dryer.class);
                CardCheckbox cardCheckbox = findCardCheckbox(dryer.getDryerID());
                layout.removeView(cardCheckbox);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    private void setFloatingListener()
    {
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener((View view) ->
        {
            final DialogCreateDryer dcd = new DialogCreateDryer();
            dcd.AddDialog(getActivity(), view);
            dcd.setListeners(ecoDatabase);
        });
    }

    private CardCheckbox findCardCheckbox(String tag)
    {
        for(int i = 0; i < layout.getChildCount(); i++)
        {
            CardCheckbox cardCheckbox = (CardCheckbox) layout.getChildAt(i);
            if(tag.equals(cardCheckbox.getTag()))
                return cardCheckbox;
        }
        return null;
    }
}