package com.mycompany.traveljournal.models;


import com.mycompany.traveljournal.helpers.Util;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;


@ParseClassName("Comment")
public class Comment extends ParseObject {

    public String getCommentID() {
        return getObjectId();
    }

    public String getBody() {
        return getString("body");
    }

    public String gePostID() {
        return getString("post_id");
    }

    public User getParseUser() {
        ParseUser parseUser = (ParseUser) getParseObject("parse_user");
        return Util.getUserFromParseUser(parseUser);
    }

    public Date getCreatedAt() {
        return getDate("created_at");
    }

}
