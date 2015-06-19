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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.internal.cr;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.common.LocationOnConnectListener;
import com.mycompany.traveljournal.common.LocationService;
import com.mycompany.traveljournal.common.ProgressBarListener;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.mycompany.traveljournal.helpers.BitmapScaler;
import com.mycompany.traveljournal.helpers.DeviceDimensionsHelper;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;

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
    JournalService client;
    String m_localPhotoPath;
    private final static String TAG = "CreatePostFragmentDebug";
    ProgressBar pbLoading;
    ImageView ivPBGif;

    public static CreatePostFragment newInstance(){
        CreatePostFragment createPostFragment = new CreatePostFragment();
        Bundle args = new Bundle();
        createPostFragment.setArguments(args);
        return createPostFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_create, container, false);
        client = JournalApplication.getClient();
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
        setRetainInstance(true);
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

                //pbLoading.setVisibility(ProgressBar.VISIBLE);
                ivPBGif.setVisibility(ImageView.VISIBLE);

                btPost.setEnabled(false);
                //PostCreator postCreator =  new PostCreator();
                //int bytes = takenImage.getByteCount();
                //Toast.makeText(getActivity(), "After size 2 " + bytes,Toast.LENGTH_SHORT).show();
                byte[] array = Util.getByteArrayFromBitmap(takenImage);

                //double latitude = 37.421828;
                //double longitude = -122.084889;

                //client.createPost(array, etCaption.getText().toString(), "", latitude, longitude, new JournalCallBack<Post>() {
                client.createPost(array, etCaption.getText().toString(), "", latLng.latitude, latLng.longitude, new JournalCallBack<Post>() {
                    @Override
                    public void onSuccess(Post post) {
                        //post created, image upload and image url update is happening at the background
                        Log.d(TAG, "success creating post");
                        //pbLoading.setVisibility(ProgressBar.INVISIBLE);

                        callNextIntent(post.getPostID());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "failed to create post");
                    }
                });
            }
        });
    }

    public void callNextIntent(String postID){
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra("post_id", postID);
        i.putExtra("local_photo_path", m_localPhotoPath);
        startActivity(i);
        getActivity().finish();
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
        ivPreview.setImageBitmap(takenImage);
        //pbLoading = (ProgressBar)v.findViewById(R.id.pbLoading);
        ivPBGif = (ImageView) v.findViewById(R.id.ivPBGif);
        Glide.with(this).load(R.raw.simple).asGif().into(ivPBGif);
    }

    public void setPhotoPath(Uri photoPathUri){
        m_localPhotoPath = photoPathUri.getPath();
        Bitmap takenImage1 = Util.rotateBitmapOrientation(photoPathUri.getPath());
        //int bytes1 = takenImage1.getByteCount();
        //Toast.makeText(getActivity(), "Before size " + bytes1,Toast.LENGTH_SHORT).show();

        int screenWidth = DeviceDimensionsHelper.getDisplayWidth(getActivity());
        // Resize a Bitmap maintaining aspect ratio based on screen width

        int width = takenImage1.getWidth();
        int height = takenImage1.getHeight();
        Log.d(TAG, "taken image width: " + width + ", height: " + height);
        if(width > height){
            Log.d(TAG, "wide image");
            //Following screenWidth is coming bigger than we expect
            // that is why we use smaller than to width to scale
            takenImage = BitmapScaler.scaleToFitWidth(takenImage1, screenWidth/2);
        }else{
            Log.d(TAG, "tall image");
            takenImage = BitmapScaler.scaleToFitWidth(takenImage1, screenWidth/3);
        }

        //int bytes = takenImage.getByteCount();
        //Toast.makeText(getActivity(), "after size 1 " + bytes,Toast.LENGTH_SHORT).show();
        // Load the taken image into a preview
        ivPreview.setImageBitmap(takenImage);
    }

    public void initLocationService(){
        locationService = new LocationService(getActivity(), new LocationOnConnectListener() {
            @Override
            public void onLocationAvailable(Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });
        locationService.connectClient();
    }

}
