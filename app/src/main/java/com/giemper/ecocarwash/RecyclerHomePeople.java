package com.giemper.ecocarwash;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by gmoma on 4/20/2018.
 */

public class RecyclerHomePeople extends RecyclerView.Adapter<RecyclerHomePeople.ViewHolder>
{
    private String[] mDataset;
    private ArrayList<CheckTracker> checkTracker = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CheckBox mCheckBox;
        public ViewHolder(View v) {
            super(v);
            mCheckBox = v.findViewById(R.id.Recycler_Checkbox);
        }
    }

    public RecyclerHomePeople(String[] myDataset) {
        mDataset = myDataset;
        for(int i = 0; i < mDataset.length; i++)
            checkTracker.add(new CheckTracker());
    }

    @Override
    public RecyclerHomePeople.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_checkbox, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CheckTracker track = checkTracker.get(position);

        holder.mCheckBox.setText(mDataset[position]);
        holder.mCheckBox.setOnCheckedChangeListener(null);
        holder.mCheckBox.setChecked(track.getCheck());
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                track.setCheck(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    class CheckTracker
    {
        private boolean isChecked;

        public CheckTracker()
        {
            isChecked = false;
        }

        public void setCheck(boolean b)
        {
            isChecked = b;
        }

        public boolean getCheck()
        {
            return isChecked;
        }
    }
}


