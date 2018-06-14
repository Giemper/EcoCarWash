package com.giemper.ecocarwash;

import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static com.giemper.ecocarwash.CarMethods.checkFirebase;

public class MainActivity extends AppCompatActivity
{

    private TabLayout homeTab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        checkFirebase();

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
                if(position == 0)
                    toolbar.setTitle("Cronometros");
                else if(position == 1)
                    toolbar.setTitle("Secadores");
                else if(position == 2)
                    toolbar.setTitle("Reportes");
                else
                    toolbar.setTitle("Eco Car Wash");
            }
        });

        homeTab = findViewById(R.id.tab_home);
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
            @Override public void onTabReselected(TabLayout.Tab tab){}
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
        });
    }

    @Override
    public void onBackPressed()
    {
        if(homeTab.getSelectedTabPosition() != 0)
        {
            homeTab.getTabAt(0).select();
        }
        else
        {
            DialogLogout dialogLogout = new DialogLogout();
            dialogLogout.AddDialog(this);
            dialogLogout.setListeners();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_logout)
        {
            DialogLogout dialogLogout = new DialogLogout();
            dialogLogout.AddDialog(this);
            dialogLogout.setListeners();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}