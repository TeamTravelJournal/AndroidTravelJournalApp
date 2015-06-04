package com.mycompany.traveljournal.models;

/**
 * Created by ekucukog on 6/3/2015.
 */
public class Post {

    private long postID;

    // Photo will be a parse file - uncomment out line below
    //private ParseFile photoUrl;

    private String caption;

    private String description;

    private double latitude;

    private double longitude;

    private String userID;

    private int likes;

    private long tripID;

    //We will not need timeStamp
    //as Parse object has built-in createdAt field

}
