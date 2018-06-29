package com.giemper.ecocarwash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

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

import static com.giemper.ecocarwash.EcoMethods.*;

public class CsvExport
{
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

    public void sendClocksReport(Calendar startDate, Calendar endDate, View view)
    {
        String start = Long.toString(startDate.getTimeInMillis());
        String end = Long.toString(endDate.getTimeInMillis());
        Query queryClocks = ecoDatabase.child("Clocks/Archive").orderByKey().startAt(start).endAt(end);
        queryClocks.keepSynced(true);
        queryClocks.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override public void onCancelled(@NonNull DatabaseError databaseError){}
            @Override
            public void onDataChange(@ NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getChildrenCount() > 0)
                {
                    for (DataSnapshot snapGroup : dataSnapshot.getChildren())
                    {
                        long snapDay = Long.parseLong(snapGroup.getKey());

                        for (DataSnapshot snapClock : snapGroup.getChildren())
                        {
                            Clocks clock = snapClock.getValue(Clocks.class);

                            AddData("ID", clock.getTransactionID());
                            AddData("Fecha", getDDMMYYYY(snapDay));
                            AddData("Secador Asignado", clock.getDryerLastName() + ", " + clock.getDryerFirstName());
                            AddData("Placa del Auto", clock.Car.getLicense());
                            AddData("Color del Auto", clock.Car.getColor());
                            AddData("Paquete", clock.Car.getPackage());
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
                    SendFile("Reporte de Cronometros");
                }
                else
                {
                    // Say that no info is available here

                    Snackbar.make(view, "No hay fechas disponibles", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    public void sendDryersReport(Calendar startDate, Calendar endDate, View view)
    {
        String start = Long.toString(startDate.getTimeInMillis());
        String end = Long.toString(endDate.getTimeInMillis());



        Query queryDryers = ecoDatabase.child("Dryers/Attendance").orderByKey().startAt(start).endAt(end);
        queryDryers.keepSynced(true);
        queryDryers.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getChildrenCount() > 0)
                {
                    List<DryerPairAttendance> pairAttendances = new ArrayList<>();

                    for(DataSnapshot snap : dataSnapshot.getChildren())
                    {
                        long snapDay = Long.parseLong(snap.getKey());

                        for (DataSnapshot snapAttendance : snap.getChildren())
                        {
                            int index;
                            DryerAttendance attendance = snapAttendance.getValue(DryerAttendance.class);

                            if(attendance.getAction().equals("Enter"))
                            {
                                index = pairAttendances.size();
                                pairAttendances.add(new DryerPairAttendance(attendance));
                                pairAttendances.get(index).setStartTime(attendance.getDate());
                                pairAttendances.get(index).setDate(snapDay);
                            }
                            else
                            {
                                index = findPairAttendance(attendance.getDryerID(), snapDay, pairAttendances);
                                if(index >= 0)
                                    pairAttendances.get(index).setEndTime(attendance.getDate());
                                else
                                {
                                    pairAttendances.add(new DryerPairAttendance(attendance));
                                    pairAttendances.get(pairAttendances.size() - 1).setEndTime(attendance.getDate());
                                    pairAttendances.get(pairAttendances.size() - 1).setDate(snapDay);
                                }
                            }
                        }
                    }

                    printPairAttendances(pairAttendances);
                }
                else
                {
                    Snackbar.make(view, "No hay casos dentro de esas fechas.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                throw databaseError.toException();
            }
        });

    }

    private void printPairAttendances(List<DryerPairAttendance> pairAttendance)
    {
        for(DryerPairAttendance attendance : pairAttendance)
        {
            String Note = "";
            if (attendance.getStartTime() == 0)
                Note = "Entrada no registrada.";
            if(attendance.getEndTime() == 0)
                Note = "Salida no registrada.";

            AddData("Fecha", getDDMMYYYY(attendance.getDate()));
            AddData("ID", attendance.getDryerID());
            AddData("Nombre", attendance.getDryerName());
            AddData("Hora de Entrada", getHHMMSS(attendance.getStartTime()));
            AddData("Hora de Salida", getHHMMSS(attendance.getEndTime()));
            AddData("Notas", Note);
            AddRow();
        }

        CreateFile();
        SendFile("Reporte de Asistencia");
    }

    private void CreateFile()
    {
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

    private void SendFile(String fileTitle)
    {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.ACTION_SENDTO, Uri.parse("mailto:gmpco51@gmail.com"));
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Eco Car Wash - " + fileTitle);
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

    private int findPairAttendance(String DryerID, long Date, List<DryerPairAttendance> pairAttendances)
    {
        for(int i = 0; i < pairAttendances.size(); i++)
        {
            if(!pairAttendances.get(i).getComplete())
            {
                if (pairAttendances.get(i).getDryerID().equals(DryerID) && pairAttendances.get(i).getDate() == Date)
                    return i;
            }
        }

        return -1;
    }
}
