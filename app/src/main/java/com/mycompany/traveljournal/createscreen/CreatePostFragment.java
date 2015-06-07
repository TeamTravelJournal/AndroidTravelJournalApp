package com.mycompany.traveljournal.createscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.helpers.Util;

/**
 * Created by sjayaram on 6/5/2015.
 */
public class CreatePostFragment extends Fragment {

    ImageView ivPreview;
    ImageView ivCamera;

    public static CreatePostFragment newInstance(String photoPath){
        CreatePostFragment createPostFragment = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString("photoPath", photoPath);
        createPostFragment.setArguments(args);
        return createPostFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_create, container, false);
        setUpViews(view);
        setUpListeners();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setUpListeners(){
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CameraActivity.class);
                startActivity(i);
            }
        });
    }

    public void setUpViews(View v){
        Uri photoPathUri = Util.getPhotoFileUri(getArguments().getString("photoPath"));
        Bitmap takenImage = BitmapFactory.decodeFile(photoPathUri.getPath());
        // Load the taken image into a preview
        ivPreview = (ImageView) v.findViewById(R.id.ivPreview);
        ivCamera = (ImageView) v.findViewById(R.id.ivCamera);
        ivPreview.setImageBitmap(takenImage);
    }

}
