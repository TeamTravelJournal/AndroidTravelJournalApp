package com.mycompany.traveljournal.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("User")
public class User extends ParseObject {

    // I put String as I don't know fb id has alphanumeric. This covers both cases
    private String userId;

    private String name;

    private String profileImageUrl;

    public String getUserId() {
        return getString("user_id");
    }

    public String getName() {
        return getString("name");
    }

    public String getProfileImageUrl() {
        return getString("profile_image_url");
    }

    public User() {
    }

    public static User getFakeUser() {
        User user = new User();
        user.put("name", "Nathan Esquenazi");
        user.put("user_id", "101");
        user.put("profile_image_url", "https://plus.google.com/u/0/_/focus/photos/public/AIbEiAIAAABECM3zs7-msvWn8QEiC3ZjYXJkX3Bob3RvKihkMzczZjRmZTk3OWVlYTA5M2QzNzFjYWJhMGMzYWZhMGM1OTk1YjNkMAGa9EYBpMonkZGqWdV_0y7vUILd4A?sz=128");
        return user;
    }

}
