package com.mycompany.traveljournal.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sjayaram on 6/13/2015.
 */
public class MyCustomReceiver extends BroadcastReceiver {
    private static final String TAG = "MyCustomReceiver";
    public static final String intentAction = "SEND_PUSH";
    //public static final String activityAction = "com.mycompany.traveljournal.detailsscreen.MainPostFragment";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.d(TAG, "Receiver intent null");
        } else {
            // Parse push message and handle accordingly
            processPush(context, intent);
        }
    }

    private void processPush(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "got action " + action );
        if (intentAction.equals(action))
        {
            String channel = intent.getExtras().getString("com.parse.Channel");
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
                // Iterate the parse keys if needed
                Iterator<String> itr = json.keys();
                String postID = "";
                String title = "";
                String userId = "";

                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    // Extract custom push data
                    if (key.equals("content")) {
                        title = json.getString(key);
                    }
                    if (key.equals("userId")) {
                        userId = json.getString(key);
                    }
                    if (key.equals("customdata")) {
                        postID = json.getString(key);
                    }
                    Log.d(TAG, "..." + key + " => " + json.getString(key));
                }

                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;

                if(componentInfo.getPackageName().equalsIgnoreCase("com.mycompany.traveljournal")){
                    //Activity Running
                    triggerBroadcastToActivity(context);
                }
                else{
                    //Activity Not Running
                    //Generate Notification
                    createNotification(context, title, postID);
                }


            } catch (JSONException ex) {
                Log.d(TAG, "JSON failed!");
            }
        }
    }

    public static final int NOTIFICATION_ID = 45;
    // Create a local dashboard notification to tell user about the event
    private void createNotification(Context context, String title, String datavalue) {

        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra("post_id", datavalue);
        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(context, requestID, i, flags);


        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_notif_airballoon);

        Notification noti =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notif_airballoon)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Travel notification")
                        .setContentText(title).setAutoCancel(true)
                        .setContentIntent(pIntent).build();

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(45, noti);
    }

    // Handle push notification by invoking activity directly
    private void launchSomeActivity(Context context, String datavalue) {
        Intent pupInt = new Intent(context, DetailActivity.class);
        pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        pupInt.putExtra("post_id", datavalue);
        context.getApplicationContext().startActivity(pupInt);
    }

    // Handle push notification by sending a local broadcast
    // to which the activity subscribes to
    private void triggerBroadcastToActivity(Context context) {
        Intent in = new Intent("com.mycompany.traveljournal.detailsscreen.MainPostFragment");
        LocalBroadcastManager.getInstance(context).sendBroadcast(in);
    }
}
