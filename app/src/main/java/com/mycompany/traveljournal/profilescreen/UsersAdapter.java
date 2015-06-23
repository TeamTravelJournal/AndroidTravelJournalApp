package com.mycompany.traveljournal.profilescreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sjayaram on 6/22/2015.
 */
public class UsersAdapter extends ArrayAdapter<User> {

private static class ViewHolder {
    ImageView ivProfileImage;
    TextView tvName;
}

    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.fragment_comment_detail, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fragment_comment_detail, parent, false);

            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(user.getName());

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(user.getProfileImgUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholderthumbnail)
                .transform(Util.getTransformation(30))//give radius as half of the size of the image
                .into(viewHolder.ivProfileImage);

        return convertView;
    }
}