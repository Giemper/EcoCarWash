package com.giemper.ecocarwash;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import static com.giemper.ecocarwash.CarMethods.*;
import java.util.Calendar;
import java.util.List;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomePeople extends Fragment
{

    private DatabaseReference ecoDatabase;
    private View rootView;
    private List<Dryer> DryerList;
    private int QueueCount = 0;
    LinearLayout layout;

    public FragmentHomePeople() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_home_people, container, false);
        layout = rootView.findViewById(R.id.Card_Layout);
        ecoDatabase = FirebaseDatabase.getInstance().getReference();

        setDatabaseListener();
        setFloatingListener();

        return rootView;
    }

    private void setDatabaseListener()
    {
        Query queryList = ecoDatabase.child("Dryers/List").orderByChild("active").equalTo(true);
        queryList.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                layout.removeAllViews();
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Dryer dryer = snap.getValue(Dryer.class);
                    CardCheckbox dryerCheck = new CardCheckbox(getActivity(), dryer);

                    dryerCheck.setCheckBoxListener(dryer, ecoDatabase, getActivity());

                    if(dryer.getWorkStatus() == "Available")
                        dryerCheck.Box.setChecked(true);
                    else
                        dryerCheck.Box.setChecked(false);

                    layout.addView(dryerCheck);
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
            final DialogCreateDryer dcd = new DialogCreateDryer();
            dcd.AddDialog(getActivity(), view);

            setDialogCreateDryerListener(dcd);
        });
    }

    private void setDialogCreateDryerListener(DialogCreateDryer dcd)
    {
        final DialogCreateDryer dialogCreateDryer = dcd;
        Button add = dialogCreateDryer.dialog.findViewById(R.id.Dialog_CreateDryer_Button_Add);
        add.setOnClickListener((View view) ->
        {
            EditText firstName = dialogCreateDryer.dialog.findViewById(R.id.Dialog_CreateDryer_Name);
            EditText lastNameFather = dialogCreateDryer.dialog.findViewById(R.id.Dialog_CreateDryer_LastNameFather);
            EditText lastNameMother = dialogCreateDryer.dialog.findViewById(R.id.Dialog_CreateDryer_LastNameMother);
            long dryerID = Calendar.getInstance().getTimeInMillis();

            Dryer dryer = new Dryer();
            dryer.setDryerID(Long.toString(dryerID));
            dryer.setFirstName(firstName.getText().toString());
            dryer.setLastNameFather(lastNameFather.getText().toString());
            dryer.setLastNameMother(lastNameMother.getText().toString());
            dryer.setStartTime(getFullDate());
            dryer.setActive(true);

            dialogCreateDryer.dialog.dismiss();

            ecoDatabase.child("Dryers").child("List").child(dryer.getDryerID()).setValue(dryer);
        });
    }
}
