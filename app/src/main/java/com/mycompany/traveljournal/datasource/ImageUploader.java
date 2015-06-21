package com.mycompany.traveljournal.datasource;


import android.util.Log;

import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

public class ImageUploader {

    private static final String TAG = "ImageUploader";
    private String postId;
    private byte[] imageBytes;
    ParseFile file;
    String imageUrl;
    private JournalService client;

    public ImageUploader(String postId, byte[] imageBytes) {
        this.postId = postId;
        this.imageBytes = imageBytes;
        this.client = JournalApplication.getClient();
    }

    /**
     * This uploads a photo in the background and then updates the post's image_url
     */
    public void upload() {

        Log.wtf(TAG, "uploading");

        file = new ParseFile(imageBytes);

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    imageUrl = file.getUrl();
                    setImageUrlOnPost();
                    Log.wtf(TAG, "done, setting image url on post");
                } else {
                    // There was a problem
                    Log.wtf(TAG, "Problem uploading image to post ");
                }
            }
        });
    }

    /**
     * This updates the image_url field on a post in the background
     */
    private void setImageUrlOnPost() {

        client.getPostWithId(postId, new JournalCallBack<Post>() {
            @Override
            public void onSuccess(Post post) {
                post.put("image_url", imageUrl);
                post.saveInBackground();

                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.wtf(TAG, "done, setting image url on post successfully");
                            PostCreatorHelper.sendPush(postId);
                        } else {
                            // There was a problem
                            Log.wtf(TAG, "Problem uploading image to post ");
                        }
                    }
                });

            }
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Post not found");
            }
        });
    }
}
