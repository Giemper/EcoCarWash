package com.giemper.ecocarwash;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecyclerHomePeople extends RecyclerView.Adapter<RecyclerHomePeople.ViewHolder>
{
    private String[] mDataset;
    private List<Dryer> activeList;
    private ArrayList<CheckTracker> checkTracker = new ArrayList<>();
    private DryerStatus status = new DryerStatus();

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public CheckBox mCheckBox;
        public ViewHolder(View v) {
            super(v);
            mCheckBox = v.findViewById(R.id.Recycler_Checkbox);
        }
    }

    public RecyclerHomePeople(String[] myDataset)
    {
        mDataset = myDataset;
        for(int i = 0; i < mDataset.length; i++)
            checkTracker.add(new CheckTracker());
    }

    public RecyclerHomePeople(List<Dryer> myDataset)
    {
        for(int i = 0; i < myDataset.size(); i++)
        {
            checkTracker.add(new CheckTracker(myDataset.get(i)));
        }
    }

    @Override
    public RecyclerHomePeople.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_checkbox, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final CheckTracker track = checkTracker.get(position);

        holder.mCheckBox.setText(mDataset[position]);
        holder.mCheckBox.setOnCheckedChangeListener(null);
        holder.mCheckBox.setChecked(track.getCheck());
        holder.mCheckBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) ->
        {

            track.setCheck(isChecked);
            if(isChecked)
                status.addDryer(track.getDryer());
            else
                status.removeDryer(track.getDryer());

        });
    }

    @Override
    public int getItemCount()
    {
        return mDataset.length;
    }

    class CheckTracker
    {
        private boolean isChecked = false;
        private Dryer dryer;

        public CheckTracker() { }

        public CheckTracker(Dryer dry)
        {
            dryer = dry;
        }

        public void setCheck(boolean b)
        {
            isChecked = b;
        }

        public boolean getCheck()
        {
            return isChecked;
        }

        public Dryer getDryer()
        {
            return dryer;
        }
    }
}


