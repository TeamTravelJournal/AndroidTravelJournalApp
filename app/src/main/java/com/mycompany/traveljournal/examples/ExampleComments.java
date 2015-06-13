package com.mycompany.traveljournal.examples;

import android.util.Log;

import com.mycompany.traveljournal.models.Comment;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;

import java.util.List;

public class ExampleComments {

    private final static String TAG = "ExampleComments";

    private String postId;
    private String userId;
    private String body;
    private JournalService client;

    public ExampleComments() {
        client = JournalApplication.getClient();
    }

    public void createComment() {
        postId = "4jJZPXv8Zu";
        body = "Outdoor Shopping =)";

        client.getPostWithId(postId, new JournalCallBack<Post>() {
            @Override
            public void onSuccess(Post post) {
                client.createComment(post, body);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void updateCommentAuthor() {
        client.updateCommentUser("eX30QuMm8i", "PmdydyJioh");
    }

    public void getCommentsForPost() {
        postId = "4jJZPXv8Zu";
        client.getCommentsForPost(postId, 100, new JournalCallBack<List<Comment>>() {

            @Override
            public void onSuccess(List<Comment> comments) {

                for (int i = 0; i < comments.size() ; i++) {
                    Log.wtf(TAG, comments.get(i).toString());
                }

            }

            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Failed to get comments for post " + postId);
            }

        });
    }
}
