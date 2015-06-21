package com.mycompany.traveljournal.mainscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnRecyclerViewOnScrollListener;
import com.etiennelawlor.quickreturn.library.utils.QuickReturnUtils;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListFragment;
import com.mycompany.traveljournal.common.MyCustomReceiver;
import com.mycompany.traveljournal.createscreen.CreatePostActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mapscreen.MapActivity;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalCallBack;
import java.util.Date;
import java.util.List;

/**
 * Created by sjayaram on 6/4/2015.
 * This code is managed by ekucukog
 */
public class MainPostFragment extends PostsListFragment {

    private final static String TAG = "MainPostFragmentDebug";
    private Date latestDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setUpListeners();
        populateList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter("com.mycompany.traveljournal.detailsscreen.MainPostFragment");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(testReceiver, filter);
        // or `registerReceiver(testReceiver, filter)` for a normal broadcast
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(testReceiver);
        // or `unregisterReceiver(testReceiver)` for a normal broadcast
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            client.getLatestPosts(latestDate, Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
                @Override
                public void onSuccess(List<Post> resultPosts) {
                    Log.d(TAG, "success getting posts: " + resultPosts.toString());

                    if (resultPosts.size() > 0) {
                        posts.addAll(0, resultPosts);
                        latestDate = posts.get(0).getCreatedAt();
                        aPosts.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "New Posts.", Toast.LENGTH_LONG).show();
                        //ivNewPosts.setVisibility(View.VISIBLE);
                        tvNewPosts.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "Failed to get posts");
                }

            });

        }
    };


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

        tvNewPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvPosts.smoothScrollToPosition(0);
                tvNewPosts.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int footerHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.footer_height);
        int headerHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.footer_height);

        QuickReturnRecyclerViewOnScrollListener scrollListener;

        scrollListener = new QuickReturnRecyclerViewOnScrollListener.Builder(QuickReturnViewType.FOOTER)
                .footer(mQuickReturnView)
                .minFooterTranslation(footerHeight)
                .build();

        scrolls.addScrollListener(scrollListener);

        int indicatorHeight =  QuickReturnUtils.dp2px(getActivity(), 4);
        int headerTranslation = -headerHeight + indicatorHeight;
        QuickReturnRecyclerViewOnScrollListener scrollListener1 = new QuickReturnRecyclerViewOnScrollListener.Builder(QuickReturnViewType.HEADER)
                .header(toolbar)
                .minHeaderTranslation(headerTranslation)
                .build();

        //scrolls.addScrollListener(scrollListener1);

        lvPosts.setOnScrollListener(scrolls);
    }


    @Override
     public void populateList() {

        if(m_location==null){

            Log.d(TAG, "no location");
            client.getRecentPosts(earliestTimeStamp, Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
                @Override
                public void onSuccess(List<Post> resultPosts){
                    Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "success getting posts: " + resultPosts.toString());

                    //TODO review code
                    if(posts.size()==0)
                    {
                        if(resultPosts.size()>0)
                        {
                            latestDate = resultPosts.get(0).getCreatedAt();
                        }
                    }

                    posts.addAll(resultPosts);
                    if (posts.size() > 0) {
                        earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                    }

                    aPosts.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Exception e){
                    Toast.makeText(getActivity(), "parse call failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Failed to get posts");
                }
            });

        }else{
            Log.d(TAG, "location");
            client.getPostsWithinMilesOrderByDate(earliestTimeStamp, Util.MAX_POST_SEARCH_DISTANCE, m_location.latitude, m_location.longitude, Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
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
    }

    @Override
    public void refreshList() {

        if(m_location==null){

            Log.d(TAG, "no location");
            client.getRecentPosts(null, Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
                @Override
                public void onSuccess(List<Post> resultPosts) {
                    Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "success getting posts: " + resultPosts.toString());

                    //aPosts.clear();
                    posts.clear();
                    posts.addAll(resultPosts);

                    //TODO review code
                    if (posts.size() > 0)
                    {
                        earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                        latestDate = posts.get(0).getCreatedAt();
                    }

                    aPosts.notifyDataSetChanged();

                    if(swipeContainer!=null){
                        swipeContainer.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getActivity(), "parse call failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Failed to get posts");
                    if(swipeContainer!=null){
                        swipeContainer.setRefreshing(false);
                    }
                }
            });

        }else{
            Log.d(TAG, "location");
            client.getPostsWithinMilesOrderByDate(null, Util.MAX_POST_SEARCH_DISTANCE, m_location.latitude, m_location.longitude, Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
                @Override
                public void onSuccess(List<Post> resultPosts) {

                    Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "success getting posts: " + resultPosts.toString());

                    //aPosts.clear();
                    posts.clear();
                    posts.addAll(resultPosts);
                    if (posts.size() > 0) {
                        earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                    }
                    aPosts.notifyDataSetChanged();
                    if (swipeContainer != null) {
                        swipeContainer.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getActivity(), "parse call failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Failed to get posts");
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
            client.getRecentPosts(null, Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
                @Override
                public void onSuccess(List<Post> resultPosts) {
                    Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "success getting posts: " + resultPosts.toString());

                    //aPosts.clear();
                    posts.clear();
                    posts.addAll(resultPosts);
                    earliestTimeStamp = null;
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

        }else{
            Log.d(TAG, "location");
            client.getPostsWithinMilesOrderByDate(null, Util.MAX_POST_SEARCH_DISTANCE, m_location.latitude, m_location.longitude, Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
                @Override
                public void onSuccess(List<Post> resultPosts){

                    Toast.makeText(getActivity(), "parse call successful", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "success getting posts: " + resultPosts.toString());

                    //aPosts.clear();
                    posts.clear();
                    posts.addAll(resultPosts);
                    earliestTimeStamp = null;
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
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
