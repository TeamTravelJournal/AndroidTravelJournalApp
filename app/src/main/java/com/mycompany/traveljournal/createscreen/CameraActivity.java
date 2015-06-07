package com.mycompany.traveljournal.createscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.mycompany.traveljournal.helpers.BitmapScaler;
import com.mycompany.traveljournal.helpers.DeviceDimensionsHelper;
import com.mycompany.traveljournal.helpers.Util;

public class CameraActivity extends ActionBarActivity {

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setUpCameraIntent();
    }

    public void setUpCameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Util.getPhotoFileUri(photoFileName)); // set the image file name
        // Start the image capture intent to take photo
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void executeCreatePostIntent(){
        Intent i = new Intent(this, CreatePostActivity.class);
        i.putExtra(MediaStore.EXTRA_OUTPUT, photoFileName);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = Util.getPhotoFileUri(photoFileName);
                Bitmap takenImage = Util.rotateBitmapOrientation(takenPhotoUri.getPath());
                int screenWidth = DeviceDimensionsHelper.getDisplayWidth(this);
                // Resize a Bitmap maintaining aspect ratio based on screen width
                BitmapScaler.scaleToFitWidth(takenImage, screenWidth);
                executeCreatePostIntent();

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
