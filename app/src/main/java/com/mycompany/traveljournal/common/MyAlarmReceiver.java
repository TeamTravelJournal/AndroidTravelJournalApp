package com.mycompany.traveljournal.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.mycompany.traveljournal.service.NewPostsService;

/**
 * Created by sjayaram on 6/16/2015.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.mycompany.traveljournal.common.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, NewPostsService.class);
        //Bundle bundle = intent.getExtras();
        //String latestDate = bundle.getString(NewPostsService.PARAM_IN_MSG);
        SharedPreferences mSettings = context.getSharedPreferences("Settings", 0);
        String latestDate = mSettings.getString(NewPostsService.PARAM_IN_MSG, "missing");

        if(!latestDate.equals("missing")) {
            i.putExtra(NewPostsService.PARAM_IN_MSG, latestDate);
            context.startService(i);
        }
    }
}
