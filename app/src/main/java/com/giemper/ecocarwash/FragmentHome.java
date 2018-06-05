package com.giemper.ecocarwash;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentHome extends FragmentPagerAdapter {

    public Context mContext;
    public FragmentHomeTimer Home_Timer;
    public FragmentHomePeople Home_People;
    public FragmentHomeRegistry Home_Registry;
    private Toolbar toolbar;

    public FragmentHome(Context context, FragmentManager fm, Toolbar tool)
    {
        super(fm);
        mContext = context;
        DatabaseReference ecoDatabase = FirebaseDatabase.getInstance().getReference();

        Home_Timer = new FragmentHomeTimer();
        Home_Timer.setFirebase(ecoDatabase);

        Home_People = new FragmentHomePeople();
        Home_People.setFirebase(ecoDatabase);

        Home_Registry = new FragmentHomeRegistry();
        Home_Registry.setFirebase(ecoDatabase);

        toolbar = tool;
    }

    @Override
    public Fragment getItem(int position)
    {
        if (position == 0)
            return Home_Timer;
        else if (position == 1)
            return Home_People;
        else
            return Home_Registry;
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    // This determines the title for each tab
//    @Override
//    public CharSequence getPageTitle(int position)
//    {
//        // Generate title based on item position
//
//        switch (position) {
//            case 0:
//                return mContext.getString(R.string.Home_Tab1);
//            case 1:
//                return mContext.getString(R.string.Home_Tab2);
//            case 2:
//                return mContext.getString(R.string.Home_Tab3);
//            default:
//                return null;
//        }
//    }


}