package com.mycompany.traveljournal.mainscreen;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;
import com.mycompany.traveljournal.datasource.ParseClient;
import com.mycompany.traveljournal.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;


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
        //runExmaples();
    }

    // Examples for fetching data from Parse
    private void runExmaples() {
        exampleGetUser();

    }

    /**
     * This is an example of how to get a User with the user's id
     */
    private void exampleGetUser() {
        String userId = "1";
        User.getUserWithId(userId, new FindCallback<User>() {
            public void done(List<User> users, ParseException e) {
                if (e == null) {
                    User user = users.get(0);
                    Log.wtf(TAG, user.getName());
                    Log.wtf(TAG, user.getUserId());
                    Log.wtf(TAG, user.getProfileImageUrl());
                } else {
                    Log.wtf(TAG, "User not found");
                }
            }
        });
    }
}
