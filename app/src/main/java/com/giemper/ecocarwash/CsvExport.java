package com.giemper.ecocarwash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import com.google.firebase.database.DatabaseReference;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CsvExport
{
    // Reference https://developer.android.com/reference/android/os/Environment
    // Reference https://stackoverflow.com/questions/5401104/android-exporting-to-csv-and-sending-as-email-attachment

    private DatabaseReference ecoDatabase;
    private Context mContext;

    public CsvExport(DatabaseReference eco, Context context)
    {
        ecoDatabase = eco;
        mContext = context;

        Test();
    }

    private void Test()
    {
        String top = "\"Nombre\",\"Apellido\"";
        String down = "\"Taco\",\"De Asada\"";
        String combined = top + "\n" + down;

        File file = null;
        File root = Environment.getDataDirectory();
        if(root.canWrite())
        {
            File dir = new File (root.getAbsolutePath() + "/EcoCarWash");
            dir.mkdirs();
            file = new File(dir, "Report.csv");

            FileOutputStream out = null;
            try
            {
                out = new FileOutputStream(file);
                out.write(combined.getBytes());
                out.close();
            }
            catch (IOException ignored){ }
        }

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.ACTION_SENDTO, Uri.parse("mailto:gmpco51@gmail.com"));
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte Eco");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        sendIntent.setType("text/html");
        mContext.startActivity(sendIntent);
    }
}
