package com.mycompany.traveljournal.mainscreen;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;
import com.mycompany.traveljournal.datasource.ParseClient;


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
