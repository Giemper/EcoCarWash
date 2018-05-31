package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by guillermo.magdaleno on 4/20/2018.
 */

public class DialogCreateDryer
{
    public Dialog dialog;

    private boolean CheckName;
    private boolean CheckFather;
    private boolean CheckMother;
    private boolean CheckDate;
    private Calendar EntryDate;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_createdryer);

        setCheckers();

        dialog.show();
    }

    private void setCheckers()
    {
        EditText firstName = dialog.findViewById(R.id.Dialog_CreateDryer_Name);
        firstName.addTextChangedListener(new TextWatcher()
        {
            @Override public void afterTextChanged(Editable editable){}
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence.length() > 0)
                {
                    CheckName = true;
                    CheckDialog();
                }
                else
                    CheckName = false;
            }
        });

        EditText lastNameFather = dialog.findViewById(R.id.Dialog_CreateDryer_LastNameFather);
        lastNameFather.addTextChangedListener(new TextWatcher()
        {
            @Override public void afterTextChanged(Editable editable){}
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence.length() > 0)
                {
                    CheckFather = true;
                    CheckDialog();
                }
                else
                    CheckFather = false;
            }
        });

        EditText lastNameMother = dialog.findViewById(R.id.Dialog_CreateDryer_LastNameMother);
        lastNameMother.addTextChangedListener(new TextWatcher()
        {
            @Override public void afterTextChanged(Editable editable){}
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence.length() > 0)
                {
                    CheckMother = true;
                    CheckDialog();
                }
                else
                    CheckMother = false;
            }
        });

        Calendar date = Calendar.getInstance();
        TextView dateText = dialog.findViewById(R.id.Dialog_CreateDryer_TextDate);
        RelativeLayout dateEntry = dialog.findViewById(R.id.Dialog_CreateDryer_Date);
        DatePickerDialog.OnDateSetListener datePickerDialog = ((DatePicker datePicker, int year, int month, int day) ->
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);

            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, month);
            date.set(Calendar.DAY_OF_MONTH, day);
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);

            dateText.setText(simpleDateFormat.format(date.getTime()));
            if(dateText.getText().length() > 0)
            {
                EntryDate = date;
                CheckDate = true;
                CheckDialog();
            }
            else
                CheckDate = false;
        });

        dateEntry.setOnClickListener((View view) ->
        {
            DatePickerDialog picker = new DatePickerDialog(
                    dialog.getContext(),
                    datePickerDialog,
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH)
            );
            picker.show();
        });
    }

    public void setListeners(DatabaseReference ecoDatabase)
    {
        Button add = dialog.findViewById(R.id.Dialog_CreateDryer_Button_Add);
        add.setOnClickListener((View view) ->
        {
            EditText firstName = dialog.findViewById(R.id.Dialog_CreateDryer_Name);
            EditText lastNameFather = dialog.findViewById(R.id.Dialog_CreateDryer_LastNameFather);
            EditText lastNameMother = dialog.findViewById(R.id.Dialog_CreateDryer_LastNameMother);
            long dryerID = Calendar.getInstance().getTimeInMillis();

            Dryer dryer = new Dryer();
            dryer.setDryerID(Long.toString(dryerID));
            dryer.setFirstName(firstName.getText().toString());
            dryer.setLastNameFather(lastNameFather.getText().toString());
            dryer.setLastNameMother(lastNameMother.getText().toString());
            dryer.setStartTime(EntryDate.getTimeInMillis());
            dryer.setActive(true);
            dryer.setWorkStatus("None");

            ecoDatabase.child("Dryers").child(dryer.getDryerID()).setValue(dryer);

            dialog.dismiss();
        });

        Button quit = dialog.findViewById(R.id.Dialog_CreateDryer_Button_Quit);
        quit.setOnClickListener((View view) ->
        {
            dialog.dismiss();
        });
    }

    private void CheckDialog()
    {
        Button add = dialog.findViewById(R.id.Dialog_CreateDryer_Button_Add);
        if(CheckName && CheckFather && CheckMother && CheckDate)
            add.setEnabled(true);
        else
            add.setEnabled(false);
    }
}