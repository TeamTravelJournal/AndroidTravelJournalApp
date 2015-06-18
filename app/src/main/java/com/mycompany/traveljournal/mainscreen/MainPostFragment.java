package com.mycompany.traveljournal.mainscreen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListFragment;
import com.mycompany.traveljournal.common.MyAlarmReceiver;
import com.mycompany.traveljournal.createscreen.CreatePostActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mapscreen.MapActivity;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.NewPostsService;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by sjayaram on 6/4/2015.
 * This code is managed by ekucukog
 */
public class MainPostFragment extends PostsListFragment {

    private final static String TAG = "MainPostFragmentDebug";
    private Date earliestTimeStamp = null;
    private Date latestDate;
    private ResponseReceiver receiver;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        getActivity().registerReceiver(receiver, filter);
        SharedPreferences mSettings = getActivity().getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setUpListeners();
        populateList();
        scheduleAlarm();
        return view;
    }



    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getActivity(), MyAlarmReceiver.class);
        //intent.putExtra(NewPostsService.PARAM_IN_MSG, latestDate != null ? latestDate : "");
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(getActivity(), MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Setup periodic alarm every 100 seconds
        long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
        int intervalMillis = 100000; // 100 seconds
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
        alarm.setInexactRepeating(AlarmManager.RTC, firstMillis, intervalMillis, pIntent);
    }


    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "com.mamlambo.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(NewPostsService.PARAM_OUT_MSG);

            //query from local dataStore
            List<Post> resultPosts =  client.getLatestPostsFromLocal(latestDate);
            if (resultPosts.size() > 0) {
                posts.addAll(0, resultPosts);
                latestDate = posts.get(0).getCreatedAt();
                aPosts.notifyDataSetChanged();
                editor.putString(NewPostsService.PARAM_IN_MSG, latestDate.toString());
                editor.commit();
                Toast.makeText(getActivity(), text + " new Posts.", Toast.LENGTH_LONG).show();
                ivNewPosts.setVisibility(View.VISIBLE);
            }
        }
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

        ivNewPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvPosts.smoothScrollToPosition(0);
                ivNewPosts.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int footerHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.footer_height);
        int headerHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.footer_height);

        QuickReturnListViewOnScrollListener scrollListener;

        scrollListener = new QuickReturnListViewOnScrollListener.Builder(QuickReturnViewType.FOOTER)
                .footer(mQuickReturnView)
                .minFooterTranslation(footerHeight)
                .build();

        scrolls.addScrollListener(scrollListener);

        QuickReturnListViewOnScrollListener scrollListener1 = new QuickReturnListViewOnScrollListener.Builder(QuickReturnViewType.HEADER)
                .header(toolbar)
                .minHeaderTranslation(-headerHeight)
                .build();

        scrolls.addScrollListener(scrollListener1);

        lvPosts.setOnScrollListener(scrolls);
    }


    @Override
     public void populateList() {

        /*if(m_query!=null){
            m_location = Util.getLocationFromQuery(getActivity(), m_query);
        }*/

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
                        unpinObjects();
                        if(resultPosts.size()>0)
                        {
                            latestDate = resultPosts.get(0).getCreatedAt();
                            editor.putString(NewPostsService.PARAM_IN_MSG, latestDate.toString());
                        }
                        else{
                            editor.putString(NewPostsService.PARAM_IN_MSG, null);
                        }
                        editor.commit();
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

    private void unpinObjects(){
        //unpin local store
        try {
            ParseObject.unpinAll("POSTS_GROUP_NAME");
        } catch (ParseException e) {
            e.printStackTrace();
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

                    unpinObjects();
                    aPosts.clear();
                    posts.addAll(resultPosts);

                    //TODO review code
                    if (posts.size() > 0)
                    {
                        earliestTimeStamp = posts.get(posts.size() - 1).getCreatedAt();
                        latestDate = posts.get(0).getCreatedAt();
                        editor.putString(NewPostsService.PARAM_IN_MSG, latestDate.toString());
                    }
                    else{
                        editor.putString(NewPostsService.PARAM_IN_MSG, null);
                    }

                    editor.commit();
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

                    aPosts.clear();
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

                    aPosts.clear();
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

                    aPosts.clear();
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
    }
}
