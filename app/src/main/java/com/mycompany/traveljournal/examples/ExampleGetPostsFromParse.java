package com.mycompany.traveljournal.examples;


import android.util.Log;

import com.mycompany.traveljournal.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class ExampleGetPostsFromParse {

    private final static String TAG = "ExampleGetPostsFromParse";

    public static void getPostWithId() {

        String postId = "6KGn7knDWJ";
        Post.getPostWithId(postId, new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    Post post = posts.get(0);
                    Log.wtf(TAG, post.toString());
                } else {
                    Log.wtf(TAG, "Post not found");
                }
            }
        });
    }

    public static void getPostsNearLocation() {
        double latitude = 37.423266;
        double longitude = -122.128192;
        int limit = 10;

        Post.getPostsNearLocation(latitude, longitude, limit, new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    for (int i = 0 ; i < posts.size() ; i++) {
                        Log.wtf(TAG, posts.get(i).toString());
                    }

                } else {
                    Log.wtf(TAG, "Failed to get posts");
                }
            }

        });
    }

}
