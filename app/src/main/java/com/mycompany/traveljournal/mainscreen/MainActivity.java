package com.mycompany.traveljournal.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;
import com.mycompany.traveljournal.createscreen.CreatePostFragment;
import com.mycompany.traveljournal.datasource.ParseClient;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.mycompany.traveljournal.profilescreen.ProfileActivity;
import com.parse.ParseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends PostsListActivity {

    private final static String TAG = "MainActivity";
    MainPostFragment mainPostFragment;
    ParseClient parseClient;
    private final String MAIN_FRAGMENT_TAG = "mainPostFragment";

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


}
