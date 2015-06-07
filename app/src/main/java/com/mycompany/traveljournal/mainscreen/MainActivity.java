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

    @Override
    public void setUpFragment() {

        mainPostFragment =  new MainPostFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, mainPostFragment);
        ft.commit();

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


//        ExampleSavePostToParse savePost = new ExampleSavePostToParse();
//        savePost.createPostExample();








        // Richard - please keep, for my debugging
//        Intent i = new Intent(this, DetailActivity.class);
//        String postId = "8nxq1SkIUo";
//        Log.wtf(TAG, "adding post id "+postId);
//        i.putExtra("post_id", postId);
//        startActivity(i);
    }

}
