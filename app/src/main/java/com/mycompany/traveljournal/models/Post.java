package com.mycompany.traveljournal.models;

import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Post")
public class Post extends ParseObject {

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

    // This gets a Parse poiter to the created by user
    public User getCreatedByUserPointer() {
        return (User) getParseObject("created_by");
    }

    // This actually retrieves the created by user. It is async so work must be defined in the callback
    public void getUser(JournalCallBack<User> journalCallBack) {
        JournalService client = JournalApplication.getClient();
        client.getCreatedByUserFromPost(getCreatedByUserPointer(), journalCallBack);
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
