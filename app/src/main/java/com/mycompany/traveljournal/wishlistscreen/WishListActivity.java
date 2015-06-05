package com.mycompany.traveljournal.wishlistscreen;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;
import com.mycompany.traveljournal.mainscreen.MainPostFragment;

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
