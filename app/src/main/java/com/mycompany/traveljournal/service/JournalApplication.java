package com.mycompany.traveljournal.service;

import android.app.Application;
import android.content.Context;

import com.mycompany.traveljournal.datasource.ParseClient;

/**
 * Created by ekucukog on 6/7/2015.
 */
public class JournalApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        JournalApplication.context = this;
    }

    public static ParseClient getClient() {
        return (ParseClient) ParseClient.getInstance(JournalApplication.context);
    }
}
