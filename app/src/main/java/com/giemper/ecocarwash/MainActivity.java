package com.giemper.ecocarwash;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        setSupportActionBar(toolbar);


        ViewPager viewHome = findViewById(R.id.view_home);
        final FragmentHome fragmentHome = new FragmentHome(this, getSupportFragmentManager(), toolbar);
        viewHome.setAdapter(fragmentHome);
        viewHome.setOffscreenPageLimit(fragmentHome.getCount());
        viewHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){}
            @Override public void onPageScrollStateChanged(int state){}
            @Override
            public void onPageSelected(int position)
            {
                int oldColor = toolbar.getSolidColor();
                int newColor;
                switch (position)
                {
                    case 0:
                        toolbar.setTitle("Cronometros");
                        newColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
                        break;
                    case 1:
                        toolbar.setTitle("Secadores");
                        newColor = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent2);
                        break;
                    case 2:
                        toolbar.setTitle("Reportes");
                        newColor = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent3);
                        break;
                    default:
                        toolbar.setTitle("Eco Car Wash");
                        newColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
                        break;
                }

                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), oldColor, newColor);
                colorAnimation.setDuration(250);
                colorAnimation.addUpdateListener((ValueAnimator animator) ->
                {
                    toolbar.setBackgroundColor((int) animator.getAnimatedValue());
                });
                colorAnimation.start();
            }
        });

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