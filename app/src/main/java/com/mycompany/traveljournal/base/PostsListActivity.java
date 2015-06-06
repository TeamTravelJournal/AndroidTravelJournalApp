package com.mycompany.traveljournal.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.datasource.ParseClient;

public abstract class PostsListActivity extends ActionBarActivity {

    private final static String TAG = "PostsListActivity";
    ParseClient parseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Init Parse
        parseClient = ParseClient.getInstance(this);

        if(savedInstanceState == null)
            setUpFragment();
    }

    public abstract void setUpFragment();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
