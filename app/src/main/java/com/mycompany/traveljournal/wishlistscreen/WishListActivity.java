package com.mycompany.traveljournal.wishlistscreen;

import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class WishListActivity extends PostsListActivity {

    WishListPostsFragment wishListPostsFragment;

    @Override
    public void setUpFragment() {

        Toast.makeText(this, "Inside", Toast.LENGTH_SHORT).show();

        wishListPostsFragment =  new WishListPostsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, wishListPostsFragment);
        ft.commit();

    }

}
