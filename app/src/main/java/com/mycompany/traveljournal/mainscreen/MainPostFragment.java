package com.mycompany.traveljournal.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListFragment;
import com.mycompany.traveljournal.createscreen.CreatePostActivity;
import com.mycompany.traveljournal.profilescreen.ProfileActivity;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class MainPostFragment extends PostsListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setUpListeners();
        return view;
    }


    @Override
    public void setUpListeners() {
        super.setUpListeners();
        mQuickReturnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreatePostActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int footerHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.footer_height);
        QuickReturnListViewOnScrollListener scrollListener;

        scrollListener = new QuickReturnListViewOnScrollListener.Builder(QuickReturnViewType.FOOTER)
                .footer(mQuickReturnView)
                .minFooterTranslation(footerHeight)
                .build();

        scrolls.addScrollListener(scrollListener);
        lvPosts.setOnScrollListener(scrolls);


    }


    @Override
    public void populateList() {

    }

    @Override
    public void refreshList() {

    }
}
