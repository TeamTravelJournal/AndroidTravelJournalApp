package com.mycompany.traveljournal.models;

import com.mycompany.traveljournal.helpers.Util;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {

    String profileImg;
    String userId;
    String body;

    public User getParseUser() {
        ParseUser parseUser = (ParseUser) getParseObject("parse_user");
        return Util.getUserFromParseUser(parseUser);
    }

    public String getProfileImg(){
        return this.profileImg == null ? getParseUser().getProfileImgUrl() : this.profileImg;
    }

    public String getUserId() {
        return this.userId == null ? getString("userId") : this.userId;
    }

    public String getBody() {
        return this.body == null ? getString("body") : this.body;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setProfileImg(String profileImg){
        this.profileImg = profileImg;
    }

}
