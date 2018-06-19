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

public class DialogSendReportClocks extends DialogSendReport
{
    @Override
    public void AddDialog(Activity activity)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_sendreport_clocks);

        mActivity = activity;

        RelativeLayout dateStart = dialog.findViewById(R.id.Dialog_SendReport_DateStart);
        TextView textStart = dialog.findViewById(R.id.Dialog_SendReport_TextStart);
        setDatePickListeners(dateStart, textStart, 0);

        RelativeLayout dateEnd = dialog.findViewById(R.id.Dialog_SendReport_DateEnd);
        TextView textEnd = dialog.findViewById(R.id.Dialog_SendReport_TextEnd);
        setDatePickListeners(dateEnd, textEnd, 1);

        dialog.show();
    }

    @Override
    public void setButtonListeners(DatabaseReference ecoDatabase)
    {
        Button add = dialog.findViewById(R.id.Button_Add);
        add.setOnClickListener((View view) ->
        {
            CsvExport CSV = new CsvExport(ecoDatabase, dialog.getContext());
            CSV.sendClocksReport(DateStart, DateEnd, mActivity.findViewById(android.R.id.content));
        });


        Button quit = dialog.findViewById(R.id.Button_Quit);
        quit.setOnClickListener((View view) ->
        {
            dialog.dismiss();
        });
    }
}