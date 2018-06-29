package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.ToggleGroup;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import static com.giemper.ecocarwash.EcoMethods.getTodayInMillisString;
import static com.giemper.ecocarwash.EcoMethods.getTodaySmallInString;

public class DialogCreateCar
{
    public Dialog dialog;
    private Calendar StartTime;
    private boolean CheckPack = false;
    private boolean CheckSize = false;
    private boolean CheckLicence = false;
    private boolean optionalDryer = false;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_createcar);


        StartTime = Calendar.getInstance();

        Chronometer chrono = dialog.findViewById(R.id.Dialog_CreateCar_Chronometer);
        chrono.start();

        setCheckers();
        dialog.show();
    }

    private void setCheckers()
    {
        ToggleGroup Group_Pack_Top = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_PackTop);
        ToggleGroup Group_Pack_Bottom = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_PackBottom);
        Group_Pack_Top.setOnCheckedChangeListener((ToggleGroup group, int[] checkedId) ->
        {
            if(Group_Pack_Top.getCheckedId() != -1)
            {
                Group_Pack_Bottom.clearChecked();
                CheckPack = true;
                CheckDialog();
            }
        });

        Group_Pack_Bottom.setOnCheckedChangeListener((ToggleGroup group, int[] checkedId) ->
        {
            if(Group_Pack_Bottom.getCheckedId() != -1)
            {
                Group_Pack_Top.clearChecked();
                CheckPack = true;
                CheckDialog();
            }
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dialog.getContext(), R.array.Dialog_CreateCar_SpinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerColor = dialog.findViewById(R.id.Dialog_CreateCar_SpinnerColor);
        spinnerColor.setAdapter(adapter);

        Spinner spinnerDryer = dialog.findViewById(R.id.Dialog_CreateCar_SpinnerDryer);
        Button pre = dialog.findViewById(R.id.Dialog_CreateCar_SpinnerDryerPre);
        pre.setOnClickListener((View view) ->
        {
            Query queryDryers = ecoDatabase.child("Dryers/List").orderByChild("workStatus").equalTo("available");
            queryDryers.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override public void onCancelled(@NonNull DatabaseError databaseError){}
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    ArrayAdapter<DrySpinner> arrayAdapter = new ArrayAdapter<>(dialog.getContext(), android.R.layout.simple_spinner_item);
                    if(dataSnapshot.getChildrenCount() > 0)
                    {
                        optionalDryer = true;
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        arrayAdapter.add(new DrySpinner("none", "", ""));

                        for (DataSnapshot snap : dataSnapshot.getChildren())
                        {
                            Dryer dryer = snap.getValue(Dryer.class);
                            arrayAdapter.add(new DrySpinner(dryer.getDryerID(), dryer.getFirstName(), dryer.getLastNameFather()));
                        }
                        spinnerDryer.setAdapter(arrayAdapter);
                        spinnerDryer.performClick();
                    }
                    else
                    {
                        arrayAdapter.add(new DrySpinner("", "No hay secadores disponibles.", ""));
                        spinnerDryer.setAdapter(arrayAdapter);
                        spinnerDryer.setEnabled(false);
                    }
                }
            });
            pre.setVisibility(View.GONE);
        });
    }

    public void setDialogCreateCarListener(DatabaseReference ecoDatabase)
    {
        Button add = dialog.findViewById(R.id.Dialog_CreateCar_Button_Add);
        add.setOnClickListener((View view) ->
        {
            add.setOnClickListener(null);

            Clocks clock = new Clocks(getTodaySmallInString());
            clock.setCarValues(dialog);
            clock.setStartTime(StartTime.getTimeInMillis());

            Map hash = new HashMap<>();
            hash.put("Clocks/Active/" + getTodayInMillisString() + "/" + clock.getTransactionID(), clock);

            if(optionalDryer)
            {
                Spinner spinner = dialog.findViewById(R.id.Dialog_CreateCar_SpinnerDryer);
                DrySpinner selectedDryer = (DrySpinner) spinner.getSelectedItem();
                if(!selectedDryer.getTag().equals("none"))
                {
                    clock.setDryerID(selectedDryer.getTag());
                    clock.setDryerFirstName(selectedDryer.getFirstName());
                    clock.setDryerLastName(selectedDryer.getLastName());

                    hash.put("Dryers/List/" + clock.getDryerID() + "/workStatus", "busy");
                    hash.put("Dryers/List/" + clock.getDryerID() + "/queue", 0);
                }
            }

            ecoDatabase.updateChildren(hash);
            dialog.dismiss();
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