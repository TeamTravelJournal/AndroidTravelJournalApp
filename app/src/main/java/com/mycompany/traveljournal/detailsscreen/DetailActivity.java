package com.mycompany.traveljournal.detailsscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;
import com.mycompany.traveljournal.commentscreen.CommentActivity;

public class DetailActivity extends PostsListActivity implements DetailFragment.OpenCommentsListenerInterface {

    private static final String TAG = "DetailActivity";
    private final String DETAIL_FRAGMENT_TAG = "detailPostFragment";
    DetailFragment detailFragment;
    String postId;
    String localPhotoPath;
    private final int REQUEST_CODE_OPEN_COMMENTS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Get Post ID
        postId = getIntent().getStringExtra("post_id");
        localPhotoPath = getIntent().getStringExtra("local_photo_path");

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
        detailFragment =  DetailFragment.newInstance(postId, localPhotoPath);
        detailFragment.setListener(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, detailFragment, DETAIL_FRAGMENT_TAG);
        ft.commit();
    }

    public void setUpFragmentFromTag(){
        detailFragment = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_OPEN_COMMENTS) {
            boolean newCommentCreated = data.getExtras().getBoolean("new_comment_created");
            if (newCommentCreated) {
                detailFragment.refreshComments();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void openCommentsScreen(String postId) {
        Intent i = new Intent(DetailActivity.this, CommentActivity.class);
        i.putExtra("post_id", postId);
        startActivityForResult(i, REQUEST_CODE_OPEN_COMMENTS);
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
