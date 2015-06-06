package com.mycompany.traveljournal.wishlistscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.traveljournal.base.PostsListFragment;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class WishListPostsFragment extends PostsListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mQuickReturnView.setVisibility(View.GONE);
        return v;
    }

    @Override
    public void setUpHasOptionsMenu() {
        setHasOptionsMenu(false);
    }

    @Override
    public void populateList() {

    }

    @Override
    public void refreshList() {

    }
}
