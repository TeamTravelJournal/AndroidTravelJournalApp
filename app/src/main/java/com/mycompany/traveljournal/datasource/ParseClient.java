package com.mycompany.traveljournal.datasource;


import android.content.Context;

import com.mycompany.traveljournal.models.Like;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseClient {

    private static final String TAG = "ParseClient";
    private static ParseClient instance = null;

    protected ParseClient() {

    }

    /**
     *  Get a the singleton ParseClient instance
     */
    public static ParseClient getInstance(Context context) {
        if (instance == null) {
            instance = new ParseClient();

            // Automatically initialize
            instance.init(context);
        }
        return instance;
    }

    public void init(Context context) {

        // Enable Local Datastore
        Parse.enableLocalDatastore(context);

        // Register Parse sublasses
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Like.class);

        Parse.initialize(context, "ZFoSsZ6iQBe1CvJaNqio6V0nmlN4V7U4VzboX4J4", "0GDxAZahVe7ibC6pqiMNK6n91fYoh7HRfxXLo5TK");
    }


    public static void createPost(byte[] imageBytes, String caption, String description, String userId, double latitude, double longitude) {
        // Save post w/o image

        // Save image with postId

    }






}
