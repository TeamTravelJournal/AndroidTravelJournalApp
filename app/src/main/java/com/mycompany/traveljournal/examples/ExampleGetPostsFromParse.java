package com.mycompany.traveljournal.examples;


import android.util.Log;

import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;

import java.util.List;

public class ExampleGetPostsFromParse {

    private final static String TAG = "ExampleGetPostsFromParse";

    public static void getPostWithId() {

        String postId = "pD7HdQPTSg";
        JournalService client = JournalApplication.getClient();
        client.getPostWithId(postId, new JournalCallBack<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                Post post = posts.get(0);
                Log.wtf(TAG, post.toString());

                post.getUser(new JournalCallBack<User>() {
                    @Override
                    public void onSuccess(User user) {
                        Log.wtf(TAG, user.toString());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.wtf(TAG, "Failed to get user");
                    }
                });

            }
            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Post not found");
            }
        });
    }

    public static void getPostsNearLocation() {
        double latitude = 37.423266;
        double longitude = -122.128192;
        int limit = 10;

        JournalService client = JournalApplication.getClient();
        client.getPostsNearLocation(latitude, longitude, limit, new JournalCallBack<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                for (int i = 0; i < posts.size(); i++) {
                    Log.wtf(TAG, posts.get(i).toString());
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Failed to get posts");
            }
        });
    }

    public static void getPostsWithinWindow() {
        double latitudeMin = 37.403423;
        double longitudeMin = -122.158368;
        double latitudeMax = 37.453218;
        double longitudeMax = -122.101029;
        int limit = 10;

        JournalService client = JournalApplication.getClient();
        client.getPostsWithinWindow(latitudeMin, longitudeMin, latitudeMax, longitudeMax, limit, new JournalCallBack<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                for (int i = 0; i < posts.size(); i++) {
                    Log.wtf(TAG, posts.get(i).toString());
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Failed to get posts");
            }
        });
    }

}
