package com.mycompany.traveljournal.models;


import com.mycompany.traveljournal.helpers.Util;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;


@ParseClassName("Comment")
public class Comment extends ParseObject {

    public Comment() {

    }

    public String getCommentID() {
        return getObjectId();
    }

    public String getBody() {
        return getString("body");
    }

    public String getPostID() {
        return getString("post_id");
    }

    public User getUser() {
        ParseUser parseUser = (ParseUser) getParseObject("parse_user");
        return Util.getUserFromParseUser(parseUser);
    }

    public Date getCreatedAt() {
        return getDate("created_at");
    }

    public String toString() {
        String output = "";
        output += getCommentID() + "\t";
        output += getPostID() + "\t";
        output += getUser().getName() + "\t";
        output += getBody();
        return output;
    }

}
