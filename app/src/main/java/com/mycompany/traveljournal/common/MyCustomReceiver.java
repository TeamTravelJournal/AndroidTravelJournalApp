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
import android.graphics.PixelFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.chatscreen.ChatActivity;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalService;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sjayaram on 6/13/2015.
 */
public class MyCustomReceiver extends BroadcastReceiver {
    private static final String TAG = "MyCustomReceiver";
    private WindowManager windowManager;
    private RelativeLayout item;
    public static final String intentMessageAction = "SEND_MESSAGE_PUSH";
    public static final String intentAction = "SEND_PUSH";

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

        //Push new posts
        if (intentAction.equals(action) || intentMessageAction.equals(action))
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
                String profileImg = "";

                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    // Extract custom push data
                    if (key.equals("content")) {
                        title = json.getString(key);
                    }
                    if (key.equals("userId")) {
                        userId = json.getString(key);
                    }
                    if (key.equals("profileImg")) {
                        profileImg = json.getString(key);
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
                    if (intentAction.equals(action)) {
                        triggerBroadcastToActivity(context);
                    }
                }
                else{
                    //Activity Not Running
                    //Generate Notification
                    if (intentAction.equals(action)) {
                        createNotification(context, title, postID);
                    }
                }

                if(taskInfo.get(0).topActivity.getClassName().equalsIgnoreCase("com.mycompany.traveljournal.chatscreen.ChatActivity")){
                    //Activity Running
                    if(intentMessageAction.equals(action)){
                        triggerBroadcastToChatActivity(context, postID, userId, profileImg);
                    }
                }
                else{
                    //Activity Not Running
                    //Generate Notification
                    if(intentMessageAction.equals(action)){
                        createMessageNotification(context, title, profileImg);
                    }
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

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_notif_airballoon);

        Notification noti =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notif_airballoon)
                        //.setLargeIcon(largeIcon)
                        .setContentTitle("Travel notification")
                        .setContentText(title).setAutoCancel(true)
                        .setSound(soundUri) //This sets the sound to play
                        .setContentIntent(pIntent).build();

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(45, noti);
    }

    private void createMessageNotification(final Context context, String title, String profileImg) {

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_head, null);
        item = (RelativeLayout) view.findViewById(R.id.rl);
        ImageView chatHead = (ImageView) view.findViewById(R.id.family_hub_imageview);

        Picasso.with(context)
                .load(profileImg)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholderthumbnail)
                .transform(Util.getNoBorderTransformation(40))
                .into(chatHead);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                280,
                280,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 150;
        params.y = 1050;

        windowManager.addView(item, params);

        item.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return false;
                    case MotionEvent.ACTION_UP:
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(item, params);
                        return false;
                }
                return false;
            }
        });

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(i);
                windowManager.removeView(item);
            }
        });

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

    private void triggerBroadcastToChatActivity(Context context, String message, String userId, String profileImg) {
        Intent in = new Intent("com.mycompany.traveljournal.chatscreen.ChatActivity");
        in.putExtra("message", message);
        in.putExtra("userId", userId);
        in.putExtra("profileImg", profileImg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(in);

    }
}
