package com.giemper.ecocarwash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class DialogLogout
{
    public Dialog dialog;
    private Activity mActivity;

    public void AddDialog(Activity activity)
    {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.show();

        mActivity = activity;
    }

    public void setListeners()
    {
        Button cancel = dialog.findViewById(R.id.Dialog_Logout_Cancel);
        cancel.setOnClickListener((View view) ->
        {
            dialog.dismiss();
        });

        Button close = dialog.findViewById(R.id.Dialog_Logout_Close);
        close.setOnClickListener((View view) ->
        {
            dialog.dismiss();
            FirebaseAuth.getInstance().signOut();
            mActivity.startActivity(new Intent(mActivity.getApplication(), LoginTest.class));
            mActivity.finish();
        });
    }
}
