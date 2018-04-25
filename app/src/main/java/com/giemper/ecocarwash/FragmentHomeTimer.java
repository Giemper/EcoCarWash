package com.giemper.ecocarwash;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomeTimer extends Fragment {

    private View rootView;
    List<Countdown> CountdownList = new ArrayList<Countdown>();
    List<Clocks> ClockList = new ArrayList<Clocks>();
    LinearLayout layout;

    public FragmentHomeTimer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_timer, container, false);
        layout = rootView.findViewById(R.id.Card_Layout);

        setFloatingListener();

        return rootView;
    }

    private void setFloatingListener()
    {
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final DialogCreateCar dcc = new DialogCreateCar();
                dcc.AddDialog(getActivity(), view);

                setDialogCreateCarListener(dcc);
            }
        });
    }

    private void setDialogCreateCarListener(DialogCreateCar dcc)
    {
        final DialogCreateCar dialogCreateCar = dcc;
        Button add = dialogCreateCar.dialog.findViewById(R.id.Dialog_CreateCar_Button_Add);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Clocks clock = new Clocks(ClockList.size());
                clock.setCarValues(dialogCreateCar.dialog);
                ClockList.add(clock);

                CountdownList.add(new Countdown(getActivity(), dialogCreateCar.StartTime, clock.Car, ClockList.size() - 1));
                int last = CountdownList.size() - 1;
                layout.addView(CountdownList.get(last), layout.getChildCount() - 1);
                dialogCreateCar.dialog.dismiss();

                setCountdownButtonListener(CountdownList.get(last));
            }
        });
    }

    private void setDialogCreateDryerListener(DialogCreateDryer dcd, int _index)
    {
        final int index = _index;
        final DialogCreateDryer dialogDryer = dcd;
        Button add = dialogDryer.dialog.findViewById(R.id.Dialog_CreateDryer_Button_Add);

        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Countdown c = CountdownList.get(index);
                c.chrono2.setBase(SystemClock.elapsedRealtime() - (Calendar.getInstance().getTimeInMillis() - c.MidTime.getTimeInMillis()));
                c.chrono2.start();

                ClockList.get(c.ClockID).setDryer(0, dialogDryer.DryerName);

                CountdownList.get(index).secondLayout2.removeView(c.nextButton);
                CountdownList.get(index).secondLayout2.addView(c.stopButton);
                dialogDryer.dialog.dismiss();
            }
        });
    }

    private void setCountdownButtonListener(Countdown cd)
    {
        cd.nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final int index = findCountdownIndex(view.getId());

                CountdownList.get(index).MidTime = Calendar.getInstance();
                final DialogCreateDryer dialogDryer = new DialogCreateDryer();
                dialogDryer.AddDialog(getActivity(), view);

                setDialogCreateDryerListener(dialogDryer, index);
            }
        });

        cd.stopButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int index = findCountdownIndex(view.getId());
                CountdownList.get(index).EndTime = Calendar.getInstance();
                Snackbar.make(view, "Countdown at " + index + " was removed.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                layout.removeView(CountdownList.get(index));
                CountdownList.remove(index);
            }
        });
    }

    public int findCountdownIndex(int id)
    {
        for(int i=0; i < CountdownList.size(); i++)
        {
            if (CountdownList.get(i).stopButton.getId() == id)
                return i;
            else if (CountdownList.get(i).nextButton.getId() == id)
                return i;
        }
        return -1;
    }
}
