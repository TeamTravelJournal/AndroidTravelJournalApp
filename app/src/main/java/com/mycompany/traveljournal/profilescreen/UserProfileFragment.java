package com.mycompany.traveljournal.profilescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mapscreen.MapActivity;
import com.mycompany.traveljournal.mapscreen.ProfileMapActivity;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class UserProfileFragment extends Fragment{
    private ImageView ivProfileImg;
    private ImageView ivCover;
    private TextView tvName;
    private TextView tvTravel;
    private User m_user;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        setUpViews(view);
        setUpListeners();
        return view;
    }

    public void setUpViews(View v){
        ivProfileImg = (ImageView) v.findViewById(R.id.ivProfileImage);
        ivCover = (ImageView) v.findViewById(R.id.ivBannerImage);
        tvName = (TextView) v.findViewById(R.id.tvName);
        tvTravel = (TextView) v.findViewById(R.id.tvTravel);

        tvTravel.setText("Travel Map");
    }

    public void setUpListeners(){


        //I will change this click listener from profile image to some text saying "see user's travel on map"
        tvTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), ProfileMapActivity.class);
                i.putExtra("user_id", m_user.getId());
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    public void setData(User user){
        m_user = user;
        tvName.setText(user.getName());
        if(!"".equals(user.getProfileImgUrl()))
            Picasso.with(getActivity()).load(user.getProfileImgUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholderthumbnail)
                    .transform(Util.getTransformation())
                    .into(ivProfileImg);
        if(!"".equals(user.getCoverImageUrl()))
            Picasso.with(getActivity()).load(user.getCoverImageUrl()).into(ivCover);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
