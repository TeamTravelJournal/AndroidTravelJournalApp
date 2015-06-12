package com.mycompany.traveljournal.profilescreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class UserProfileFragment extends Fragment{
    private ImageView ivProfileImg;
    private ImageView ivCover;
    private TextView tvName;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        setUpViews(view);
        return view;
    }

    public void setUpViews(View v){
        ivProfileImg = (ImageView) v.findViewById(R.id.ivProfileImage);
        ivCover = (ImageView) v.findViewById(R.id.ivBannerImage);
        tvName = (TextView) v.findViewById(R.id.tvName);
    }


    public void setData(User user){
        tvName.setText(user.getName());
        if(!"".equals(user.getProfileImgUrl()))
            Picasso.with(getActivity()).load(user.getProfileImgUrl()).into(ivProfileImg);
        if(!"".equals(user.getCoverImageUrl()))
            Picasso.with(getActivity()).load(user.getCoverImageUrl()).into(ivCover);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
