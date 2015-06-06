package com.mycompany.traveljournal.createscreen;

import android.support.v4.app.FragmentTransaction;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;

/**
 * Created by sjayaram on 6/5/2015.
 */
public class CreatePostActivity extends PostsListActivity {

    CreatePostFragment createPostFragment;

    @Override
    public void setUpFragment() {

        createPostFragment =  new CreatePostFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, createPostFragment);
        ft.commit();

    }

}
