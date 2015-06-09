package com.mycompany.traveljournal.examples;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {

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
        user.put("profile_image_url", "https://plus.google.com/u/0/_/focus/photos/public/AIbEiAIAAABECM3zs7-msvWn8QEiC3ZjYXJkX3Bob3RvKihkMzczZjRmZTk3OWVlYTA5M2QzNzFjYWJhMGMzYWZhMGM1OTk1YjNkMAGa9EYBpMonkZGqWdV_0y7vUILd4A?sz=128");
        return user;
    }

    // This is just for debugging purposes
    @Override
    public String toString() {
        String output = "";
        output += getName() + " ";
        output += getProfileImageUrl() + " ";
        return output;
    }

}
