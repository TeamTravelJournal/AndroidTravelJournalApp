package com.mycompany.traveljournal.detailsscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.mapscreen.SingleMapActivity;
import com.mycompany.traveljournal.mapscreen.SingleMapFragment;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DetailFragment extends Fragment {

    private final static String TAG = "DetailFragment";
    private String postId;
    private ImageView ivProfile;
    private ImageView ivPost;
    private TextView tvCaption;
    private ImageView ivShare;
    private ImageView ivFollow;
    private ImageView ivStar;
    private TextView tvLikes;

    public static DetailFragment newInstance(String postId) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("post_id", postId);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        setUpViews(view);
        setUpListeners();
        fetchPostAndPopulateViews();
        //insertNestedFragment();//inserts map fragment
        return view;
    }
/*
    // Embeds the map fragment dynamically
    private void insertNestedFragment() {
        SingleMapFragment childFragment = SingleMapFragment.newInstance(postId);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, childFragment).commit();
        /*SingleMapFragment singleMapFragment = (SingleMapFragment)(getChildFragmentManager().findFragmentById(R.id.single_map));
        singleMapFragment.setData(postId);
    }
*/

    public void setUpViews(View v){
        ivProfile = (ImageView) v.findViewById(R.id.ivProfile);
        ivPost = (ImageView) v.findViewById(R.id.ivPost);
        tvCaption = (TextView) v.findViewById(R.id.tvCaption);
        ivShare = (ImageView) v.findViewById(R.id.ivShare);
        ivFollow = (ImageView) v.findViewById(R.id.ivFollow);
        ivStar = (ImageView) v.findViewById(R.id.ivStar);
        tvLikes = (TextView) v.findViewById(R.id.tvLikes);
    }

    public void setUpListeners() {




        //Using below code for my testing. Pls do not remove/feel free to comment it out. //Esra
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), SingleMapActivity.class);
                i.putExtra("post_id", postId);
                startActivity(i);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postId = getArguments().getString("post_id", "");

        Log.wtf(TAG, "--postid is " + postId);
    }

    private void fetchPostAndPopulateViews() {
        JournalService client = JournalApplication.getClient();
        client.getPostWithId(postId, new JournalCallBack<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                Post post = posts.get(0);
                populateViews(post);
            }
            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Post not found with id "+postId);
            }

        });
    }

    private void populateViews(Post post) {
        Picasso.with(getActivity()).load(post.getImageUrl()).into(ivPost);
        tvCaption.setText(post.getCaption());
        tvLikes.setText(post.getLikes()+" Likes");

        // Default profile picture
        Picasso.with(getActivity()).load(R.drawable.icon_user_32).into(ivProfile);

        if(post.getParseUser()!=null) {
            Picasso.with(getActivity()).load(post.getParseUser().getProfileImgUrl()).into(ivProfile);
        }
    }



//    private void loadImage() {
//
//
//
//        String postId = "6KGn7knDWJ";
//        JournalService client = JournalApplication.getClient();
//        client.getPostWithId(postId, new JournalCallBack<List<Post>>() {
//            @Override
//            public void onSuccess(List<Post> posts) {
//                    Post post = posts.get(0);
//                    Log.wtf(TAG, post.toString());
//
//
//                    post.doWithPhoto(new GetDataCallback() {
//                        String postId = "6KGn7knDWJ";
//
//                        @Override
//                        public void done(byte[] bytes, ParseException e) {
//                            Log.wtf(TAG, "DonE!");
//                            ImageView ivPost = (ImageView) getActivity().findViewById(R.id.ivPost);
//
//
//                            try {
//                                //File f = new File(getActivity().getCacheDir(), "somefilename");
//                                File f = new File(getActivity().getCacheDir(), postId);
//                                f.createNewFile();
//                                FileOutputStream fos = new FileOutputStream(f);
//                                fos.write(bytes);
//                                fos.flush();
//                                fos.close();
//                                Picasso.with(getActivity()).load(f).into(ivPost);
//                                Log.wtf(TAG, "YES!!");
//                            } catch (IOException e1) {
//                                e1.printStackTrace();
//                                Log.wtf(TAG, "NO="+e.toString());
//                            }
//
//                            //Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                            //Picasso.with(getActivity()).load(bmp).into(ivPost);
//                        }
//                    });
//
//
//                }
//              @Override
//              public void onFailure(Exception e) {
//                  Log.wtf(TAG, "Post not found");
//              }
//        });
//    }

}
