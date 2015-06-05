package com.mycompany.traveljournal.datasource;


import android.content.Context;

import com.parse.Parse;

public class ParseDatasource {

    public void init(Context context) {
        // Enable Local Datastore
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "ZFoSsZ6iQBe1CvJaNqio6V0nmlN4V7U4VzboX4J4", "0GDxAZahVe7ibC6pqiMNK6n91fYoh7HRfxXLo5TK");
    }

}
