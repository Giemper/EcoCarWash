package com.giemper.ecocarwash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.giemper.ecocarwash.CarMethods.*;

public class CsvExport
{
    // Reference https://developer.android.com/reference/android/os/Environment
    // Reference https://stackoverflow.com/questions/5401104/android-exporting-to-csv-and-sending-as-email-attachment
    // Reference https://stackoverflow.com/questions/16111496/java-how-can-i-write-my-arraylist-to-a-file-and-read-load-that-file-to-the

    private DatabaseReference ecoDatabase;
    private Context mContext;
    private List<String> csvColumns;
    private List<String> csvRows;
    private File file;

    public CsvExport(DatabaseReference eco, Context context)
    {
        ecoDatabase = eco;
        mContext = context;

        file = null;

        csvColumns = new ArrayList<>();

        csvRows = new ArrayList<>();
        csvRows.add("");
        csvRows.add("");
    }

    public void getRange(Calendar startDate, Calendar endDate)
    {
        String start = Long.toString(startDate.getTimeInMillis());
        String end = Long.toString(endDate.getTimeInMillis());
        Query queryClocks = ecoDatabase.child("Clocks/Archive").orderByKey().startAt(start).endAt(end);

        queryClocks.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                long i = dataSnapshot.getChildrenCount();
                for(DataSnapshot snapGroup : dataSnapshot.getChildren())
                {
                    long snapDay = Long.parseLong(snapGroup.getKey());

                    for(DataSnapshot snapClock : snapGroup.getChildren())
                    {
                        Clocks clock = snapClock.getValue(Clocks.class);

                        AddData("ID", clock.getTransactionID());
                        AddData("Fecha", getDDMMYYYY(snapDay));
                        AddData("Secador Asignado", clock.getDryerLastName() +", " + clock.getDryerFirstName());
                        AddData("Placa del Auto", clock.Car.getLicense());
                        AddData("Color del Auto", clock.Car.getColor());
                        AddData("Paquete", Integer.toString(clock.Car.getPackage()));
                        AddData("Tamaño", clock.Car.getSize());
                        AddData("Entrada", getHHMMSS(clock.getStartTime()));
                        AddData("Inicio de Secado", getHHMMSS(clock.getMidTime()));
                        AddData("Salida", getHHMMSS(clock.getEndTime()));
                        AddData("Tiempo Transcurrido Total", getHHMMSS(clock.getStartTime(), clock.getEndTime()));
                        AddData("Tiempo Transcurrido de Espera", getHHMMSS(clock.getStartTime(), clock.getMidTime()));
                        AddData("Tiempo Transcurrido de Secado", getHHMMSS(clock.getMidTime(), clock.getEndTime()));
                        AddRow();
                    }
                }
                CreateFile();
                SendFile();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                throw databaseError.toException();
            }
        });
    }

    public void CreateFile()
    {
        int c = csvColumns.size();
        int s = csvRows.size();
        if(csvColumns.size() > 0)
        {
            File root = Environment.getExternalStorageDirectory();
            if(root.canWrite())
            {
                File dir = new File (root.getAbsolutePath() + "/EcoCarWash");
                dir.mkdirs();
                file = new File(dir, "Report.csv");

                try
                {
                    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_16);
                    PrintWriter pw = new PrintWriter(osw, true);
                    for(String row : csvRows)
                        pw.println(row);
                    pw.close();
                }
                catch (IOException ignored) {}
            }
        }
    }

    public void SendFile()
    {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.ACTION_SENDTO, Uri.parse("mailto:gmpco51@gmail.com"));
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte Eco");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        sendIntent.setType("text/html");
        mContext.startActivity(sendIntent);
    }

    private void AddData(String column, String data)
    {
        boolean b = csvColumns.contains(column);
        if(!csvColumns.contains(column))
        {
            csvColumns.add(column);
            csvRows.set(0, csvRows.get(0) + "\"" + column + "\",");
        }
        int row = csvRows.size() - 1;
        csvRows.set(row, csvRows.get(row) + "\"" + data + "\",");
    }

    private void AddRow()
    {
        csvRows.add("");
    }
}
