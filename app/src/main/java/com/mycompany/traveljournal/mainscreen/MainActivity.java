package com.mycompany.traveljournal.mainscreen;

import android.support.v4.app.FragmentTransaction;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;


public class MainActivity extends PostsListActivity {

    MainPostFragment mainPostFragment;

    @Override
    public void setUpFragment() {

        mainPostFragment =  new MainPostFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, mainPostFragment);
        ft.commit();

    }
}
