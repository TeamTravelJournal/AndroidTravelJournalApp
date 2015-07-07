package com.mycompany.traveljournal.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.datasource.ParseClient;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.loginscreen.LoginActivity;
import com.mycompany.traveljournal.models.User;
import com.mycompany.traveljournal.profilescreen.ProfileActivity;
import com.mycompany.traveljournal.wikitude.ArchitectCamActivity;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public abstract class PostsListActivity extends ActionBarActivity {

    private final static String TAG = "PostsListActivity";
    ParseClient parseClient;
    protected DrawerLayout mDrawer;
    protected User user;
    protected NavigationView nvDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Init Parse
        parseClient = ParseClient.getInstance(this);

        if(savedInstanceState == null)
            setUpFragment();
        else
            setUpFragmentFromTag();

        // Setup the drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        Log.wtf(TAG, "nvDrawer is " + nvDrawer);
        user = Util.getUserFromParseUser(ParseUser.getCurrentUser());
        setupDrawerContent(nvDrawer);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        View header = getLayoutInflater().inflate(R.layout.drawer_header, null);
        ImageView ivProfile = (ImageView)header.findViewById(R.id.ivProfile);
        TextView tvName = (TextView)header.findViewById(R.id.name);
        TextView tvEmail = (TextView)header.findViewById(R.id.email);

        Picasso.with(this).load(user.getProfileImgUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholderthumbnail)
                .transform(Util.getNoBorderTransformation(60))
                .into(ivProfile);

        tvName.setText(user.getName());
        tvEmail.setText("Richard.Pon@gmail.com");

        navigationView.addHeaderView(header);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return false;
                    }
                }
        );

    }

    private void selectDrawerItem(MenuItem menuItem) {

        // Profile
        switch(menuItem.getItemId()) {
            case R.id.nav_profile:
                executeProfileIntent(parseClient.getCurrentUser());
                break;
            case R.id.nav_ar:
                Intent i2 = new Intent(this, ArchitectCamActivity.class);
                this.startActivity(i2);
                break;
            case R.id.nav_logout:
                ParseUser.logOut();
                Intent i3 = new Intent(this, LoginActivity.class);
                this.startActivity(i3);
                break;
        }

        mDrawer.closeDrawers();
    }

    protected void executeProfileIntent(User data){
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("User", data);
        this.startActivity(i);
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public abstract void setUpFragmentFromTag();

    public abstract void setUpFragment();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
