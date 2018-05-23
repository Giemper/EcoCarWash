package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class DialogInfoDryer
{
    public Dialog dialog;
    public Button Accept;
    public Button Delete;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_createdryer);

        Accept = dialog.findViewById(R.id.Dialog_InfoDryer_Accept);
        Delete = dialog.findViewById(R.id.Dialog_InfoDryer_Delete);

        Accept.setOnClickListener((View v) ->
        {
            dialog.dismiss();
        });

        dialog.show();
    }
}
