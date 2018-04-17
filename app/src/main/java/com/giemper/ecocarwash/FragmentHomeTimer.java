package com.giemper.ecocarwash;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomeTimer extends Fragment {

    private View rootView;
    List<Countdown> CountdownList = new ArrayList<Countdown>();
    LinearLayout layout;



    public FragmentHomeTimer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_timer, container, false);
        layout = rootView.findViewById(R.id.Card_Layout);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogCreateCar dcc = new DialogCreateCar();
                dcc.AddDialog(getActivity(), view);
                Button add = dcc.dialog.findViewById(R.id.Dialog_Button_Add);
                add.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        CreateCarValue values = dcc.getCarValues(dcc.dialog);
                        if(values.Package >= 0 && values.Size >= 0 && values.License != "")
                        {
                            CountdownList.add(new Countdown(getActivity()));
                            int last = CountdownList.size() - 1;
                            layout.addView(CountdownList.get(last), layout.getChildCount() - 1);

                            CountdownList.get(last).stopButton.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view)
                                {
                                    int index = findCountdownIndex(view.getId());
                                    Snackbar.make(view, "Countdown at " + index + " was removed.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    layout.removeView(CountdownList.get(index));
                                    CountdownList.remove(index);
                                }
                            });

                            dcc.dialog.dismiss();
                        }
                    }
                });

//                CountdownList.add(new Countdown(getActivity()));
//                int last = CountdownList.size() - 1;
//                layout.addView(CountdownList.get(last), layout.getChildCount() - 1);
//
//                CountdownList.get(last).stopButton.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View view)
//                    {
//                        int index = findCountdownIndex(view.getId());
//                        Snackbar.make(view, "Countdown at " + index + " was removed.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
////                        CountdownList.get(index).Timer.stop();
//                        layout.removeView(CountdownList.get(index));
//                        CountdownList.remove(index);
//                    }
//                });


            }
        });

        return rootView;
    }

    public int findCountdownIndex(int id)
    {
        for(int i=0; i < CountdownList.size(); i++)
            if(CountdownList.get(i).stopButton.getId() == id)
                return i;
        return -1;
    }
}
