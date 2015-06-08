package com.mycompany.traveljournal.examples;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.mycompany.traveljournal.datasource.PostCreator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ExampleSavePostToParse {

    private final static String TAG = "ExampleSavePostToParse";

    // Helper
    private static Bitmap getBitmapFromURL(String src) {
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

    // Helper
    private static byte[] getByteArrayFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    // Helper
    private static byte[] getByteArrayFromUrl(String url) {
        Bitmap bmp = getBitmapFromURL(url);
        return getByteArrayFromBitmap(bmp);
    }

    // Helper
    private class CreatePostAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            createPostComplete();

            //return Void;
            return null;
        }
    }

    /**
     * This method can be used to create fake posts
     */
    public static void createPostComplete() {

        // Get sample byte Array
        // THIS REQUIRES THE ASYNC TASK
        String imageUrl = "http://upload.wikimedia.org/wikipedia/commons/c/cd/Google_Campus2_cropped.jpg";
        byte[] imageBytes = getByteArrayFromUrl(imageUrl);


        // No Async task required
        PostCreator creator = new PostCreator();
        String caption = "At the Goog!";
        String description = "";
        double latitude = 37.421828;
        double longitude = -122.084889;

        creator.createPost(imageBytes, caption, description, latitude, longitude);
    }

    /**
     * The example only has to happen in an AsyncTask because it downloads an image
     *
     * This only requires an ASYNC TASK when testing
     */
    public void createPostExample() {
        new CreatePostAsync().execute();
    }

}
