package com.mycompany.traveljournal.base;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.common.PostListenerObj;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class PostsListAdapter extends ArrayAdapter<Post> {

    private static class ViewHolder {
        ImageView ivProfile;
        ImageView ivStar;
        ImageView ivPost;
        TextView tvCaption;
    }

    ViewHolder viewHolder;
    private PostListenerObj.PostListener listener;

    // Assign the listener implementing events interface that will receive the events
    public void setPostObjListener(PostListenerObj.PostListener listener) {
        this.listener = listener;
    }

    public PostsListAdapter(Context context, List<Post> posts) {
        super(context, android.R.layout.simple_list_item_1, posts);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Post post = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivPost = (ImageView) convertView.findViewById(R.id.ivPost);
            viewHolder.ivStar = (ImageView) convertView.findViewById(R.id.ivStar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivProfile.setImageResource(android.R.color.transparent);
        viewHolder.ivPost.setImageResource(android.R.color.transparent);

        viewHolder.tvCaption.setText(post.getCaption());

        Picasso.with(getContext())
                .load(post.getImageUrl())
                .fit()
                .centerCrop()
                //.centerInside()
                .placeholder(R.drawable.placeholderwide)
                .into(viewHolder.ivPost);


        // Default profile picture
        Picasso.with(getContext()).load(R.drawable.icon_user_32).into(viewHolder.ivProfile);

        User user = post.getCreatedByUser();
        String profileImageUrl = "";
        if(user!=null){
            Log.d("DEBUG", "user associated with this post is: " + user.toString());
            profileImageUrl = user.getProfileImageUrl();

            Picasso.with(getContext())
                    .load(profileImageUrl)
                    .centerInside()
                    .resize(50, 50)
                            //.placeholder(R.drawable.placeholderthumbnail)
                    .into(viewHolder.ivProfile);
        }

        setUpListeners(post);

        return convertView;
    }

    //subclass can override if needed
    private void setUpListeners(final Post post) {

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