package com.mycompany.traveljournal.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.common.EndlessScrollListener;
import com.mycompany.traveljournal.common.MultiScrollListener;
import com.mycompany.traveljournal.common.PostListenerObj;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;
import com.mycompany.traveljournal.profilescreen.ProfileActivity;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalService;
import com.mycompany.traveljournal.wishlistscreen.WishListActivity;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by sjayaram on 6/4/2015.
 */
public abstract class PostsListFragment extends Fragment {

    protected ArrayList<Post> posts;
    protected ListView lvPosts;
    protected SwipeRefreshLayout swipeContainer;
    protected PostsListAdapter aPosts;
    protected MultiScrollListener scrolls;
    protected ImageView mQuickReturnView;
    protected String m_query;
    protected LatLng m_location;
    protected JournalService client;
    protected Toolbar toolbar;
    protected ImageView ivNewPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_list, container, false);
        setUpViews(v);
        setUpAdapter();
        setUpListeners();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts = new ArrayList<>();
        aPosts = new PostsListAdapter(getActivity(), posts);
        setUpHasOptionsMenu();
        client = JournalApplication.getClient();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_base, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                if(query != null && !"".equals(query)) {
                    m_query = query;
                    m_location = Util.getLocationFromQuery(getActivity(), m_query);
                    Toast.makeText(getActivity(), "query " + query, Toast.LENGTH_SHORT).show();
                    populateListOnSearch();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_profile){
            executeProfileIntent(client.getCurrentUser());
        }

        return super.onOptionsItemSelected(item);
    }

    //subclass can override if needed -------------

    public void setUpHasOptionsMenu(){
        setHasOptionsMenu(true);
    }

    public void setUpAdapter(){
        lvPosts.setAdapter(aPosts);
    }

    public void setUpViews(View v){
        lvPosts = (ListView)v.findViewById(R.id.lvPosts);
        swipeContainer = (SwipeRefreshLayout)v.findViewById(R.id.swipeContainer);
        mQuickReturnView = (ImageView)v.findViewById(R.id.quick_return_iv);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
        ivNewPosts = (ImageView)v.findViewById(R.id.ivNewPosts);
    }

    public void setUpListeners(){

        scrolls = new MultiScrollListener();

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refreshList();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        scrolls.addScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                //customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                populateList();
            }
        });

        lvPosts.setOnScrollListener(scrolls);

        lvPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Call detail page from here
                Intent i = new Intent(getActivity(), DetailActivity.class);
                Post post = (Post)parent.getItemAtPosition(position);
                i.putExtra("post_id", post.getPostID());

                Pair<View, String> p1 = Pair.create(view.findViewById(R.id.ivPost), "postImg");
                Pair<View, String> p2 = Pair.create(view.findViewById(R.id.ivProfile), "profileImg");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2);
                getActivity().startActivity(i, options.toBundle());
            }
        });

        aPosts.setPostObjListener(new PostListenerObj.PostListener() {
            @Override
            public void onProfileClick(Post data) {
                executeProfileIntent(data.getParseUser());
            }

            @Override
            public void onFavourite(Post data) {

            }
        });

    }

    protected void executeProfileIntent(User data){
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        i.putExtra("User", data);
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    //override in subclass
    public abstract void populateList();

    //override in subclass
    public abstract void refreshList();

    //override in subclass
    public abstract void populateListOnSearch();

}
