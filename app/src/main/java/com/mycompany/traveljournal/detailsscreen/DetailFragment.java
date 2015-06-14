package com.mycompany.traveljournal.detailsscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.createscreen.CreatePostActivity;
import com.mycompany.traveljournal.helpers.BitmapScaler;
import com.mycompany.traveljournal.helpers.DeviceDimensionsHelper;
import com.mycompany.traveljournal.commentscreen.CommentActivity;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mainscreen.MainActivity;
import com.mycompany.traveljournal.mapscreen.SingleMapActivity;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.squareup.picasso.Picasso;


public class DetailFragment extends Fragment {

    private final static String TAG = "DetailFragment";
    private String postId;
    private ImageView ivProfile;
    private ImageView ivPost;
    private TextView tvCaption;
    private ImageView ivShare;
    private ImageView ivFollow;
    private ImageView ivStar;
    private ImageView ivComment;
    private TextView tvLikes;
    private TextView tvName;
    private Toolbar toolbar;
    private ImageView ivStaticMap;
    private Post m_post;
    private TextView tvNumComments;
    private String localPhotoPath;
    private boolean imageViewLoaded = false;

    public static DetailFragment newInstance(String postId, String localPhotoPath) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("post_id", postId);
        args.putString("local_photo_path", localPhotoPath);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        setUpViews(view);
        setToolbar();
        setUpListeners();
        populateImageViewFromLocal();
        fetchPostAndPopulateViews();
        return view;
    }

    public void setUpViews(View v){
        ivProfile = (ImageView) v.findViewById(R.id.ivProfile);
        ivPost = (ImageView) v.findViewById(R.id.ivPost);
        tvCaption = (TextView) v.findViewById(R.id.tvCaption);
        ivShare = (ImageView) v.findViewById(R.id.ivShare);
        ivFollow = (ImageView) v.findViewById(R.id.ivFollow);
        ivStar = (ImageView) v.findViewById(R.id.ivStar);
        ivComment = (ImageView) v.findViewById(R.id.ivComment);
        tvLikes = (TextView) v.findViewById(R.id.tvLikes);
        tvName = (TextView) v.findViewById(R.id.tvUserName);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ivStaticMap = (ImageView)v.findViewById(R.id.ivStaticMap);
        tvNumComments = (TextView) v.findViewById(R.id.tvNumComments);
    }

    public void setUpListeners() {

        //Once clicked to map, go to single map activity for that post
        ivStaticMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SingleMapActivity.class);
                i.putExtra("post_id", postId);
                startActivity(i);
            }
        });

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CommentActivity.class);
                i.putExtra("post_id", postId);
                startActivity(i);
            }
        });

        ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PhotoActivity.class);
                i.putExtra("image_url", m_post.getImageUrl());
                startActivity(i);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postId = getArguments().getString("post_id", "");
        localPhotoPath = getArguments().getString("local_photo_path");

        Log.wtf(TAG, "post_id is : " + postId + " , local_photo_path is : " + localPhotoPath);
    }

    private void fetchPostAndPopulateViews() {
        JournalService client = JournalApplication.getClient();
        client.getPostWithId(postId, new JournalCallBack<Post>() {
            @Override
            public void onSuccess(Post post) {
                m_post = post;
                populateViews(post);
            }
            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Post not found with id "+postId);
            }

        });
    }


    private void populateViews(Post post) {
        if(!imageViewLoaded)
            Picasso.with(getActivity()).load(post.getImageUrl()).into(ivPost);
        tvCaption.setText(post.getCaption());
        tvLikes.setText(post.getLikes()+" Likes");
        tvName.setText(post.getParseUser().getName());

        // Number of Comments
        int numComments = post.getNumComments();
        String numCommentText;
        if (numComments == 1) {
            numCommentText = numComments + " Comment";
        } else {
            numCommentText = numComments + " Comments";
        }
        tvNumComments.setText(numCommentText);


        // Default profile picture
        Picasso.with(getActivity()).load(R.drawable.icon_user_32).into(ivProfile);

        if(post.getParseUser()!=null) {
            Picasso.with(getActivity()).load(post.getParseUser().getProfileImgUrl()).into(ivProfile);
        }

        //put static map
        double latitude = post.getLatitude();
        double longitude = post.getLongitude();
        int width = DeviceDimensionsHelper.getDisplayWidth(getActivity());
        width = 600;
        String staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?size="
                + width + "x" + width + "&zoom=17&markers="
                //+ "icon:http://chart.apis.google.com/chart?chst=d_map_pin_icon%26chld=cafe%257C996600%7C"
                + latitude + "," + longitude;
        Picasso.with(getActivity()).load(staticMapUrl)
                .into(ivStaticMap);
    }

    private void setToolbar() {
        if (toolbar != null) {
            ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
            //toolbar.setBackgroundColor(Color.parseColor(user.getProfileBackgroundColor()));

            // Set the home icon on toolbar
            ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_up_menu);
        }
    }

    public void populateImageViewFromLocal(){
        if(localPhotoPath!=null) {

            Log.d(TAG, "local_photo_path : " + localPhotoPath);
            Bitmap takenImage1 = Util.rotateBitmapOrientation(localPhotoPath);
            int screenWidth = DeviceDimensionsHelper.getDisplayWidth(getActivity());
            Bitmap localImage = BitmapScaler.scaleToFitWidth(takenImage1, screenWidth);
            ivPost.setImageBitmap(localImage);
            imageViewLoaded = true;
        }else{
            Log.d(TAG, "local photo path is null");
        }
    }
}
