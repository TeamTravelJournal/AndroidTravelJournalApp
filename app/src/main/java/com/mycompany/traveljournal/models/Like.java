package com.mycompany.traveljournal.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Like")
public class Like extends ParseObject {

    private String userID;

    private int postID;

    public String getUserId() {
        return getString("user_id");
    }

    public String getPostId() {
        return getString("post_id");
    }

    public Like() {
        
    }

}
