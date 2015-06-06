package com.mycompany.traveljournal.examples;


import android.util.Log;

import com.mycompany.traveljournal.models.Post;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

public class ExampleSavePostToParse {

    private final static String TAG = "ExampleSavePostToParse";

    public static void savePost() {
        Post post = new Post();
        post.put("caption", "Shopping @Stanford");
        post.put("description", "");

        ParseGeoPoint location = new ParseGeoPoint();
        location.setLatitude(37.438230);
        location.setLongitude(-122.173328);
        post.put("location", location);

        post.put("user_id", "1");
        post.put("likes", 6);
        post.put("trip_id", 0);


        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.wtf(TAG, "Post saved");
                } else {
                    Log.wtf(TAG, "Failed to save post"+e.toString());
                }
            }
        });

    }

}
