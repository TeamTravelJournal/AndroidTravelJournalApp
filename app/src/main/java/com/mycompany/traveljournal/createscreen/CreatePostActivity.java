package com.mycompany.traveljournal.createscreen;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;

/**
 * Created by sjayaram on 6/5/2015.
 */
public class CreatePostActivity extends PostsListActivity {

    CreatePostFragment createPostFragment;
    String photoPath;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void setUpFragment() {
        photoPath = getIntent().getStringExtra(MediaStore.EXTRA_OUTPUT);
        createPostFragment =  CreatePostFragment.newInstance(photoPath);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, createPostFragment);
        ft.commit();

    }

}
