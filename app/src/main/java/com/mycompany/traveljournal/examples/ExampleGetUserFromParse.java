package com.mycompany.traveljournal.examples;


import android.util.Log;

import com.mycompany.traveljournal.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class ExampleGetUserFromParse {

    private final static String TAG = "ExampleGetUserFromParse";

    /**
     * This is an example of how to get a User with the user's id
     */
    public static void run() {
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
