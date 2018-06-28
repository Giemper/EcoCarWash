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

public class DialogCreateDryer
{
    public Dialog dialog;

    private boolean CheckName;
    private boolean CheckFather;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
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
    }

    public void setListeners(DatabaseReference ecoDatabase)
    {
        Button add = dialog.findViewById(R.id.Dialog_CreateDryer_Button_Add);
        add.setOnClickListener((View view) ->
        {
            add.setOnClickListener(null);
            EditText firstName = dialog.findViewById(R.id.Dialog_CreateDryer_Name);
            EditText lastNameFather = dialog.findViewById(R.id.Dialog_CreateDryer_LastNameFather);
            long dryerID = Calendar.getInstance().getTimeInMillis();

            Dryer dryer = new Dryer();
            dryer.setDryerID(Long.toString(dryerID));
            dryer.setFirstName(firstName.getText().toString());
            dryer.setLastNameFather(lastNameFather.getText().toString());
            dryer.setStartTime(Calendar.getInstance().getTimeInMillis());
            dryer.setActive(true);
            dryer.setWorkStatus("none");

            ecoDatabase.child("Dryers/List").child(dryer.getDryerID()).setValue(dryer);

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
        if(CheckName && CheckFather)
            add.setEnabled(true);
        else
            add.setEnabled(false);
    }
}