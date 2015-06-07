package com.mycompany.traveljournal.mainscreen;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListFragment;
import com.mycompany.traveljournal.createscreen.CreatePostActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mapscreen.MapActivity;
import com.mycompany.traveljournal.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.Date;
import java.util.List;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class MainPostFragment extends PostsListFragment {

    private final static String TAG = "MainPostFragmentDebug";
    private Date earliestTimeStamp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setUpListeners();
        populateList();
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

        /*if(m_query!=null){
            m_location = Util.getLocationFromQuery(getActivity(), m_query);
        }*/

        if(m_location==null){

            Log.d(TAG, "no location");
            Post.getRecentPosts(earliestTimeStamp, Util.LIMIT_POST, new FindCallback<Post>() {
                @Override
                public void done(List<Post> resultPosts, ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "success getting posts: " + resultPosts.toString());

                        posts.addAll(resultPosts);
                        if (posts.size() > 0) {
                            earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                        }
                        aPosts.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "parse call failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Failed to get posts");
                    }
                }
            });

        }else{
            Log.d(TAG, "location");
            Post.getPostsWithinMilesOrderByDate(earliestTimeStamp, Util.MAX_POST_SEARCH_DISTANCE,
                    m_location.latitude, m_location.longitude, Util.LIMIT_POST, new FindCallback<Post>() {
                @Override
                public void done(List<Post> resultPosts, ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "success getting posts: " + resultPosts.toString());

                        posts.addAll(resultPosts);
                        if (posts.size() > 0) {
                            earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                        }
                        aPosts.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "parse call failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Failed to get posts");
                    }
                }
            });
        }
    }

    @Override
    public void refreshList() {

        if(m_location==null){

            Log.d(TAG, "no location");
            Post.getRecentPosts(null, Util.LIMIT_POST, new FindCallback<Post>() {
                @Override
                public void done(List<Post> resultPosts, ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "success getting posts: " + resultPosts.toString());

                        aPosts.clear();
                        posts.addAll(resultPosts);
                        if (posts.size() > 0) {
                            earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                        }
                        aPosts.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "parse call failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Failed to get posts");
                    }
                    if(swipeContainer!=null){
                        swipeContainer.setRefreshing(false);
                    }
                }
            });

        }else{
            Log.d(TAG, "location");
            Post.getPostsWithinMilesOrderByDate(null, Util.MAX_POST_SEARCH_DISTANCE,
                    m_location.latitude, m_location.longitude, Util.LIMIT_POST, new FindCallback<Post>() {
                @Override
                public void done(List<Post> resultPosts, ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "success getting posts: " + resultPosts.toString());

                        aPosts.clear();
                        posts.addAll(resultPosts);
                        if (posts.size() > 0) {
                            earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                        }
                        aPosts.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "parse call failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Failed to get posts");
                    }
                    if (swipeContainer != null) {
                        swipeContainer.setRefreshing(false);
                    }
                }
            });
        }
    }

    @Override
    public void populateListOnSearch() {

        if(m_location==null){

            Log.d(TAG, "no location");
            Post.getRecentPosts(null, Util.LIMIT_POST, new FindCallback<Post>() {
                @Override
                public void done(List<Post> resultPosts, ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "success getting posts: " + resultPosts.toString());

                        aPosts.clear();
                        posts.addAll(resultPosts);
                        earliestTimeStamp = null;
                        if (posts.size() > 0) {
                            earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                        }
                        aPosts.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "parse call failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Failed to get posts");
                    }
                }
            });

        }else{
            Log.d(TAG, "location");
            Post.getPostsWithinMilesOrderByDate(null, Util.MAX_POST_SEARCH_DISTANCE,
                    m_location.latitude, m_location.longitude, Util.LIMIT_POST, new FindCallback<Post>() {
                @Override
                public void done(List<Post> resultPosts, ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "success getting posts: " + resultPosts.toString());

                        aPosts.clear();
                        posts.addAll(resultPosts);
                        earliestTimeStamp = null;
                        if (posts.size() > 0) {
                            earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                        }
                        aPosts.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "parse call failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Failed to get posts");
                    }
                }
            });
        }
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
}
