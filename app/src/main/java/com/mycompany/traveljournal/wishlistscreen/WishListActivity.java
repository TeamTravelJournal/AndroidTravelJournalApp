package com.mycompany.traveljournal.wishlistscreen;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class WishListActivity extends PostsListActivity {

    WishListPostsFragment wishListPostsFragment;

    @Override
    public void setUpFragmentFromTag() {

    }

    @Override
    public void setUpFragment() {

        getSupportActionBar().hide();

        //Toast.makeText(this, "Inside", Toast.LENGTH_SHORT).show();

        wishListPostsFragment =  new WishListPostsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, wishListPostsFragment);
        ft.commit();

    }

}
