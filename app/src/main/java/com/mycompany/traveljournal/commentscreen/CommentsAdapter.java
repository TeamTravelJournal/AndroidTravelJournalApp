package com.mycompany.traveljournal.commentscreen;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsAdapter extends ArrayAdapter<Comment>{

    private static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvName;
        TextView tvBody;
    }

    public CommentsAdapter(Context context, ArrayList<Comment> comments) {
        super(context, R.layout.fragment_comment_detail, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fragment_comment_detail, parent, false);

            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(comment.getUser().getName());
        viewHolder.tvBody.setText(comment.getBody());

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(comment.getUser().getProfileImgUrl()).into(viewHolder.ivProfileImage);

        return convertView;
    }
}
