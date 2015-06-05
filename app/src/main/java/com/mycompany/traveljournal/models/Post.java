package com.mycompany.traveljournal.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Post")
public class Post extends ParseObject{

    private int postID;

// Photo will be a parse file - uncomment out line below
    //private ParseFile photoUrl;

    private String caption;
    private String description;
    private String latitude;
    private String longitude;
    private String userID;
    private int likes;
    private int tripID;

    //We will not need timeStamp
    //as Parse object has built-in createdAt field

    public int getPostID() {
        return getInt("post_id");
    }

    public String getCaption() {
        return getString("caption");
    }

    public String getDescription() {
        return getString("description");
    }

    public String getLatitude() {
        return getString("latitude");
    }

    public String getLongitude() {
        return getString("longitude");
    }

    public String getUserID() {
        return getString("user_id");
    }

    public int getLikes() {
        return getInt("likes");
    }

    public long getTripID() {
        return getInt("trip_id");
    }

    public Post() {

    }

    public static ArrayList<Post> getFakePosts() {
        ArrayList<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.put("post_id", 1);
        post1.put("caption", "The Church in San Francisco");
        post1.put("description", "");
        post1.put("latitude", "37.764830");
        post1.put("longitude", "-122.432080");
        post1.put("user_id", "101");
        post1.put("likes", "18");
        post1.put("trip_id", 0);
        posts.add(post1);

        Post post2 = new Post();
        post2.put("post_id", 2);
        post2.put("caption", "An Awesome cafe I found!");
        post2.put("description", "");
        post2.put("latitude", "37.764579");
        post2.put("longitude", "-122.433104");
        post2.put("user_id", "101");
        post2.put("likes", "3");
        post2.put("trip_id", 0);
        posts.add(post2);

        return posts;
    }
}
