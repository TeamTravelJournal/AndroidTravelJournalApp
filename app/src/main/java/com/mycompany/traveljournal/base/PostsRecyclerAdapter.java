package com.mycompany.traveljournal.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.common.PostListenerObj;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.models.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sjayaram on 6/18/2015.
 */
public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostsRecyclerAdapter.SimpleItemViewHolder> {
    private List<Post> items;
    private PostListenerObj.PostListener listener;
    private Activity mContext;

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

        public SimpleItemViewHolder(View itemView, final Activity context) {
            super(itemView);
            rootView = itemView;
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            ivPost = (ImageView) itemView.findViewById(R.id.ivPost);
            ivStar = (ImageView) itemView.findViewById(R.id.ivStar);

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
                .transform(Util.getTransformation())
                .into(viewHolder.ivProfile);

        if(post.getParseUser()!=null)
        {
            Picasso.with(viewHolder.tvCaption.getContext())
                    .load(post.getParseUser().getProfileImgUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholderthumbnail)
                    .transform(Util.getTransformation())
                    .into(viewHolder.ivProfile);
        }

        viewHolder.ivStar.setImageResource(android.R.color.transparent);
        Picasso.with(viewHolder.tvCaption.getContext())
                .load(R.drawable.icon_heart_white_mtrls)
                .into(viewHolder.ivStar);


        setUpListeners(viewHolder, post);

    }

    private void setUpListeners(SimpleItemViewHolder viewHolder, final Post post) {

        viewHolder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProfileClick(post);
            }
        });

        viewHolder.ivStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFavourite(post);
            }
        });

    }
}
