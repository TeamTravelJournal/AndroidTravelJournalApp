package com.mycompany.traveljournal.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.common.PostListenerObj;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.models.Post;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by sjayaram on 6/18/2015.
 */
public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.SimpleItemViewHolder> {
    private List<Post> items;
    private PostListenerObj.PostListener listener;
    private Activity mContext;
    private final static String TAG = "PostsRecyclerAdapter";

    // Assign the listener implementing events interface that will receive the events
    public void setPostObjListener(PostListenerObj.PostListener listener) {
        this.listener = listener;
    }

    // Provide a reference to the views for each data item
    // Provide access to all the views for a data item in a view holder
    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        final View rootView;
        ImageView ivProfile;
        ImageView ivStar;
        ImageView ivPost;
        TextView tvCaption;
        ImageView ivHeartInside;
        ImageView ivHeartOutside;
        RelativeLayout rlPost;
        RelativeTimeTextView tvTime;

        public SimpleItemViewHolder(View itemView, final Activity context) {
            super(itemView);
            rootView = itemView;
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            ivPost = (ImageView) itemView.findViewById(R.id.ivPost);
            ivStar = (ImageView) itemView.findViewById(R.id.ivStar);
            ivHeartInside =(ImageView) itemView.findViewById(R.id.ivHeartInside);
            ivHeartOutside =(ImageView) itemView.findViewById(R.id.ivHeartOutside);
            rlPost =(RelativeLayout) itemView.findViewById(R.id.rlPost);
            tvTime = (RelativeTimeTextView)itemView.findViewById(R.id.timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Call detail page from here
                    Intent i = new Intent(context, DetailActivity.class);
                    Post post = (Post)v.getTag();
                    i.putExtra("post_id", post.getPostID());

                    Pair<View, String> p1 = Pair.create(v.findViewById(R.id.ivPost), "postImg");
                    Pair<View, String> p2 = Pair.create(v.findViewById(R.id.ivProfile), "profileImg");

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, p1, p2);
                    context.startActivity(i, options.toBundle());
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostsRecyclerAdapter(Activity context, List<Post> items) {
        this.items = items;
        mContext = context;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    // Create new items (invoked by the layout manager)
    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_post, viewGroup, false);
        return new SimpleItemViewHolder(itemView, mContext);
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(SimpleItemViewHolder viewHolder, int position) {
        Post post = items.get(position);
        viewHolder.rootView.setTag(post);
        viewHolder.ivPost.setImageResource(android.R.color.transparent);
        viewHolder.tvCaption.setText(post.getCaption());

        Picasso.with(viewHolder.tvCaption.getContext())
                .load(post.getImageUrl())
                .fit()
                .centerCrop()
                        //.centerInside()
                .placeholder(R.drawable.placeholderwide)
                .into(viewHolder.ivPost);

        viewHolder.ivProfile.setImageResource(android.R.color.transparent);

        Picasso.with(viewHolder.tvCaption.getContext())
                .load(R.drawable.icon_user_32)
                .transform(Util.getTransformation(40))
                .into(viewHolder.ivProfile);

        if(post.getParseUser()!=null)
        {
            Picasso.with(viewHolder.tvCaption.getContext())
                    .load(post.getParseUser().getProfileImgUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholderthumbnail)
                    .transform(Util.getTransformation(40))
                    .into(viewHolder.ivProfile);
        }

        viewHolder.ivStar.setImageResource(android.R.color.transparent);

        if(post.isLiked()){
            Picasso.with(viewHolder.tvCaption.getContext())
                    .load(R.drawable.icon_heart_accent)
                    .into(viewHolder.ivStar);
            viewHolder.ivStar.setAlpha(1.0f);
        }else{
            Picasso.with(viewHolder.tvCaption.getContext())
                    .load(R.drawable.icon_heart_white)
                    .into(viewHolder.ivStar);
            viewHolder.ivStar.setAlpha(0.4f);
        }

        if(post.doesNeedAnimation()){//just favorited, do heart animation
            Log.d(TAG, "do heart animation for " + post.getCaption());
            post.setNeedsAnimation(false);
            animateHearts(viewHolder);

        }else{
            Log.d(TAG, "no need for heart animation for " + post.getCaption());
        }

        viewHolder.tvTime.setReferenceTime(post.getCreatedAt().getTime());

        setUpListeners(viewHolder, post);

    }

    private void setUpListeners(final SimpleItemViewHolder viewHolderCurrent, final Post post) {

        viewHolderCurrent.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProfileClick(post);
            }
        });

        viewHolderCurrent.ivStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "clicked to " + post.getCaption());
                if(post.isLiked()==false){// action is to like

                    post.setLiked(true);
                    post.setNeedsAnimation(true);

                    Log.d(TAG, "view holder of post: " + viewHolderCurrent.tvCaption.getText());
                    //viewHolderCurrent.ivHeartOutside.setAlpha(1.0f);

                }else{//action is to unlike
                    post.setLiked(false);
                }
                listener.onFavourite(post);
            }
        });

        viewHolderCurrent.ivHeartOutside.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d(TAG, "clicked to " + post.getCaption());
                post.setLiked(true);
                post.setNeedsAnimation(true);

                Log.d(TAG, "view holder of post: " + viewHolderCurrent.tvCaption.getText());
                listener.onFavourite(post);

                return true;
            }
        });
    }

    private void animateHearts(SimpleItemViewHolder viewHolderCurrent){

        Log.d(TAG, "view holder of post: " + viewHolderCurrent.tvCaption.getText());

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(viewHolderCurrent.ivHeartInside, "alpha", 0.4f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(viewHolderCurrent.ivHeartOutside, "alpha", 0.2f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(viewHolderCurrent.ivHeartInside, "scaleX", 0.2f, 1.0f)
                        .setDuration(1000),
                ObjectAnimator.ofFloat(viewHolderCurrent.ivHeartInside, "scaleY", 0.2f, 1.0f)
                        .setDuration(1000)
        );
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(viewHolderCurrent.ivHeartInside, "alpha", 0.0f)
                        .setDuration(0),
                ObjectAnimator.ofFloat(viewHolderCurrent.ivHeartOutside, "alpha", 0.0f)
                        .setDuration(0));
        AnimatorSet set3 = new AnimatorSet();
        set3.playSequentially(set, animatorSet);
        set3.start();
    }
}
