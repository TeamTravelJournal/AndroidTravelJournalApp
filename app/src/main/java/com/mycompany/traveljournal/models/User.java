package com.mycompany.traveljournal.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

@ParseClassName("User")
public class User implements Serializable{

    String name;
    String profileImgUrl;
    String coverImageUrl;
    String id;
    Boolean isFollowed = false;

    public void User(String name, String profileImgUrl, String covereImageUrl, String id){
        this.name = name;
        this.profileImgUrl = profileImgUrl;
        this.coverImageUrl = covereImageUrl;
        this.id = id;
    }

    public void User(){

    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public Boolean getIsFollowed() {
        return isFollowed;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void setCovereImageUrl(String covereImageUrl) {
        this.coverImageUrl = covereImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    // This is just for debugging purposes
    @Override
    public String toString() {
        String output = "";
        output += getName() + " ";
        output += getProfileImgUrl() + " ";
        return output;
    }

}
