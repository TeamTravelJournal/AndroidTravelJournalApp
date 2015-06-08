package com.mycompany.traveljournal.createscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.common.LocationOnConnectListener;
import com.mycompany.traveljournal.common.LocationService;
import com.mycompany.traveljournal.datasource.PostCreator;
import com.mycompany.traveljournal.helpers.BitmapScaler;
import com.mycompany.traveljournal.helpers.DeviceDimensionsHelper;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mainscreen.MainActivity;
import com.mycompany.traveljournal.profilescreen.ProfileActivity;

import java.nio.ByteBuffer;

/**
 * Created by sjayaram on 6/5/2015.
 */
public class CreatePostFragment extends Fragment {

    ImageView ivPreview;
    ImageView ivCamera;
    EditText etCaption;
    private LocationService locationService;
    private LatLng latLng;
    Button btPost;
    Bitmap takenImage;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";

    public static CreatePostFragment newInstance(){
        CreatePostFragment createPostFragment = new CreatePostFragment();
        Bundle args = new Bundle();
        createPostFragment.setArguments(args);
        return createPostFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_create, container, false);
        setUpViews(view);
        setUpListeners();
        initLocationService();
        return view;
    }

    /*
     * Called when the Activity becomes visible.
    */
    @Override
    public void onStart() {
        super.onStart();
        if(locationService!=null) {
            locationService.connectClient();
        }
    }

    /*
	 * Called when the Activity is no longer visible.
	 */
    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        if(locationService!=null) {
            locationService.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setUpListeners(){
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpCameraIntent();
            }
        });

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCreator postCreator =  new PostCreator();

                int bytes = takenImage.getByteCount();
                //Create a new buffer
                ByteBuffer buffer = ByteBuffer.allocate(bytes);
                //Move the byte data to the buffer
                takenImage.copyPixelsToBuffer(buffer);
                byte[] array = buffer.array();

                postCreator.createPost(array ,etCaption.getText().toString(), "", latLng.latitude, latLng.longitude);
                callNextIntent();

            }
        });
    }

    public void callNextIntent(){
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    public void setUpCameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Util.getPhotoFileUri(photoFileName)); // set the image file name
        // Start the image capture intent to take photo
        getActivity().startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void setUpViews(View v){
        ivPreview = (ImageView) v.findViewById(R.id.ivPreview);
        ivCamera = (ImageView) v.findViewById(R.id.ivCamera);
        btPost = (Button)v.findViewById(R.id.btPost);
        etCaption = (EditText)v.findViewById(R.id.etCaption);
    }

    public void setPhotoPath(Uri photoPathUri){
        Bitmap takenImage1 = Util.rotateBitmapOrientation(photoPathUri.getPath());
        int screenWidth = DeviceDimensionsHelper.getDisplayWidth(getActivity());
        // Resize a Bitmap maintaining aspect ratio based on screen width
        takenImage = BitmapScaler.scaleToFitWidth(takenImage1, screenWidth);
        // Load the taken image into a preview
        ivPreview.setImageBitmap(takenImage);
    }

    public void initLocationService(){
        locationService = LocationService.getInstance(getActivity(), new LocationOnConnectListener() {
            @Override
            public void onLocationAvailable(Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });
        locationService.connectClient();
    }

}
