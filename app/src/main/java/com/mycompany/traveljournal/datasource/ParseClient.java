package com.mycompany.traveljournal.datasource;


import android.content.Context;

import com.mycompany.traveljournal.models.Like;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;


public class ParseClient implements JournalService {

    private static final String TAG = "ParseClient";
    private static ParseClient instance = null;

    protected ParseClient() {

    }

    /**
     *  Get a the singleton ParseClient instance
     */
    public static ParseClient getInstance(Context context) {
        if (instance == null) {
            instance = new ParseClient();

            // Automatically initialize
            instance.init(context);
        }
        return instance;
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

    public void getPostWithId(String postId, final JournalCallBack<List<Post>> journalCallBack) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("objectId", postId);
        query.setLimit(1);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> resultPosts, ParseException e) {
                if (e == null) {
                    journalCallBack.onSuccess(resultPosts);
                } else {
                    journalCallBack.onFailure(e);
                }
            }
        });
    }

    public void getPostsNearLocation(double latitude, double longitude, int limit, final JournalCallBack<List<Post>> journalCallBack) {
        ParseGeoPoint userLocation = new ParseGeoPoint(latitude, longitude);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereNear("location", userLocation);
        query.setLimit(limit);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> resultPosts, ParseException e) {
                if (e == null) {
                    journalCallBack.onSuccess(resultPosts);
                } else {
                    journalCallBack.onFailure(e);
                }
            }
        });
    }

    public void getPostsWithinWindow(double latitudeMin, double longitudeMin, double latitudeMax, double longitudeMax, int limit, final JournalCallBack<List<Post>> journalCallBack) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setLimit(limit);
        ParseGeoPoint swPoint = new ParseGeoPoint(latitudeMin, longitudeMin);
        ParseGeoPoint nePoint = new ParseGeoPoint(latitudeMax, longitudeMax);
        query.whereWithinGeoBox("location", swPoint, nePoint);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> resultPosts, ParseException e) {
                if (e == null) {
                    journalCallBack.onSuccess(resultPosts);
                } else {
                    journalCallBack.onFailure(e);
                }
            }
        });
    }

    public void getRecentPosts(Date createdAt, int limit, final JournalCallBack<List<Post>> journalCallBack){

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        if(createdAt!=null){
            //if we pass a timestamp, query the posts older than timestamp
            //otherwise query everything sorted by timestamp
            query.whereLessThan("createdAt", createdAt);
        }
        query.orderByDescending("createdAt");
        query.setLimit(limit);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> resultPosts, ParseException e) {
                if (e == null) {
                    journalCallBack.onSuccess(resultPosts);
                } else {
                    journalCallBack.onFailure(e);
                }
            }
        });
    }

    public void getPostsNearLocationOrderByDate(Date createdAt, double latitude, double longitude, int limit, final JournalCallBack<List<Post>> journalCallBack) {
        ParseGeoPoint userLocation = new ParseGeoPoint(latitude, longitude);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereNear("location", userLocation);
        if(createdAt!=null){
            //if we pass a timestamp, query the posts older than timestamp
            //otherwise query everything sorted by timestamp
            query.whereLessThan("createdAt", createdAt);
        }
        query.orderByDescending("createdAt");
        query.setLimit(limit);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> resultPosts, ParseException e) {
                if (e == null) {
                    journalCallBack.onSuccess(resultPosts);
                } else {
                    journalCallBack.onFailure(e);
                }
            }
        });
    }

    public void getPostsWithinMilesOrderByDate(Date createdAt, int maxDistance, double latitude, double longitude, int limit, final JournalCallBack<List<Post>> journalCallBack) {
        ParseGeoPoint userLocation = new ParseGeoPoint(latitude, longitude);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereWithinMiles("location", userLocation, maxDistance);
        if(createdAt!=null){
            //if we pass a timestamp, query the posts older than timestamp
            //otherwise query everything sorted by timestamp
            query.whereLessThan("createdAt", createdAt);
        }
        query.orderByDescending("createdAt");
        query.setLimit(limit);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> resultPosts, ParseException e) {
                if (e == null) {
                    journalCallBack.onSuccess(resultPosts);
                } else {
                    journalCallBack.onFailure(e);
                }
            }
        });
    }

    public void getUserWithId(String userId, final JournalCallBack<List<User>> journalCallBack) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo("user_id", userId);
        query.setLimit(1);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> resultUsers, ParseException e) {
                if (e == null) {
                    journalCallBack.onSuccess(resultUsers);
                } else {
                    journalCallBack.onFailure(e);
                }
            }
        });
    }

    public void getCreatedByUserFromPost(User user, final JournalCallBack<User> journalCallBack) {
        user.fetchIfNeededInBackground(new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                if (e == null) {
                    journalCallBack.onSuccess(user);
                } else {
                    journalCallBack.onFailure(e);
                }
            }
        });
    }

}