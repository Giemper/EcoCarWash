package com.giemper.ecocarwash;

import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ViewPager viewHome = findViewById(R.id.view_home);
        final FragmentHome fragmentHome = new FragmentHome(this, getSupportFragmentManager(), toolbar);
        viewHome.setAdapter(fragmentHome);
        viewHome.setOffscreenPageLimit(fragmentHome.getCount());

        TabLayout homeTab = findViewById(R.id.tab_home);
        homeTab.setupWithViewPager(viewHome);
        homeTab.getTabAt(0).setIcon(R.drawable.ic_timer);
        homeTab.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        homeTab.getTabAt(1).setIcon(R.drawable.ic_people);
        homeTab.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight), PorterDuff.Mode.SRC_IN);
        homeTab.getTabAt(2).setIcon(R.drawable.ic_format_list);
        homeTab.getTabAt(2).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight), PorterDuff.Mode.SRC_IN);

        homeTab.setTabGravity(TabLayout.GRAVITY_FILL);

        homeTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){}
        });
    }
}