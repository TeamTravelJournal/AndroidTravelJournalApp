package com.mycompany.traveljournal.datasource;


import com.mycompany.traveljournal.models.User;

public class SignedInUser {

    /**
     * Returns the signed in User
     */
    public static User getSignedInUser() {

        // TODO: get the actual signed in user
        return User.getFakeUser();
    }
}
