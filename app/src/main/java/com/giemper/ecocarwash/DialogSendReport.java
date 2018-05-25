package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by gmoma on 4/21/2018.
 */

public class DialogSendReport
{
    public Dialog dialog;
    public Button add;

    public void AddDialog(Activity activity, View view)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sendreport);

        Button quit = dialog.findViewById(R.id.Button_Quit);
        quit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { dialog.dismiss(); }
        });

        dialog.show();
    }
}
