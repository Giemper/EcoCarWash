package com.giemper.ecocarwash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FragmentHome extends FragmentPagerAdapter {

    public Context mContext;
    public FragmentHomeTimer Home_Timer;
    public FragmentHomePeople Home_People;
    public FragmentHomeRegistry Home_Registry;
    private Toolbar toolbar;
    private String ecoUserType;

    public FragmentHome(Context context, FragmentManager fm, Toolbar tool)
    {
        super(fm);
        mContext = context;
        DatabaseReference ecoDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser ecoUser = FirebaseAuth.getInstance().getCurrentUser();

        Home_Timer = new FragmentHomeTimer();
        Home_Timer.setFirebase(ecoDatabase, ecoUser, "Supervisor");

        Home_People = new FragmentHomePeople();
        Home_People.setFirebase(ecoDatabase, ecoUser, "Supervisor");

        Home_Registry = new FragmentHomeRegistry();
        Home_Registry.setFirebase(ecoDatabase, ecoUser, "Supervisor");

        toolbar = tool;

        setRoles(ecoDatabase, ecoUser);
    }

    private void setRoles(DatabaseReference ecoDatabase, FirebaseUser ecoUser)
    {
        Query q = ecoDatabase.child("Users").orderByKey().equalTo(ecoUser.getUid());
        q.keepSynced(true);
        q.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override public void onCancelled(@NonNull DatabaseError databaseError){}
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                long s = dataSnapshot.getChildrenCount();
                ecoUserType = dataSnapshot.child(ecoUser.getUid()).getValue(String.class);

                if(ecoUserType.equals("admin"))
                {
                    Home_Timer.setFloatingListener();
                    Home_Timer.setCountdownAccess();
                }
                else if(ecoUserType.equals("cashier"))
                {
                    Home_Timer.setFloatingListener();

                    Home_People.setFloatingListener();
                }
                else if(ecoUserType.equals("supervisor"))
                {
                    Home_Timer.setCountdownAccess();
                }
            }
        });
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

}