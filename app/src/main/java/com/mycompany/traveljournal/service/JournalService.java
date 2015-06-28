package com.mycompany.traveljournal.service;

import com.mycompany.traveljournal.models.Comment;
import com.mycompany.traveljournal.models.Message;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;

import java.util.Date;
import java.util.List;

/**
 * Created by ekucukog on 6/7/2015.
 */
public interface JournalService {

    public void getPostWithId(String postId, JournalCallBack<Post> journalCallBack);

    public void getPostsNearLocation(double latitude, double longitude, int limit, JournalCallBack<List<Post>> journalCallBack);

    public void getPostsWithinWindow(double latitudeMin, double longitudeMin, double latitudeMax, double longitudeMax, int limit, JournalCallBack<List<Post>> journalCallBack);

    public void getRecentPosts(Date createdAt, int limit, JournalCallBack<List<Post>> journalCallBack);

    public void getLatestPosts(Date createdAt, int limit, JournalCallBack<List<Post>> journalCallBack);

    public List<Post> getLatestPostsFromLocal(Date latestDate);

    public void getPostsWithinMilesOrderByDate(Date createdAt, int maxDistance, double latitude, double longitude, int limit, JournalCallBack<List<Post>> journalCallBack);

    public void getUserWithId(String userId, final JournalCallBack<List<User>> journalCallBack);

    public void createUser(String name, String profile_image_url, String cover_image_url);

    public User getCurrentUser();

    public void getPostsForUser(String userId, Date createdAt, int limit, JournalCallBack<List<Post>> journalCallBack);

    public void createComment(String postId, String body, JournalCallBack<Comment> journalCallBack);

    // Programmatically update a comment's author
    public void updateCommentUser(final String commentId, String userId);

    public void getCommentsForPost(String postId, int limit, JournalCallBack<List<Comment>> journalCallBack);

    public void createPost(byte[] imageBytes, String caption, String city, String description, double latitude, double longitude, final JournalCallBack<Post> journalCallBack);

    public void createMessage(String post, String userId, JournalCallBack journalCallBack);

    public void receiveMessage(int limit, JournalCallBack<List<Message>> journalCallBack);

    public void sendPushMessage(String message, String toUserId, String profileImg);

}
