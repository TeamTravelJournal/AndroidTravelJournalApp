package com.mycompany.traveljournal.examples;

import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;

import java.util.List;

public class ExampleComments {

    private String postId;
    private String userId;
    private String body;
    private JournalService client;

    public void createComment() {
        postId = "4jJZPXv8Zu";
        body = "Outdoor Shopping =)";

        client = JournalApplication.getClient();

        client.getPostWithId(postId, new JournalCallBack<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                Post post = posts.get(0);
                client.createComment(post, body);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void updateCommentAuthor() {
        JournalService client = JournalApplication.getClient();
        client.updateCommentUser("eX30QuMm8i", "PmdydyJioh");
    }
}
