package com.mycompany.traveljournal.detailsscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DetailFragment extends Fragment {

    private final static String TAG = "DetailFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        setUpViews(view);
        setUpListeners();


        testPostWithImage();


        return view;
    }



    public void setUpViews(View v){
    }

    public void setUpListeners() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void testPostWithImage() {

        String postId = "SZdAAxPZKf";
        JournalService client = JournalApplication.getClient();
        client.getPostWithId(postId, new JournalCallBack<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                Post post = posts.get(0);

                ImageView ivPost = (ImageView) getActivity().findViewById(R.id.ivPost);
                Picasso.with(getActivity()).load(post.getImageUrl()).into(ivPost);
            }
            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Post not found");
            }
        });






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
