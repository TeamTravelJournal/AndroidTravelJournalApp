package com.mycompany.traveljournal.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.datasource.ParseClient;
import com.mycompany.traveljournal.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public abstract class PostsListActivity extends ActionBarActivity {

    private final static String TAG = "PostsListActivity";
    ParseClient parseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Init Parse
        parseClient = ParseClient.getInstance(this);

        // Uncomment to run example of fetching record from Parse
        //runExmaples();

        if(savedInstanceState == null)
            setUpFragment();
    }

    public abstract void setUpFragment();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
