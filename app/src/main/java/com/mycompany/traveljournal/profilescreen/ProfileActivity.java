package com.mycompany.traveljournal.profilescreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.datasource.ParseClient;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mapscreen.ProfileMapActivity;
import com.mycompany.traveljournal.models.User;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    UserPostsFragment userPostsFragment;
    User user;
    private Toolbar toolbarForProfile;
    private ImageView ivProfileImg;
    private ImageView ivCover;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = (User) getIntent().getSerializableExtra("User");
        ivProfileImg = (ImageView) findViewById(R.id.ivProfileImage);
        ivCover = (ImageView) findViewById(R.id.ivBannerImage);
        toolbarForProfile = (Toolbar) findViewById(R.id.toolbar1);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setData();
        setToolbar();

        if(savedInstanceState == null)
            setUpFragment();

        if(!ParseUser.getCurrentUser().getObjectId().equals(user.getId())) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!user.getIsFollowed()) {// action is to like
                        user.setIsFollowed(true);
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.follow));
                        //animateHearts(viewHolder);
                    } else {//action is to unlike
                        user.setIsFollowed(false);
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.unfollow));
                    }
                }
            });
        }
        else{
            //fab.setRippleColor(getResources().getColor(R.color.primary_color));
        }
    }

    public void setData(){
/*
        if(!"".equals(user.getProfileImgUrl()))
            Picasso.with(this).load(user.getProfileImgUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholderthumbnail)
                    .transform(Util.getTransformation())
                    .into(ivProfileImg);
*/

        ivCover.setImageResource(android.R.color.transparent);
        if(!"".equals(user.getCoverImageUrl()))
            Picasso.with(this).load(user.getCoverImageUrl()).into(ivCover);
        //else
            //Picasso.with(this).load(R.drawable.coffee).into(ivCover);

    }

    private void setToolbar() {
        if (toolbarForProfile != null) {
            setSupportActionBar(toolbarForProfile);

            // Set the home icon on toolbar
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            //actionbar.setHomeAsUpIndicator(R.drawable.ic_up_menu);

            CollapsingToolbarLayout collapsingToolbar =
                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(user.getName());
            collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.primary_color));
        }
    }

    public void setUpFragment() {

        userPostsFragment =  UserPostsFragment.newInstance(user.getId());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, userPostsFragment);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;
            case R.id.action_mapview:
                Intent i = new Intent(this, ProfileMapActivity.class);
                i.putExtra("user_id", user.getId());
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
