package com.mycompany.traveljournal.datasource;


import android.content.Context;

import com.mycompany.traveljournal.models.Like;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseClient {

    public ParseClient() {

    }

    public void init(Context context) {

        // Enable Local Datastore
        Parse.enableLocalDatastore(context);

        // Register Parse sublasses
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Like.class);

        Parse.initialize(context, "ZFoSsZ6iQBe1CvJaNqio6V0nmlN4V7U4VzboX4J4", "0GDxAZahVe7ibC6pqiMNK6n91fYoh7HRfxXLo5TK");
    }



}
