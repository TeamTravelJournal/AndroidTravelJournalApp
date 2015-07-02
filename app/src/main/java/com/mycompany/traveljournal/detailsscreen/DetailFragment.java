package com.mycompany.traveljournal.detailsscreen;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.ImageAdapter;
import com.mycompany.traveljournal.base.TravelBaseFragment;
import com.mycompany.traveljournal.commentscreen.CommentsAdapter;
import com.mycompany.traveljournal.helpers.BitmapScaler;
import com.mycompany.traveljournal.helpers.DeviceDimensionsHelper;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mapscreen.SingleMapActivity;
import com.mycompany.traveljournal.models.Comment;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;
import com.mycompany.traveljournal.models.YelpBusiness;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.mycompany.traveljournal.yelp.Yelp;
import com.mycompany.traveljournal.yelp.YelpBusinessAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DetailFragment extends TravelBaseFragment {

    private final static String TAG = "DetailFragment";
    private final static int numComments = 3;
    private String postId;
    private ImageView ivProfile;
    //private ImageView ivPost;
    private TextView tvCaption;
    private ImageView ivShare;
    private ImageView ivSharePhoto;
    private ImageView ivFollow;
    private ImageView ivLikes;
    private ImageView ivComment;
    private TextView tvLikes;
    private TextView tvName;
    private Toolbar toolbar;

    private ImageView ivStaticMap;
    private Post m_post;
    private User m_user;
    private TextView tvNumComments;
    private String localPhotoPath;
    private String galleryPhotoPath;
    private boolean imageViewLoaded = false;
    private ViewPager viewPager;
    com.nirhart.parallaxscroll.views.ParallaxScrollView parallaxScrollView;

    private CommentsAdapter aComments;
    private LinearLayout llComments;
    JournalService client;
    private ArrayList<String> images = new ArrayList<>();
    private boolean isShareEnabled = true;

    private OpenCommentsListenerInterface openCommentsListener;
    private ImageView ivHeartInside;
    private ImageView ivHeartOutside;
    private TextView tvCity;
    private TextView tvDate;
    private ImageView ivSmileyInside;
    private ImageView ivSmileyOutside;
    private ImageView ivSadFaceInside;
    private ImageView ivSadFaceOutside;
    private TextView tvNumFollowers;

    /*private YelpBusinessAdapter aYelpBiz;
    private ArrayList<YelpBusiness> yelpBiz;
    private ListView lvYelpBiz;*/

    public static DetailFragment newInstance(String postId, String localPhotoPath, String galleryPhotoPath) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("post_id", postId);
        args.putString("local_photo_path", localPhotoPath);
        args.putString("gallery_photo_path", galleryPhotoPath);
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
        //ivPost = (ImageView) v.findViewById(R.id.ivPost);
        tvCaption = (TextView) v.findViewById(R.id.tvCaption);
        //ivShare = (ImageView) v.findViewById(R.id.ivShare);
        ivSharePhoto = (ImageView) v.findViewById(R.id.ivSharePhoto);
        ivFollow = (ImageView) v.findViewById(R.id.ivFollow);
        ivLikes = (ImageView) v.findViewById(R.id.ivLikes);
        ivComment = (ImageView) v.findViewById(R.id.ivComment);
        tvLikes = (TextView) v.findViewById(R.id.tvLikes);
        tvName = (TextView) v.findViewById(R.id.tvUserName);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ivStaticMap = (ImageView)v.findViewById(R.id.ivStaticMap);
        tvNumComments = (TextView) v.findViewById(R.id.tvNumComments);
        llComments = (LinearLayout) v.findViewById(R.id.llComments);

        viewPager = (ViewPager) v.findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new CubeOutTransformer());

        parallaxScrollView = (com.nirhart.parallaxscroll.views.ParallaxScrollView) v.findViewById(R.id.parallaxScrollView);
        tvName = (TextView) v.findViewById(R.id.tvName);

        ivHeartInside = (ImageView) v.findViewById(R.id.ivHeartInside);
        ivHeartOutside = (ImageView) v.findViewById(R.id.ivHeartOutside);
        tvCity = (TextView) v.findViewById(R.id.tvCity);
        tvDate = (TextView) v.findViewById(R.id.tvDate);
        ivSmileyInside = (ImageView) v.findViewById(R.id.ivSmileyInside);
        ivSmileyOutside = (ImageView) v.findViewById(R.id.ivSmileyOutside);
        ivSadFaceInside = (ImageView) v.findViewById(R.id.ivSadFaceInside);
        ivSadFaceOutside = (ImageView) v.findViewById(R.id.ivSadFaceOutside);
        tvNumFollowers = (TextView) v.findViewById(R.id.tvNumFollowers);

        /*lvYelpBiz = (ListView) v.findViewById(R.id.lvYelpBusinesses);
        yelpBiz = new ArrayList<>();
        aYelpBiz = new YelpBusinessAdapter(getActivity(), yelpBiz);
        lvYelpBiz.setAdapter(aYelpBiz);*/

        super.setUpViews(v);
    }

    public void setUpListeners() {

        //Once clicked to map, go to single map activity for that post
        ivStaticMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SingleMapActivity.class);
                i.putExtra("post_id", postId);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCommentsListener.openCommentsScreen(postId);
            }
        });

        /*ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePost();
            }
        });*/

        ivFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!m_user.getIsFollowed()) {
                    // currently not following: action is to follow
                    m_user.setIsFollowed(true);
                    //change icon to unfollow
                    ivFollow.setImageDrawable(getResources().getDrawable(R.drawable.unfollow_detail));
                    //increment follower count
                    tvNumFollowers.setText(Integer.parseInt(tvNumFollowers.getText().toString()) + 1 + "");
                    animateSmiley();

                } else {
                    // currently following: action is to unfollow
                    m_user.setIsFollowed(false);
                    ivFollow.setImageDrawable(getResources().getDrawable(R.drawable.follow_detail));
                    tvNumFollowers.setText(Integer.parseInt(tvNumFollowers.getText().toString()) - 1 + "");
                    animateSad();
                }
            }
        });

        ivLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_post.isLiked()){
                    // before liked, now unlike it
                    // put image to show ability to like again
                    // decrement like count
                    Log.d(TAG, "post is previously liked");
                    m_post.setLiked(false);
                    ivLikes.setImageDrawable(getResources().getDrawable(R.drawable.unfavorite_detail));
                    tvLikes.setText(Integer.parseInt(tvLikes.getText().toString()) - 1 + "");
                }else{
                    // before unliked, now like it
                    // put image to show ability to unlike again
                    // increment like count
                    Log.d(TAG, "post is previously unliked");
                    m_post.setLiked(true);
                    ivLikes.setImageDrawable(getResources().getDrawable(R.drawable.favorite_detail));
                    tvLikes.setText(Integer.parseInt(tvLikes.getText().toString()) + 1 + "");
                    animateHearts();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getActivity().supportFinishAfterTransition();
                return true;
            case R.id.action_share:
                sharePost();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        postId = getArguments().getString("post_id", "");
        localPhotoPath = getArguments().getString("local_photo_path");
        galleryPhotoPath = getArguments().getString("gallery_photo_path");

        Log.wtf(TAG, "post_id is : " + postId + " , local_photo_path is : " + localPhotoPath + ", gallery photo path is : " + galleryPhotoPath);

        client = JournalApplication.getClient();
    }

    private void fetchPostAndPopulateViews() {

        showProgress();

        client.getPostWithId(postId, new JournalCallBack<Post>() {
            @Override
            public void onSuccess(Post post) {
                m_post = post;
                m_user = post.getParseUser();
                populateViews(post);
                fetchAndPopulateComments(post, false);
                populateYelpRestaurants(post);
                hideProgress();
            }

            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Post not found with id " + postId);
                hideProgress();
            }

        });
    }


    private void populateViews(Post post) {
        if(!imageViewLoaded) {
            images.add(post.getImageUrl());
            //TODO get hardcoded image list from parse
            //images.add("https://canadaalive.files.wordpress.com/2013/03/2789604382_1920dbcc87.jpg");
            //images.add("http://www.specialswallpaper.com/wp-content/uploads/2015/04/free_high_resolution_images_for_download-1.jpg");
            //images.add("http://www.hdwallpapersimages.com/wp-content/uploads/2014/01/Winter-Tiger-Wild-Cat-Images.jpg");
            if(post.getImage1Url() != null)
                images.add(post.getImage1Url());

            if(post.getImage2Url() != null)
                images.add(post.getImage2Url());

            if(post.getImage3Url() != null)
                images.add(post.getImage3Url());

            ImageAdapter adapter = new ImageAdapter(getActivity(), images, null);
            viewPager.setAdapter(adapter);
         }

        tvCaption.setText(post.getCaption());
        tvLikes.setText(post.getLikes()+"");
        tvNumFollowers.setText("3");
        tvName.setText(post.getParseUser().getName());
        tvCity.setText(post.getCity());

        SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy", Locale.ENGLISH);
        String date = format.format(post.getCreatedAt());
        tvDate.setText(date);

        // Default profile picture
        Picasso.with(getActivity()).load(R.drawable.icon_user_32)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholderthumbnail)
                .transform(Util.getTransformation(40))
                .into(ivProfile);

        if(post.getParseUser()!=null) {
            Picasso.with(getActivity()).load(post.getParseUser().getProfileImgUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholderthumbnail)
                    .transform(Util.getTransformation(40))
                    .into(ivProfile);
        }

        //put static map
        double latitude = post.getLatitude();
        double longitude = post.getLongitude();
        int width = 600;
        String staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?size="
                + width + "x" + width + "&zoom=17&markers="
                //+ "icon:http://chart.apis.google.com/chart?chst=d_map_pin_icon%26chld=cafe%257C996600%7C"
                + latitude + "," + longitude;
        Picasso.with(getActivity()).load(staticMapUrl)
                .into(ivStaticMap);

        //Load image into placeholder to enable image sharing
        Picasso.with(getActivity()).load(m_post.getImageUrl()).into(ivSharePhoto);
    }

    private void populateNumComments(int numComments) {
        tvNumComments.setText(numComments+"");

    }

    private void setToolbar() {
        if (toolbar != null) {
            ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
            //toolbar.setBackgroundColor(Color.parseColor(user.getProfileBackgroundColor()));

            // Set the home icon on toolbar
            ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            //actionbar.setHomeAsUpIndicator(R.drawable.ic_up_menu);
        }
    }

    private void fetchAndPopulateComments(Post post, final boolean showProgress) {
        if (showProgress) {
            showProgress();
        }

        client.getCommentsForPost(post.getPostID(), 1000, new JournalCallBack<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> comments) {
                Log.wtf(TAG, "Got comments #="+comments.size());
                addAllCommentsToList(comments);
                populateNumComments(comments.size());

                if (showProgress) {
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Failed to get comments:"+e.toString());
                if (showProgress) {
                    hideProgress();
                }
            }
        });
    }

    public void populateImageViewFromLocal(){
        if(localPhotoPath!=null) {

            Log.d(TAG, "local_photo_path : " + localPhotoPath);
            int screenWidth = DeviceDimensionsHelper.getDisplayWidth(getActivity());
            int screenHeight = DeviceDimensionsHelper.getDisplayHeight(getActivity());

            Bitmap takenImage1 = Util.rotateBitmapOrientation(localPhotoPath, screenWidth/3, screenHeight/3);

            Bitmap localImage = BitmapScaler.scaleToFitWidth(takenImage1, screenWidth);
            ArrayList<String> fakeImg = new ArrayList<String>();
            fakeImg.add("");

            takenImage1 = null;

            ImageAdapter adapter = new ImageAdapter(getActivity(), fakeImg , localImage);
            viewPager.setAdapter(adapter);
            imageViewLoaded = true;
        }
        else if(galleryPhotoPath!=null) {

            Log.d(TAG, "gallery_photo_path : " + galleryPhotoPath);

            Bitmap selectedImage=null;
            try{
                File file = new File(galleryPhotoPath);
                Uri uri = Uri.fromFile(file);
                //Uri uri = Uri.parse("content:/" + galleryPhotoPath);

                Bitmap selectedImage1 = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri);

                int screenWidth = DeviceDimensionsHelper.getDisplayWidth(getActivity());
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

                ArrayList<String> fakeImg = new ArrayList<String>();
                fakeImg.add("");
                ImageAdapter adapter = new ImageAdapter(getActivity(), fakeImg , selectedImage);
                viewPager.setAdapter(adapter);
                imageViewLoaded = true;
            }else{
                Log.d(TAG, "selected image is null");
            }
        }else{
            Log.d(TAG, "local/gallery photo path is null");
        }
    }

    public void addAllCommentsToList(List<Comment> comments) {
        if(getActivity()!=null){
            ViewGroup llComments = (ViewGroup) getActivity().findViewById(R.id.llComments);

            // Remove existing comments before proceeding
            llComments.removeAllViews();

            int numCommentsToShow = numComments;
            int numComments = comments.size();
            if (numComments < numCommentsToShow) {

                // Show subset of comments
                numCommentsToShow = numComments;
                for (int i = 0 ; i < numCommentsToShow ; i++ ){
                    addSingleCommentToList(comments.get(i), llComments);
                }
            } else {

                // Show max number of comments
                for (int i = numComments - numCommentsToShow ; i < numComments ; i++ ){
                    addSingleCommentToList(comments.get(i), llComments);
                }
            }

        }else{
            Log.d(TAG, "getActivity returns null");
        }
    }

    private void addSingleCommentToList(Comment comment, ViewGroup viewGroup) {
        LayoutInflater i = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View commentDetailView = i.inflate(R.layout.fragment_comment_detail, null);

        TextView tvName = (TextView) commentDetailView.findViewById(R.id.tvName);
        TextView tvBody = (TextView) commentDetailView.findViewById(R.id.tvBody);
        ImageView ivProfileImage = (ImageView) commentDetailView.findViewById(R.id.ivProfileImage);

        tvName.setText(comment.getUser().getName());
        tvBody.setText(comment.getBody());

        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getActivity()).load(comment.getUser().getProfileImgUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholderthumbnail)
                .transform(Util.getTransformation(30))//radius = half of image size
                .into(ivProfileImage);

        viewGroup.addView(commentDetailView);
    }

    public interface OpenCommentsListenerInterface {
        public void openCommentsScreen(String postId);
    }

    public void setListener(OpenCommentsListenerInterface openCommentsListener) {
        this.openCommentsListener = openCommentsListener;
    }

    public void refreshComments(int numNewComments) {
        // Clear Existing Comments
        //ViewGroup llComments = (ViewGroup) getActivity().findViewById(R.id.llComments);
        //llComments.removeAllViews();

        fetchAndPopulateComments(m_post, true);

        //Update number of comments
        Log.wtf(TAG, "refreshing comments, adding "+numNewComments);

        int numComments = Integer.parseInt(tvNumComments.getText().toString());
        numComments += numNewComments;
        tvNumComments.setText(numComments+"");
    }

    // Mostly copied from http://guides.codepath.com/android/Sharing-Content-with-Intents
    // Can be triggered by a view event such as a button press
    //
    // This requires the photo be loaded into an image view, hence the need for the invisible ivSharePhoto
    //
    public void sharePost() {
        if (!isShareEnabled) {
            return;
        }
        isShareEnabled = false;

        // Get access to bitmap image from view
        ImageView ivImage = (ImageView) getActivity().findViewById(R.id.ivSharePhoto);
        // Get access to the URI for the bitmap
        Uri bmpUri = Util.getLocalBitmapUri(ivImage);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
            isShareEnabled = true;
        } else {
            // ...sharing failed, handle error
            isShareEnabled = true;
        }
    }

    private void animateHearts(){

        Log.d(TAG, "starting heart animation");

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(ivHeartInside, "alpha", 0.4f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(ivHeartOutside, "alpha", 0.2f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(ivHeartInside, "scaleX", 0.2f, 1.0f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(ivHeartInside, "scaleY", 0.2f, 1.0f)
                        .setDuration(1000)
        );
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(ivHeartInside, "alpha", 0.0f)
                        .setDuration(0),
                ObjectAnimator.ofFloat(ivHeartOutside, "alpha", 0.0f)
                        .setDuration(0));
        AnimatorSet set3 = new AnimatorSet();
        set3.playSequentially(set, animatorSet);
        set3.start();
    }

    private void animateSmiley(){

        Log.d(TAG, "starting smiley animation");

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(ivSmileyInside, "alpha", 0.4f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(ivSmileyOutside, "alpha", 0.2f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(ivSmileyInside, "scaleX", 0.2f, 1.0f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(ivSmileyInside, "scaleY", 0.2f, 1.0f)
                        .setDuration(1000)
        );
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(ivSmileyInside, "alpha", 0.0f)
                        .setDuration(0),
                ObjectAnimator.ofFloat(ivSmileyOutside, "alpha", 0.0f)
                        .setDuration(0));
        AnimatorSet set3 = new AnimatorSet();
        set3.playSequentially(set, animatorSet);
        set3.start();
    }

    private void animateSad(){

        Log.d(TAG, "starting sad animation");

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(ivSadFaceInside, "alpha", 0.4f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(ivSadFaceOutside, "alpha", 0.2f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(ivSadFaceInside, "scaleX", 0.2f, 1.0f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(ivSadFaceInside, "scaleY", 0.2f, 1.0f)
                        .setDuration(1000)
        );
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(ivSadFaceInside, "alpha", 0.0f)
                        .setDuration(0),
                ObjectAnimator.ofFloat(ivSadFaceOutside, "alpha", 0.0f)
                        .setDuration(0));
        AnimatorSet set3 = new AnimatorSet();
        set3.playSequentially(set, animatorSet);
        set3.start();
    }

    private void populateYelpRestaurants(final Post post) {

        new AsyncTask<Void, Void, ArrayList<YelpBusiness>>() {
            @Override
            protected ArrayList<YelpBusiness> doInBackground(Void... params) {
                Yelp yelp = Yelp.getYelp(getActivity());
                String jsonStuff = yelp.search(post.getCity());
                try {
                    Log.d(TAG, "json from yelp: " + jsonStuff);
                    JSONObject json = new JSONObject(jsonStuff);
                    JSONArray businesses = json.getJSONArray("businesses");
                    return YelpBusiness.fromJsonArray(businesses);
                } catch (JSONException e) {
                    Log.d(TAG, "json exception: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<YelpBusiness> businesses) {

                if(businesses!=null){

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < businesses.size(); i++) {
                        sb.append(businesses.get(i).toStringShort());
                    }
                    tvCaption.setText(sb.toString());

                    addAllYelpBizToList(businesses);

                    //yelpBiz.addAll(businesses);
                    //aYelpBiz.notifyDataSetChanged();
                    //setListViewHeightBasedOnChildren(lvYelpBiz);
                }
            }
        }.execute();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void addAllYelpBizToList(List<YelpBusiness> biz) {
        if(getActivity()!=null){
            ViewGroup llYelpBusinesses = (ViewGroup) getActivity().findViewById(R.id.llYelpBusinesses);
            int max = biz.size();
            if(max>3){
                max =3;
            }
            for (int i = 0 ; i < max ; i++ ){
                addSingleBusinessToList(biz.get(i), llYelpBusinesses);
            }
        }
        else{
            Log.d(TAG, "getActivity returns null");
        }
    }

    private void addSingleBusinessToList(YelpBusiness yelpBusiness, ViewGroup viewGroup) {
        LayoutInflater i = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View yelpBusinessDetailView = i.inflate(R.layout.item_yelp_business, null);

        ImageView ivBusinessImage = (ImageView) yelpBusinessDetailView.findViewById(R.id.ivBusinessImage);
        TextView tvBusinessName = (TextView) yelpBusinessDetailView.findViewById(R.id.tvBusinessName);
        TextView tvSnippet = (TextView) yelpBusinessDetailView.findViewById(R.id.tvSnippet);
        TextView tvRating = (TextView) yelpBusinessDetailView.findViewById(R.id.tvRating);
        ImageView ivRating = (ImageView) yelpBusinessDetailView.findViewById(R.id.ivRating);

        tvBusinessName.setText(yelpBusiness.getName());
        tvSnippet.setText(yelpBusiness.getSnippet_text());
        tvRating.setText(yelpBusiness.getRating() + "");

        ivBusinessImage.setImageResource(android.R.color.transparent);
        Picasso.with(getActivity()).load(yelpBusiness.getImage_url())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholderthumbnail)
                .transform(Util.getTransformation(30))//give radius as half of the size of the image
                .into(ivBusinessImage);

        ivRating.setImageResource(android.R.color.transparent);
        Picasso.with(getActivity()).load(yelpBusiness.getRating_img_url_small())
                .fit()
                .placeholder(R.drawable.ratings_placeholder)
                .into(ivRating);

        viewGroup.addView(yelpBusinessDetailView);
    }
}
