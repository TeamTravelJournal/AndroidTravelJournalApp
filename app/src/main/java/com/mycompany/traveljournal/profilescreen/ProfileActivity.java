package com.mycompany.traveljournal.profilescreen;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.datasource.ParseClient;
import com.mycompany.traveljournal.models.User;

public class ProfileActivity extends ActionBarActivity {

    UserPostsFragment userPostsFragment;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = (User) getIntent().getSerializableExtra("User");

        UserProfileFragment userProfileFragment = (UserProfileFragment)getSupportFragmentManager().findFragmentById(R.id.userFragment);
        userProfileFragment.setData(user);
        getSupportActionBar().hide();

        if(savedInstanceState == null)
            setUpFragment();
    }


    public void setUpFragment() {

        userPostsFragment =  UserPostsFragment.newInstance(user.getId());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, userPostsFragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }
}
