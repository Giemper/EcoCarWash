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
import android.support.v7.widget.ToggleButton;
import java.util.Calendar;

/**
 * Created by gmoma on 4/13/2018.
 */

public class DialogCreateCar
{
    public Dialog dialog;
    public Button add;
    public Calendar StartTime;
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

        Spinner spinner = dialog.findViewById(R.id.Dialog_CreateCar_Spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.Dialog_CreateCar_SpinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Chronometer chrono = dialog.findViewById(R.id.Dialog_CreateCar_Chronometer);
        chrono.start();

        Button quit = (Button) dialog.findViewById(R.id.Dialog_CreateCar_Button_Quit);
        quit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        setListeners(view);

        dialog.show();
    }

    private void setListeners(View _view)
    {
        final View view = _view;
        ToggleGroup Group_Pack = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_Pack);
        Group_Pack.setOnCheckedChangeListener(new ToggleGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(ToggleGroup group, int[] checkedId)
            {
                CheckPack = true;
                CheckDialog();
            }
        });

        ToggleGroup Group_Size = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_Size);
        Group_Size.setOnCheckedChangeListener(new ToggleGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(ToggleGroup group, int[] checkedId)
            {
                CheckSize = true;
                CheckDialog();
            }
        });

        EditText License = dialog.findViewById(R.id.Dialog_CreateCar_Text_Licence);
        License.addTextChangedListener(new TextWatcher()
        {
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

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            @Override
            public void afterTextChanged(Editable s){}
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
