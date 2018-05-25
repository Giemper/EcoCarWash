package com.giemper.ecocarwash;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by gmoma on 2/5/2018.
 */

public class FragmentHome extends FragmentPagerAdapter
{

    public Context mContext;
    public FragmentHomeTimer Home_Timer;
    public FragmentHomePeople Home_People;
    public FragmentHomeRegistry Home_Registry;

    public FragmentHome(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        Home_Timer = new FragmentHomeTimer();
        Home_People = new FragmentHomePeople();
        Home_Registry = new FragmentHomeRegistry();
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position)
    {
        if (position == 0) {
            return Home_Timer;
        } else if (position == 1){
            return Home_People;
        } else {
            return Home_Registry;
        }
    }

    // This determines the number of tabs
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