package com.mycompany.traveljournal.examples;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.datasource.ImageUploader;
import com.mycompany.traveljournal.models.Post;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class ExampleSavePostToParse {

    private final static String TAG = "ExampleSavePostToParse";

    public static void savePost() {
        Post post = new Post();
        post.put("caption", "Shopping @Stanford");
        post.put("description", "");

        ParseGeoPoint location = new ParseGeoPoint();
        location.setLatitude(37.438230);
        location.setLongitude(-122.173328);
        post.put("location", location);

        post.put("user_id", "1");
        post.put("likes", 6);
        post.put("trip_id", 0);


        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.wtf(TAG, "Post saved");
                } else {
                    Log.wtf(TAG, "Failed to save post"+e.toString());
                }
            }
        });

    }

    public static void uploadPhotoToPost(Context context) {

        // test postId
        String postId = "oVyDQtk78T";
        // test data
        byte[] byteArray = ExampleSavePostToParse.getByteArray(context);

        ImageUploader uploader = new ImageUploader(postId, byteArray);
        uploader.upload();
    }

    private static byte[] getByteArray(Context context) {
        Resources res = context.getResources();
        Drawable drawable = res.getDrawable(R.drawable.coffee);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();
        return bitMapData;
    }

}
