package com.mycompany.traveljournal.detailsscreen;

import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;

/**
 * Created by sjayaram on 6/5/2015.
 */
public class DetailActivity extends PostsListActivity {

    DetailFragment detailFragment;

    @Override
    public void setUpFragment() {

        getSupportActionBar().hide();
        Toast.makeText(this, "Inside Detail", Toast.LENGTH_SHORT).show();
        detailFragment =  new DetailFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, detailFragment);
        ft.commit();

    }
}
