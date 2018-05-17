package com.giemper.ecocarwash;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.giemper.ecocarwash.CarMethods.*;
import java.util.Calendar;



/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomePeople extends Fragment {

    private DatabaseReference ecoDatabase;
    private View rootView;
    private RecyclerHomePeople adapterHomePeople;


    public FragmentHomePeople() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_home_people, container, false);
        ecoDatabase = FirebaseDatabase.getInstance().getReference();


        String[] arrayHomePoeple = getResources().getStringArray(R.array.Recycler_Home_People);
        adapterHomePeople = new RecyclerHomePeople(arrayHomePoeple);
        RecyclerView recycler = (RecyclerView) rootView.findViewById(R.id.Recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapterHomePeople);

        setFloatingListener();

        return rootView;
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

            dialogCreateDryer.dialog.dismiss();

            ecoDatabase.child("Dryers").child("List").setValue(dryer);
        });
    }

}
