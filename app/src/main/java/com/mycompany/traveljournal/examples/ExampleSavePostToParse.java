package com.mycompany.traveljournal.examples;


import android.content.Context;
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
        private Context context;

        public CreatePostAsync(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            createPostComplete(context);
            //return Void;
            return null;
        }
    }

    /**
     * This method can be used to create fake posts
     */
    public static void createPostComplete(Context context) {

        // Get sample byte Array
        String imageUrl = "http://www.digitaljournal.com/img/8/7/3/i/4/1/3/o/StarbucksCafe.jpg";
        byte[] imageBytes = getByteArrayFromUrl(imageUrl);

        PostCreator creator = new PostCreator();
        String caption = "I love my coffee in the morning";
        String description = "This was in a Starbucks";
        double latitude = 37.544783;
        double longitude = -122.292885;

        creator.createPost(imageBytes, caption, description, latitude, longitude);
    }

    public void createPostExample(Context context) {
        new CreatePostAsync(context).execute();
    }

}
