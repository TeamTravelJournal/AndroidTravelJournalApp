package com.mycompany.traveljournal.commentscreen;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.datasource.ParseClient;

public class CommentActivity extends ActionBarActivity {

    private static final String TAG = "CommentActivity";
    String postId;
    ParseClient parseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // Get Post ID
        postId = getIntent().getStringExtra("post_id");

        if (postId == null) {
            Log.wtf(TAG, "Using default post id");
            postId = "8nxq1SkIUo";
        }


        // Init Parse
        parseClient = ParseClient.getInstance(this);

        if(savedInstanceState == null) {
            setUpFragment();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void setUpFragment() {
        CommentFragment commentFragment =  CommentFragment.newInstance(postId);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, commentFragment);
        ft.commit();
    }

}
