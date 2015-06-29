package com.mycompany.traveljournal.createscreen;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.google.android.gms.maps.model.LatLng;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.common.LocationOnConnectListener;
import com.mycompany.traveljournal.common.LocationService;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.mycompany.traveljournal.helpers.BitmapScaler;
import com.mycompany.traveljournal.helpers.DeviceDimensionsHelper;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import java.io.IOException;

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
    public final static int PICK_PHOTO_CODE = 1046;
    public String photoFileName = "photo.jpg";
    JournalService client;
    String m_localPhotoPath;
    String m_galleryPhotoPath;
    Bitmap selectedImage;
    private final static String TAG = "CreatePostFragmentDebug";
    ProgressBar pbLoading;
    ImageView ivPBGif;
    ImageView ivBGCreate;
    ImageView ivProfile;
    User m_currentUser;
    ImageView ivCross;
    ImageView ivGallery;

    public static CreatePostFragment newInstance(){
        CreatePostFragment createPostFragment = new CreatePostFragment();
        Bundle args = new Bundle();
        createPostFragment.setArguments(args);
        return createPostFragment;
    }

    // Define the listener of the interface type
    // listener is the activity itself
    private OnAttachedListener listener;

    // Define the events that the fragment will use to communicate
    public interface OnAttachedListener {
        public void OnAttached();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnAttachedListener) {
            listener = (OnAttachedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement CreatePostFragment.OnAttachedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_create, container, false);
        client = JournalApplication.getClient();
        m_currentUser = Util.getUserFromParseUser(ParseUser.getCurrentUser());

        setUpViews(view);
        setUpListeners();
        initLocationService();
        listener.OnAttached();
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

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto();
            }
        });

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress();

                btPost.setEnabled(false);
                btPost.setAlpha(0.5f);
                //PostCreator postCreator =  new PostCreator();
                //int bytes = takenImage.getByteCount();
                //Toast.makeText(getActivity(), "After size 2 " + bytes,Toast.LENGTH_SHORT).show();
                byte[] array;

                //loc is current location
                LatLng loc = latLng;

                if(m_localPhotoPath!=null){//camera image
                    array = Util.getByteArrayFromBitmap(takenImage);
                }else{//gallery image
                    array = Util.getByteArrayFromBitmap(selectedImage);
                    //latlng should be retrieved from the image
                    LatLng tmp = Util.getLatLngFromImage(m_galleryPhotoPath);
                    if(tmp!=null){
                        loc = tmp;
                    }else{
                        Log.d(TAG, "LatLng from gallery image is null");
                    }
                }

                //double latitude = 37.421828;
                //double longitude = -122.084889;

                String city = Util.getCity(getActivity(), loc);

                //client.createPost(array, etCaption.getText().toString(), "", "", latitude, longitude, new JournalCallBack<Post>() {
                client.createPost(array, etCaption.getText().toString(), city, "", loc.latitude, loc.longitude, new JournalCallBack<Post>() {
                    @Override
                    public void onSuccess(Post post) {
                        //post created, image upload and image url update is happening at the background
                        Log.d(TAG, "success creating post");
                        hideProgress();

                        callNextIntent(post.getPostID());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "failed to create post");
                    }
                });
            }
        });

        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivCross.setImageResource(android.R.color.transparent);
                ivPreview.setImageResource(android.R.color.transparent);
            }
        });
    }

    public void callNextIntent(String postID){
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra("post_id", postID);
        i.putExtra("local_photo_path", m_localPhotoPath);
        i.putExtra("gallery_photo_path", m_galleryPhotoPath);

        startActivity(i);
        getActivity().finish();
    }


    public void setUpCameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Util.getPhotoFileUri(photoFileName)); // set the image file name
        // Start the image capture intent to take photo
        getActivity().startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Bring up gallery to select a photo
        getActivity().startActivityForResult(intent, PICK_PHOTO_CODE);
    }

    public void setUpViews(View v){
        ivPreview = (ImageView) v.findViewById(R.id.ivPreview);
        ivCamera = (ImageView) v.findViewById(R.id.ivCamera);
        ivGallery = (ImageView) v.findViewById(R.id.ivGallery);
        btPost = (Button)v.findViewById(R.id.btPost);
        etCaption = (EditText)v.findViewById(R.id.etCaption);
        ivPreview.setImageBitmap(takenImage);
        pbLoading = (ProgressBar)v.findViewById(R.id.pbLoading);
        //ivPBGif = (ImageView) v.findViewById(R.id.ivPBGif);
        //Glide.with(this).load(R.raw.simple).asGif().into(ivPBGif);
        ivBGCreate = (ImageView) v.findViewById(R.id.ivBGCreate);
        ivCross = (ImageView) v.findViewById(R.id.ivCross);
        ivProfile = (ImageView) v.findViewById(R.id.ivProfileImageCreate);

        ivProfile.setImageResource(android.R.color.transparent);
        ivCross.setImageResource(android.R.color.transparent);

        if(m_currentUser != null)
        {
            Picasso.with(getActivity())
                    .load(m_currentUser.getProfileImgUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholderthumbnail)
                    .transform(Util.getTransformation(40))
                    .into(ivProfile);
        }
    }

    public void setPhotoPathForCameraImage(Uri photoPathUri){
        m_localPhotoPath = photoPathUri.getPath();
        m_galleryPhotoPath = null;
        int screenWidth = DeviceDimensionsHelper.getDisplayWidth(getActivity());
        int screenHeight = DeviceDimensionsHelper.getDisplayHeight(getActivity());

        Bitmap takenImage1 = Util.rotateBitmapOrientation(photoPathUri.getPath(), screenWidth/3, screenHeight/3);
        //int bytes1 = takenImage1.getByteCount();
        //Toast.makeText(getActivity(), "Before width " + takenImage1.getWidth() + " height " + takenImage1.getHeight() + " size " +  + bytes1, Toast.LENGTH_SHORT).show();

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

        takenImage1 = null;

        //int bytes = takenImage.getByteCount();
        //Toast.makeText(getActivity(), "Before width " + takenImage.getWidth() + " height " + takenImage.getHeight() + " size " +  + bytes, Toast.LENGTH_SHORT).show();
        // Load the taken image into a preview

        ivPreview.setImageBitmap(takenImage);
        ivCross.setImageResource(R.drawable.icon_cross_24);
    }

    public void setPhotoPathForGalleryImage(Uri photoPathUri){

        if (photoPathUri.getScheme().toString().compareTo("content")==0)
        {
            Cursor cursor = this.getActivity().getContentResolver().query(photoPathUri, null, null, null, null);
            if (cursor.moveToFirst())
            {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                Uri filePathUri = Uri.parse(cursor.getString(column_index));
                String file_name = filePathUri.getLastPathSegment().toString();
                String file_path=filePathUri.getPath();
                Log.d(TAG, "File Name & PATH are:"+file_name+"\n"+file_path);
                m_galleryPhotoPath = file_path;
                //Toast.makeText(getActivity(),"File Name & PATH are:"+file_name+"\n"+file_path, Toast.LENGTH_LONG).show();
            }
        }

        m_localPhotoPath = null;

        try{
            Bitmap selectedImage1 = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), photoPathUri);

            int screenWidth = DeviceDimensionsHelper.getDisplayWidth(getActivity());
            // Resize a Bitmap maintaining aspect ratio based on screen width

            int width = selectedImage1.getWidth();
            int height = selectedImage1.getHeight();
            Log.d(TAG, "selected image width: " + width + ", height: " + height);
            if(width > height){
                Log.d(TAG, "wide image");
                //Following screenWidth is coming bigger than we expect
                // that is why we use smaller than to width to scale
                selectedImage = BitmapScaler.scaleToFitWidth(selectedImage1, screenWidth/2);
            }else{
                Log.d(TAG, "tall image");
                selectedImage = BitmapScaler.scaleToFitWidth(selectedImage1, screenWidth/3);
            }

        }catch(IOException e){
            Log.d(TAG, "IOException: " + e.getMessage());
            e.printStackTrace();
        }

        if(selectedImage!=null){
            ivPreview.setImageBitmap(selectedImage);
            ivCross.setImageResource(R.drawable.icon_cross_24);
        }
        else{
            Log.d(TAG, "Selected image is null");
        }
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

    protected void showProgress() {
        if (pbLoading != null) {
            pbLoading.setVisibility(ProgressBar.VISIBLE);
        }
        //ivPBGif.setVisibility(ImageView.VISIBLE);
    }

    protected void hideProgress() {
        if (pbLoading != null) {
            pbLoading.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}
