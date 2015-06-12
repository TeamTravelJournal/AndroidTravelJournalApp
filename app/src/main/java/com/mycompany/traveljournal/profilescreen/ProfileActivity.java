package com.mycompany.traveljournal.profilescreen;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.datasource.ParseClient;
import com.mycompany.traveljournal.models.User;

public class ProfileActivity extends ActionBarActivity {

    UserPostsFragment userPostsFragment;
    User user;
    private TextView tvName;
    private Toolbar toolbarForProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = (User) getIntent().getSerializableExtra("User");
        tvName = (TextView) findViewById(R.id.tvUserName);
        toolbarForProfile = (Toolbar) findViewById(R.id.toolbar1);
        setToolbar();

        UserProfileFragment userProfileFragment = (UserProfileFragment)getSupportFragmentManager().findFragmentById(R.id.userFragment);
        userProfileFragment.setData(user);

        if(savedInstanceState == null)
            setUpFragment();
    }

    private void setToolbar() {
        if (toolbarForProfile != null) {
            tvName.setText(user.getName());
            setSupportActionBar(toolbarForProfile);
            //toolbar.setBackgroundColor(Color.parseColor(user.getProfileBackgroundColor()));

            // Set the home icon on toolbar
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_up_menu);
        }
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
