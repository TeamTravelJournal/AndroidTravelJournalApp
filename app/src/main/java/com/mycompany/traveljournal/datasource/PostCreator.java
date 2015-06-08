package com.mycompany.traveljournal.datasource;


import android.util.Log;

import com.mycompany.traveljournal.models.Post;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

public class PostCreator {

    private final static String TAG = "PostCreator";
    private String postId;
    private Post post;
    private byte[] imageBytes;

    public void createPost(byte[] imageBytes, String caption, String description, double latitude, double longitude) {
        this.imageBytes = imageBytes;

        createPostWithoutImage(caption, description, latitude, longitude);
    }

    /**
     * Multistep process:
     * 1) Creates a post without an image - this gets a postId
     * 2) In a callback:
     *  3) Uploads an image - this gets an image_url
     *  3) Updates the post with the image_url
     */
    private void createPostWithoutImage(String caption, String description, double latitude, double longitude) {
        post = new Post();
        post.put("caption", caption);
        post.put("description", description);

        ParseGeoPoint location = new ParseGeoPoint();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        post.put("location", location);

        post.put("user_id", SignedInUser.getSignedInUser().getUserId());
        post.put("created_by", SignedInUser.getSignedInUser());
        post.put("likes", 0);
        post.put("trip_id", 0);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Save the postID so it can be passed to the image
                    postId = post.getPostID();

                    // Upload image and update image_url in post
                    uploadAndAddImageToPost();

                } else {
                    Log.wtf(TAG, "Failed to save post"+e.toString());
                }
            }
        });
    }

    /**
     * This uploads the image and updates the post with the image url
     */
    private void uploadAndAddImageToPost() {
        ImageUploader uploader = new ImageUploader(this.postId, this.imageBytes);
        uploader.upload();
    }

}
