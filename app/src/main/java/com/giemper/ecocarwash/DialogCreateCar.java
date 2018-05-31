package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ToggleGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.giemper.ecocarwash.CarMethods.getTodayInMillis;
import static com.giemper.ecocarwash.CarMethods.getTodayInMillisString;
import static com.giemper.ecocarwash.CarMethods.getTodaySmallInString;

/**
 * Created by gmoma on 4/13/2018.
 */

public class DialogCreateCar
{
    public Button add;
    private Dialog dialog;
    private Calendar StartTime;
    private Boolean CheckPack = false;
    private Boolean CheckSize = false;
    private Boolean CheckLicence = false;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_createcar);

        StartTime = Calendar.getInstance();

        Chronometer chrono = dialog.findViewById(R.id.Dialog_CreateCar_Chronometer);
        chrono.start();

        setCheckers();
        dialog.show();
    }

    private void setCheckers()
    {
        ToggleGroup Group_Pack = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_Pack);
        Group_Pack.setOnCheckedChangeListener((ToggleGroup group, int[] checkedId) ->
        {
            CheckPack = true;
            CheckDialog();
        });

        ToggleGroup Group_Size = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_Size);
        Group_Size.setOnCheckedChangeListener((ToggleGroup group, int[] checkedId) ->
        {
            CheckSize = true;
            CheckDialog();
        });

        EditText License = dialog.findViewById(R.id.Dialog_CreateCar_Text_Licence);
        License.addTextChangedListener(new TextWatcher()
        {
            @Override public void afterTextChanged(Editable s){}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() > 0)
                {
                    CheckLicence = true;
                    CheckDialog();
                }
                else
                    CheckLicence = false;
            }
        });
    }

    public void setSpinners(DatabaseReference ecoDatabase)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dialog.getOwnerActivity(), R.array.Dialog_CreateCar_SpinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerColor = dialog.findViewById(R.id.Dialog_CreateCar_SpinnerColor);
        spinnerColor.setAdapter(adapter);


        Spinner spinnerDryer = dialog.findViewById(R.id.Dialog_CreateCar_SpinnerDryer);
        spinnerDryer.setOnClickListener((View view) ->
        {
            Query queryDryers = ecoDatabase.child("Dryers").orderByChild("workStatus").equalTo("Available");
            queryDryers.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override public void onCancelled(DatabaseError databaseError){}
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(dialog.getContext(), R.layout.support_simple_spinner_dropdown_item);

                    for(DataSnapshot snap : dataSnapshot.getChildren())
                    {
                        Dryer dryer = snap.getValue(Dryer.class);
                        arrayAdapter.add(dryer.fullName());
                    }


                    // reference https://stackoverflow.com/questions/5241660/how-can-i-add-items-to-a-spinner-in-android
                    // reference for tag https://stackoverflow.com/questions/20902102/androidhow-to-set-tag-with-spinner-item

                }
            });
            spinnerDryer.setOnClickListener(null);
        });

    }

    public void setDialogCreateCarListener(DatabaseReference ecoDatabase)
    {
        Button add = dialog.findViewById(R.id.Dialog_CreateCar_Button_Add);
        add.setOnClickListener((View view) ->
        {
            Clocks clock = new Clocks(getTodaySmallInString());
            clock.setCarValues(dialog);
            clock.setStartTime(StartTime.getTimeInMillis());

            dialog.dismiss();

            ecoDatabase.child("Clocks/Active").child(getTodayInMillisString()).child(clock.getTransactionID()).setValue(clock);
        });

        Button quit = dialog.findViewById(R.id.Dialog_CreateCar_Button_Quit);
        quit.setOnClickListener((View v) ->
        {
            dialog.dismiss();
        });
    }

    private void CheckDialog()
    {
        Button add = dialog.findViewById(R.id.Dialog_CreateCar_Button_Add);
        if(CheckPack && CheckSize && CheckLicence)
            add.setEnabled(true);
        else
            add.setEnabled(false);
    }
}