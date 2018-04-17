package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.ToggleGroup;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

/**
 * Created by gmoma on 4/13/2018.
 */

public class DialogCreateCar
{
    public Dialog dialog;
    public Button add;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_createcar);

        Spinner spinner = dialog.findViewById(R.id.Dialog_CreateCar_Spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.Dialog_CreateCar_SpinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button quit = (Button) dialog.findViewById(R.id.Dialog_Button_Quit);
        quit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        Button add = (Button) dialog.findViewById(R.id.Dialog_Button_Add);





        dialog.show();
    }

    public CreateCarValue getCarValues(Dialog dialog)
    {
        CreateCarValue values = new CreateCarValue();
        ToggleGroup Group_Pack = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_Pack);
        ToggleGroup Group_Size = dialog.findViewById(R.id.Dialog_CreateCar_Toggle_Size);
        Spinner Spinner_Color = dialog.findViewById(R.id.Dialog_CreateCar_Spinner);
        EditText Text_Licence =dialog.findViewById(R.id.Dialog_Text_Licence);

        values.Package = Group_Pack.getCheckedId();
        values.Size = Group_Size.getCheckedId();
        values.Color = Spinner_Color.toString();
        values.License = Text_Licence.toString();

        return values;

//        for(int i = 0; i < Group_Pack.getChildCount(); i++)
//        {
//            ToggleButton temp = (ToggleButton)Group_Pack.getChildAt(i);
//            if(temp.isChecked())
//            {
//                values.Package = i;
//                break;
//            }
//        }
//
//        for(int i = 0; i < Group_Size.getChildCount(); i++)
//        {
//            ToggleButton temp = (ToggleButton)Group_Size.getChildAt(i);
//            if(temp.isChecked())
//            {
//                values.Size = i;
//                break;
//            }
//        }
    }
}

class CreateCarValue
{
    public int Package;
    public int Size;
    public String Color;
    public String License;
}