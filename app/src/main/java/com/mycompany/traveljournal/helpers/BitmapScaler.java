package com.mycompany.traveljournal.helpers;

import android.graphics.Bitmap;

/**
 * Created by sjayaram on 6/7/2015.
 */
public class BitmapScaler
{
    // Scale and maintain aspect ratio given a desired width
    // BitmapScaler.scaleToFitWidth(bitmap, 100);
    public static Bitmap scaleToFitWidth(Bitmap b, int width)
    {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
    }


    // Scale and maintain aspect ratio given a desired height
    // BitmapScaler.scaleToFitHeight(bitmap, 100);
    public static Bitmap scaleToFitHeight(Bitmap b, int height)
    {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
    }

    public static int scaledHeight(Bitmap b, int width)
    {
        float factor = width / (float) b.getWidth();
        return (int) (b.getHeight() * factor);
    }


    // Scale and maintain aspect ratio given a desired height
    // BitmapScaler.scaleToFitHeight(bitmap, 100);
    public static int scaledWidth(Bitmap b, int height)
    {
        float factor = height / (float) b.getHeight();
        return (int) (b.getWidth() * factor);
    }

    // ...
}