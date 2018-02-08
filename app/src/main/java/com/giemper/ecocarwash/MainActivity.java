package com.giemper.ecocarwash;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewHome = findViewById(R.id.view_home);
        final FragmentHome fragmentHome = new FragmentHome(this, getSupportFragmentManager());
        viewHome.setAdapter(fragmentHome);
        viewHome.setOffscreenPageLimit(fragmentHome.getCount());

        TabLayout homeTab = findViewById(R.id.tab_home);
        homeTab.setupWithViewPager(viewHome);
        homeTab.getTabAt(0).setIcon(R.drawable.ic_timer);
        homeTab.getTabAt(1).setIcon(R.drawable.ic_people);
        homeTab.getTabAt(2).setIcon(R.drawable.ic_format_list);
        homeTab.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



