package com.mycompany.traveljournal.mainscreen;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListFragment;
import com.mycompany.traveljournal.createscreen.CameraActivity;
import com.mycompany.traveljournal.createscreen.CreatePostActivity;
import android.view.MenuItem;

import com.mycompany.traveljournal.mapscreen.MapActivity;

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
                Intent i = new Intent(getActivity(), CameraActivity.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_mapview){

            executeMapIntent(m_query);
        }

        return super.onOptionsItemSelected(item);
    }

    private void executeMapIntent(String query){
        Intent i = new Intent(getActivity(), MapActivity.class);
        i.putExtra("query", query);
        startActivity(i);
    }

    public void onSearch(String query){

    }

}
