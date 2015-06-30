package com.mycompany.traveljournal.examples;


import android.util.Log;

import com.mycompany.traveljournal.models.User;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;

import java.util.List;

public class ExampleGetUserFromParse {

    private final static String TAG = "ExampleGetUserFromParse";

    /**
     * This is an example of how to get a User with the user's id
     */
    public static void run() {
        String userId = "1";
        JournalService client = JournalApplication.getClient();
        /*client.getUserWithId(userId, new JournalCallBack<List<User>>() {
            public void onSuccess(List<User> users) {
                User user = users.get(0);
                Log.wtf(TAG, user.getName());
                //Log.wtf(TAG, user.getUserId());
                //Log.wtf(TAG, user.getProfileImageUrl());
            }
            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "User not found");
            }
        });*/
    }
}
