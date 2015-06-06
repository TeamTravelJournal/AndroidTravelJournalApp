package com.mycompany.traveljournal.examples;


import android.util.Log;

import com.mycompany.traveljournal.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class ExampleGetPostsFromParse {

    private final static String TAG = "ExampleGetPostsFromParse";

    public static void getPostWithId(String postId) {

        Post.getPostWithId(postId, new FindCallback<Post>() {
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    Post post = posts.get(0);
                    Log.wtf(TAG, post.getCaption());
                    Log.wtf(TAG, post.getDescription());
                    Log.wtf(TAG, post.getUserID());
                    Log.wtf(TAG, post.getLikes()+"");
                    Log.wtf(TAG, post.getTripID());
                    Log.wtf(TAG, Double.toString(post.getLatitude()));
                    Log.wtf(TAG, Double.toString(post.getLongitude()));
                } else {
                    Log.wtf(TAG, "Post not found");
                }
            }
        });
    }
}
