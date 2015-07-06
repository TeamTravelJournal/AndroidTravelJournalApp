package com.mycompany.traveljournal.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;
import com.mycompany.traveljournal.datasource.ParseClient;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.loginscreen.LoginActivity;
import com.mycompany.traveljournal.models.User;
import com.mycompany.traveljournal.profilescreen.ProfileActivity;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalService;
import com.mycompany.traveljournal.wikitude.ArchitectCamActivity;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;


public class MainActivity extends PostsListActivity {

    private final static String TAG = "MainActivity";
    MainPostFragment mainPostFragment;
    ParseClient parseClient;
    private final String MAIN_FRAGMENT_TAG = "mainPostFragment";

    private DrawerLayout mDrawer;
    protected JournalService client;
    User user;

    @Override
    public void setUpFragment() {

        mainPostFragment =  new MainPostFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, mainPostFragment, MAIN_FRAGMENT_TAG);
        ft.commit();

    }

    @Override
    public void setUpFragmentFromTag() {
        mainPostFragment = (MainPostFragment)getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
    }

    /**
     *
     * Below are code examples for fetching data models from Parse
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = JournalApplication.getClient();

        // Setup the drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);

        Log.wtf(TAG, "nvDrawer is "+nvDrawer);

        user = Util.getUserFromParseUser(ParseUser.getCurrentUser());

        setupDrawerContent(nvDrawer);


        // Uncomment to run example of fetching record from Parse
        //ExampleGetUserFromParse.run();

        //ExampleGetPostsFromParse.getPostWithId();
        //ExampleGetPostsFromParse.getPostsNearLocation();
        //ExampleGetPostsFromParse.getPostsWithinWindow();


        //ExampleSavePostToParse savePost = new ExampleSavePostToParse();
        //savePost.createPostExample();

        //ExampleComments exampleComments = new ExampleComments();
        //exampleComments.getCommentsForPost();




        // Richard - please keep, for my debugging
//        Intent i = new Intent(this, DetailActivity.class);
//        String postId = "8nxq1SkIUo";
//        Log.wtf(TAG, "adding post id "+postId);
//        i.putExtra("post_id", postId);
//        startActivity(i);



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
                .transform(Util.getTransformation(60))
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
                executeProfileIntent(client.getCurrentUser());
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


}
