package com.mycompany.traveljournal.service;

import android.app.Application;
import android.content.Context;

import com.mycompany.traveljournal.datasource.ParseClient;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by ekucukog on 6/7/2015.
 */
public class JournalApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        JournalApplication.context = this;
        ParseClient.getInstance(JournalApplication.context);
    }

    public static ParseClient getClient() {
        return (ParseClient) ParseClient.getInstance(JournalApplication.context);
    }
}
