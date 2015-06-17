package com.mycompany.traveljournal.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mainscreen.MainPostFragment;
import com.mycompany.traveljournal.models.Post;
import com.parse.ParseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by sjayaram on 6/16/2015.
 */
public class NewPostsService extends IntentService {

    public static final String PARAM_IN_MSG = "latestDate";
    public static final String PARAM_OUT_MSG = "data";
    private final static String TAG = "NewPostsService";

    public NewPostsService() {
        super("NewPostsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("NewPostsService", "Service running");

        String previousDate = intent.getStringExtra(PARAM_IN_MSG);
        JournalService client = JournalApplication.getClient();
        //Tue Jun 16 23:55:17 EDT 2015

        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        Calendar cal = null;

        try {

            Date date = formatter.parse(previousDate);
            cal = Calendar.getInstance();
            cal.setTime(date);
            cal.setTimeZone(TimeZone.getDefault());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        client.getLatestPosts(cal.getTime(), Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
            @Override
            public void onSuccess(List<Post> resultPosts) {
                Log.d(TAG, "success getting posts: " + resultPosts.toString());

                if (resultPosts.size() > 0) {

                    try {
                        ParseObject.pinAll("POSTS_GROUP_NAME", resultPosts);
                    } catch (com.parse.ParseException e) {
                        e.printStackTrace();
                    }

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(MainPostFragment.ResponseReceiver.ACTION_RESP);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent.putExtra(PARAM_OUT_MSG, resultPosts.size() + "");
                    sendBroadcast(broadcastIntent);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Failed to get posts");
            }

        });

    }
}
