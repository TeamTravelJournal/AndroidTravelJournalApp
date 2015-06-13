package com.mycompany.traveljournal.detailsscreen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.MapFragment;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;
import com.mycompany.traveljournal.createscreen.CreatePostFragment;
import com.mycompany.traveljournal.mapscreen.SingleMapFragment;
import com.mycompany.traveljournal.profilescreen.UserProfileFragment;

public class DetailActivity extends PostsListActivity {

    private static final String TAG = "DetailActivity";
    private final String DETAIL_FRAGMENT_TAG = "detailPostFragment";
    DetailFragment detailFragment;
    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Get Post ID
        postId = getIntent().getStringExtra("post_id");

        if (postId == null) {
            Log.wtf(TAG, "Using default post id");
            postId = "8nxq1SkIUo";
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUpFragment() {
        detailFragment =  DetailFragment.newInstance(postId);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, detailFragment, DETAIL_FRAGMENT_TAG);
        ft.commit();
    }

    public void setUpFragmentFromTag(){
        detailFragment = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);
    }
}
