package com.mycompany.traveljournal.examples;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.datasource.ImageUploader;
import com.mycompany.traveljournal.datasource.PostCreator;
import com.mycompany.traveljournal.models.Post;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ExampleSavePostToParse {

    private final static String TAG = "ExampleSavePostToParse";

    public static void savePostWithoutImage() {
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

    private Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getByteArrayFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private static byte[] getByteArrayFromUrl(String url) {
        Bitmap bmp = getBitmapFromURL(url);
        return getByteArrayFromBitmap(bmp);
    }

    public static void createPostComplete(Context context) {

        // Get sample byte Array
        //byte[] imageBytes = ExampleSavePostToParse.getByteArray(context);
        String imageUrl = "http://static.guim.co.uk/sys-images/Guardian/Pix/pictures/2012/12/5/1354730258045/Starbucks-008.jpg";
        byte[] imageBytes = getByteArrayFromUrl(imageUrl);

        PostCreator creator = new PostCreator();
        String caption = "I love my coffee in the morning";
        String description = "This was in a Starbucks";
        double latitude = 37.544783;
        double longitude = -122.292885;

        creator.createPost(imageBytes, caption, description, latitude, longitude);
    }

}
