package com.mycompany.traveljournal.profilescreen;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.chatscreen.ChatActivity;
import com.mycompany.traveljournal.mapscreen.ProfileMapActivity;
import com.mycompany.traveljournal.models.User;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    User user;
    private Toolbar toolbarForProfile;
    private ImageView ivProfileImg;
    private ImageView ivCover;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    ProfilePagerAdapter adapterViewPager;
    TabLayout tabLayout;
    FloatingActionMenu rightLowerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = (User) getIntent().getSerializableExtra("User");
        ivProfileImg = (ImageView) findViewById(R.id.ivProfileImage);
        ivCover = (ImageView) findViewById(R.id.ivBannerImage);
        toolbarForProfile = (Toolbar) findViewById(R.id.toolbar1);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setData();
        setToolbar();
        setupViewPager(viewPager);
        buildSubmenus();

    }


    private void buildSubmenus()
    {
        if(!ParseUser.getCurrentUser().getObjectId().equals(user.getId())) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!user.getIsFollowed()) {// currently not following: action is to follow
                        user.setIsFollowed(true);
                        //change icon to unfollow
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.unfollow));

                    } else {// currently following: action is to unfollow
                        user.setIsFollowed(false);
                        //change icon to follow
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.plus));
                    }
                }
            });
        }
        else{
            //fab.setRippleColor(getResources().getColor(R.color.primary_color));
        }

        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        rLSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        rLSubBuilder.setLayoutParams(blueContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        rLSubBuilder.setLayoutParams(blueParams);

        ImageView rlIcon1 = new ImageView(this);
        ImageView rlIcon2 = new ImageView(this);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.follow));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.chat));

        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        // Set 4 default SubActionButtons
        rightLowerMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .attachTo(fab)
                .build();

        rlIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, ChatActivity.class);
                startActivity(i);
            }
        });


        // Listen menu open and close events to animate the button content view
        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fab.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fab.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        adapterViewPager = new ProfilePagerAdapter(getSupportFragmentManager(), this);
        adapterViewPager.addFragment(UserPostsFragment.newInstance(user.getId()), "Posts");
        adapterViewPager.addFragment(UserFollowersFragment.newInstance("following"), "Followers");
        adapterViewPager.addFragment(UserFollowersFragment.newInstance("followers"), "Following");
        viewPager.setAdapter(adapterViewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setData(){

        /*if(!"".equals(user.getProfileImgUrl()))
            Picasso.with(this).load(user.getProfileImgUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholderthumbnail)
                    .transform(Util.getTransformation())
                    .into(ivProfileImg);*/

        ivCover.setImageResource(android.R.color.transparent);
        if(!"".equals(user.getCoverImageUrl()))
            Picasso.with(this).load(user.getCoverImageUrl()).into(ivCover);
        else
            Picasso.with(this).load(R.drawable.coffee).into(ivCover);

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

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        rightLowerMenu.close(true);
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
