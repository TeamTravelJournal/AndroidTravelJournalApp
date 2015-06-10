package com.mycompany.traveljournal.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

@ParseClassName("User")
public class User implements Serializable{

    String name;
    String profileImgUrl;
    String covereImageUrl;

    public void User(String name, String profileImgUrl, String covereImageUrl){
        this.name = name;
        this.profileImgUrl = profileImgUrl;
        this.covereImageUrl = covereImageUrl;
    }

    public void User(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void setCovereImageUrl(String covereImageUrl) {
        this.covereImageUrl = covereImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public String getCovereImageUrl() {
        return covereImageUrl;
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
