package com.mycompany.traveljournal.service;

import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;

import java.util.Date;
import java.util.List;

/**
 * Created by ekucukog on 6/7/2015.
 */
public interface JournalService {

    public void getPostWithId(String postId, JournalCallBack<List<Post>> journalCallBack);

    public void getPostsNearLocation(double latitude, double longitude, int limit, JournalCallBack<List<Post>> journalCallBack);

    public void getPostsWithinWindow(double latitudeMin, double longitudeMin, double latitudeMax, double longitudeMax, int limit, JournalCallBack<List<Post>> journalCallBack);

    public void getRecentPosts(Date createdAt, int limit, JournalCallBack<List<Post>> journalCallBack);

    public void getPostsWithinMilesOrderByDate(Date createdAt, int maxDistance, double latitude, double longitude, int limit, JournalCallBack<List<Post>> journalCallBack);

    public void getUserWithId(String userId, final JournalCallBack<List<User>> journalCallBack);

    public void createUser(String name, String profile_image_url, String cover_image_url);

    public User getCurrentUser();

}
