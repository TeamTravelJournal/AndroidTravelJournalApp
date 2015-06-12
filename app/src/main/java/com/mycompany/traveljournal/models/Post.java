package com.mycompany.traveljournal.models;

import com.google.android.gms.appindexing.Thing;
import com.mycompany.traveljournal.helpers.Util;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("Post")
public class Post extends ParseObject {

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

    public int getLikes() {
        return getInt("likes");
    }

    public String getTripID() {
        return getString("trip_id");
    }

    public String getImageUrl() {
        return getString("image_url");
    }

    /*public User getCreatedByUser() {
        return (User) getParseObject("created_by");
    }*/

    public User getParseUser() {
        ParseUser parseUser = (ParseUser) getParseObject("parse_user");
        return Util.getUserFromParseUser(parseUser);
    }

    public int getNumComments() {
        return getInt("num_comments");
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
        output += getLikes() + " ";
        output += getTripID() + " ";
        output += getLatitude() + " ";
        output += getLongitude()+ " ";
        output += getImageUrl() + " ";
        return output;
    }

    public boolean equals (Object o) {
        Post second = (Post) o;
        if (second.getPostID().equals(getPostID()))
            return true;
        return false;
    }


}
