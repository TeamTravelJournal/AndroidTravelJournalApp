package com.mycompany.traveljournal.commentscreen;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.traveljournal.R;

public class CommentFragment extends Fragment {
    private final static String TAG = "CommentFragment";
    private String postId;
    private Toolbar toolbar;

    public static CommentFragment newInstance(String postId) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString("post_id", postId);
        commentFragment.setArguments(args);
        return commentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        setUpViews(view);
        setToolbar();
        setUpListeners();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getArguments().getString("post_id", "");
    }

    public void setUpViews(View v) {

    }

    public void setUpListeners() {

    }

    private void setToolbar() {
        if (toolbar != null) {
            ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
            //toolbar.setBackgroundColor(Color.parseColor(user.getProfileBackgroundColor()));

            // Set the home icon on toolbar
            ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_up_menu);
        }
    }
}
