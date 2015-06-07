package com.mycompany.traveljournal.detailsscreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.datasource.ImageUploader;
import com.mycompany.traveljournal.examples.ExampleSavePostToParse;

import java.io.ByteArrayOutputStream;


public class DetailFragment extends Fragment {

    private final static String TAG = "DetailFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        setUpViews(view);
        setUpListeners();

        Log.wtf(TAG, "1");
        //ExampleGetPostsFromParse.getPostWithImage();


//        //testImage();
//
//        ImageView ivPost = (ImageView) view.findViewById(R.id.ivPost);
//        String url = "http://files.parsetfss.com/0ac0f4de-204e-49bb-8e25-e6937c3c11ae/tfss-0f0fdd9a-585b-47c4-94f9-9a9528be464e-file";
//        Picasso.with(getActivity()).load(url).into(ivPost);

        ExampleSavePostToParse.uploadPhotoToPost(getActivity());


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

    private void testImage() {
        //get byte array
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.coffee);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        ImageUploader uploader = new ImageUploader("1", byteArray);
        uploader.upload();





    }

//    private void loadImage() {
//
//
//
//        String postId = "6KGn7knDWJ";
//        Post.getPostWithId(postId, new FindCallback<Post>() {
//            @Override
//            public void done(List<Post> posts, ParseException e) {
//                if (e == null) {
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
//                } else {
//                    Log.wtf(TAG, "Post not found");
//                }
//            }
//        });
//    }

}
