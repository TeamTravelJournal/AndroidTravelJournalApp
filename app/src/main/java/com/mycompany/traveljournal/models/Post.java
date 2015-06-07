package com.mycompany.traveljournal.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject{

    private String postID; //Parse ids are strings

    private String imageUrl;
    //private ParseFile image;

    private String caption;
    private String description;
    private ParseGeoPoint location;
    private String userID;
    private int likes;
    private String tripID; //Parse Ids are strings

    //We will not need timeStamp
    //as Parse object has built-in createdAt field


    // The post id is the Parse object id and will be auto generated
    public String getPostID() {
        return getObjectId();
    }

    public String getCaption() {
        return getString("caption");
    }

    public String getDescription() {
        return getString("description");
    }

    public double getLatitude() {
        ParseGeoPoint location = (ParseGeoPoint) get("location");
        if(location!=null){
            return location.getLatitude();
        }
        return 0.0d;
    }

    public double getLongitude() {
        ParseGeoPoint location = (ParseGeoPoint) get("location");
        if(location!=null) {
            return location.getLongitude();
        }
        return 0.0d;
    }

    public String getUserID() {
        return getString("user_id");
    }

    public int getLikes() {
        return getInt("likes");
    }

    public String getTripID() {
        return getString("trip_id");
    }

    public String getImageUrl() {
        return getString("image_url");
    }

    public Post() {

    }

    public static ArrayList<Post> getFakePosts() {
        ArrayList<Post> posts = new ArrayList<>();

        Post post1 = new Post();
        post1.put("caption", "The Church in San Francisco");
        post1.put("description", "");

        ParseGeoPoint location = new ParseGeoPoint();
        location.setLatitude(37.764830);
        location.setLongitude(-122.432080);
        post1.put("location", location);

        post1.put("user_id", "101");
        post1.put("likes", "18");
        post1.put("trip_id", 0);
        posts.add(post1);


        Post post2 = new Post();
        post2.put("caption", "An Awesome cafe I found!");
        post2.put("description", "");

        ParseGeoPoint location2 = new ParseGeoPoint();
        location2.setLatitude(37.764579);
        location2.setLongitude(-122.433104);
        post1.put("location", location2);

        post2.put("user_id", "101");
        post2.put("likes", "3");
        post2.put("trip_id", 0);
        posts.add(post2);

        return posts;
    }

    public static void getPostWithId(String postId, FindCallback callback) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("objectId", postId);
        query.setLimit(1);
        query.findInBackground(callback);
    }

    public static void getPostsNearLocation(double latitude, double longitude, int limit, FindCallback callback) {
        ParseGeoPoint userLocation = new ParseGeoPoint(latitude, longitude);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereNear("location", userLocation);
        query.setLimit(limit);
        query.findInBackground(callback);
    }

    public static void getPostsWithinWindow(double latitudeMin, double longitudeMin, double latitudeMax, double longitudeMax, int limit, FindCallback callback) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setLimit(limit);
        ParseGeoPoint swPoint = new ParseGeoPoint(latitudeMin, longitudeMin);
        ParseGeoPoint nePoint = new ParseGeoPoint(latitudeMax, longitudeMax);
        query.whereWithinGeoBox("location", swPoint, nePoint);
        query.findInBackground(callback);
    }

    public static void getRecentPosts(Date createdAt, int limit, FindCallback callback) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        if(createdAt!=null){
            //if we pass a timestamp, query the posts older than timestamp
            //otherwise query everything sorted by timestamp
            query.whereLessThan("createdAt", createdAt);
        }
        query.orderByDescending("createdAt");
        query.setLimit(limit);
        query.findInBackground(callback);
    }

    public static void getPostsNearLocationOrderByDate(Date createdAt, double latitude, double longitude, int limit, FindCallback callback) {
        ParseGeoPoint userLocation = new ParseGeoPoint(latitude, longitude);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereNear("location", userLocation);
        if(createdAt!=null){
            //if we pass a timestamp, query the posts older than timestamp
            //otherwise query everything sorted by timestamp
            query.whereLessThan("createdAt", createdAt);
        }
        query.orderByDescending("createdAt");
        query.setLimit(limit);
        query.findInBackground(callback);
    }

    public static void getPostsWithinMilesOrderByDate(Date createdAt, int maxDistance, double latitude, double longitude, int limit, FindCallback callback) {
        ParseGeoPoint userLocation = new ParseGeoPoint(latitude, longitude);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereWithinMiles("location", userLocation, maxDistance);
        if(createdAt!=null){
            //if we pass a timestamp, query the posts older than timestamp
            //otherwise query everything sorted by timestamp
            query.whereLessThan("createdAt", createdAt);
        }
        query.orderByDescending("createdAt");
        query.setLimit(limit);
        query.findInBackground(callback);
    }

    // This is just for debugging purposes
    @Override
    public String toString() {
        String output = "";
        output += getCaption() + " ";
        output += getDescription() + " ";
        output += getUserID() + " ";
        output += getLikes() + " ";
        output += getTripID() + " ";
        output += getLatitude() + " ";
        output += getLongitude()+ " ";
        output += getImageUrl() + " ";
        return output;
    }



}
