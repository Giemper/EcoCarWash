package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gmoma on 4/21/2018.
 */

public class DialogSendReport
{
    public Dialog dialog;
    public Button add;

    private boolean CheckStart;
    private boolean CheckEnd;

    private Calendar DateStart;
    private Calendar DateEnd;
    private Activity mActivity;

    public void AddDialog(Activity activity)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sendreport);

        mActivity = activity;

        RelativeLayout dateStart = dialog.findViewById(R.id.Dialog_SendReport_DateStart);
        TextView textStart = dialog.findViewById(R.id.Dialog_SendReport_TextStart);
        setDatePickListeners(dateStart, textStart, 0);

        RelativeLayout dateEnd = dialog.findViewById(R.id.Dialog_SendReport_DateEnd);
        TextView textEnd = dialog.findViewById(R.id.Dialog_SendReport_TextEnd);
        setDatePickListeners(dateEnd, textEnd, 1);

        dialog.show();
    }

    private void setDatePickListeners(RelativeLayout relative, TextView textView, int check)
    {
        Calendar date = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener datePickerDialog = ((DatePicker datePicker, int year, int month, int day) ->
        {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, month);
            date.set(Calendar.DAY_OF_MONTH, day);
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);

            textView.setText(simpleDateFormat.format(date.getTime()));
            if(textView.getText().length() > 0)
            {
                if(check == 0)
                {
                    DateStart = date;
                    CheckStart = true;
                }
                else
                {
                    DateEnd = date;
                    CheckEnd = true;
                }
                CheckDialog();
            }
            else
            {
                if(check == 0)
                    CheckStart = false;
                else
                    CheckEnd = false;
            }
        });

        relative.setOnClickListener((View view) ->
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

    public void CheckDialog()
    {
        Button add = dialog.findViewById(R.id.Button_Add);
        if(CheckStart && CheckEnd)
        {
            if(DateEnd.getTimeInMillis() >= DateStart.getTimeInMillis())
                add.setEnabled(true);
            else
                add.setEnabled(false);
        }
        else
            add.setEnabled(false);
    }

    public void setButtonListeners(DatabaseReference ecoDatabase)
    {
        Button add = dialog.findViewById(R.id.Button_Add);
        add.setOnClickListener((View view) ->
        {
            CsvExport CSV = new CsvExport(ecoDatabase, dialog.getContext());
            CSV.getRange(DateStart, DateEnd, mActivity.findViewById(android.R.id.content));

//            CSV.CreateFile();
//            CSV.SendFile();
        });


        Button quit = dialog.findViewById(R.id.Button_Quit);
        quit.setOnClickListener((View view) ->
        {
            dialog.dismiss();
        });
    }
}