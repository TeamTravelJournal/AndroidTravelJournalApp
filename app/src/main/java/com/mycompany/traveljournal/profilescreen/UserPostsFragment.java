package com.mycompany.traveljournal.profilescreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mycompany.traveljournal.base.PostsListFragment;
import com.mycompany.traveljournal.detailsscreen.DetailFragment;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalCallBack;

import java.util.Date;
import java.util.List;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class UserPostsFragment extends PostsListFragment {

    private final static String TAG = "UserPostsFragment";
    private Date earliestTimeStamp = null;
    private String userId;

    public static UserPostsFragment newInstance(String userId) {
        UserPostsFragment userPostsFragment = new UserPostsFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        userPostsFragment.setArguments(args);
        return userPostsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mQuickReturnView.setVisibility(View.GONE);
        populateList();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("userId", "");
    }

    public void setUpHasOptionsMenu(){
        setHasOptionsMenu(false);
    }

    @Override
    public void populateList() {

        client.getPostsForUser(userId, earliestTimeStamp, Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
            @Override
            public void onSuccess(List<Post> resultPosts) {
                Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "success getting posts: " + resultPosts.toString());

                posts.addAll(resultPosts);
                if (posts.size() > 0) {
                    earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                }
                aPosts.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), "parse call failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to get posts");
            }
        });

    }

    @Override
    public void populateListOnSearch(){

    }

    @Override
    public void refreshList() {
        swipeContainer.setRefreshing(false);
    }

}
