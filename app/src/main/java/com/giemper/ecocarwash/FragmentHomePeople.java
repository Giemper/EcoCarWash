package com.giemper.ecocarwash;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHomePeople extends Fragment {

    private View rootView;
    private RecyclerHomePeople adapterHomePeople;

    public FragmentHomePeople() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_home_people, container, false);

        String[] arrayHomePoeple = getResources().getStringArray(R.array.Recycler_Home_People);
        adapterHomePeople = new RecyclerHomePeople(arrayHomePoeple);
        RecyclerView recycler = (RecyclerView) rootView.findViewById(R.id.Recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapterHomePeople);

        return rootView;
    }

}
