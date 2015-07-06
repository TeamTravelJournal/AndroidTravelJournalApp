package com.mycompany.traveljournal.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;
import com.mycompany.traveljournal.datasource.ParseClient;
import com.mycompany.traveljournal.loginscreen.LoginActivity;
import com.mycompany.traveljournal.models.User;
import com.mycompany.traveljournal.profilescreen.ProfileActivity;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalService;
import com.mycompany.traveljournal.wikitude.ArchitectCamActivity;
import com.parse.ParseUser;


public class MainActivity extends PostsListActivity {

    private final static String TAG = "MainActivity";
    MainPostFragment mainPostFragment;
    ParseClient parseClient;
    private final String MAIN_FRAGMENT_TAG = "mainPostFragment";

    private DrawerLayout mDrawer;
    protected JournalService client;

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
