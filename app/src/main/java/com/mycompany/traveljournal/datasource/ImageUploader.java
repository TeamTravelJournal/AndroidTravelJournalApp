package com.mycompany.traveljournal.datasource;


import android.util.Log;

import com.mycompany.traveljournal.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.util.List;

public class ImageUploader {

    private static final String TAG = "ImageUploader";
    private String postId;
    private byte[] imageBytes;
    ParseFile file;
    String imageUrl;

    public ImageUploader(String postId, byte[] imageBytes) {
        this.postId = postId;
        this.imageBytes = imageBytes;
    }

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

    private void setImageUrlOnPost() {

        Post.getPostWithId(postId, new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    Post post = posts.get(0);
                    post.put("image_url", imageUrl);
                    post.saveInBackground();
                } else {
                    Log.wtf(TAG, "Post not found");
                }
            }
        });

    }
}
