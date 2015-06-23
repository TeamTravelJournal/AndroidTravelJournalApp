package com.mycompany.traveljournal.profilescreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsRecyclerAdapter;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.models.User;

import java.util.ArrayList;

/**
 * Created by sjayaram on 6/22/2015.
 */
public class UserFollowersFragment extends Fragment {
    // Store instance variables
    private String flag;
    protected ArrayList<User> users;
    protected ListView lvUsers;
    protected UsersAdapter aUsers;

    // newInstance constructor for creating fragment with arguments
    public static UserFollowersFragment newInstance(String flag) {
        UserFollowersFragment fragmentFirst = new UserFollowersFragment();
        Bundle args = new Bundle();
        args.putString("flag", flag);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = getArguments().getString("flag");

        users = new ArrayList<>();

        if("following".equals(flag)){
            User user = new User();
            user.setName("Ashwin Kunder");
            user.setProfileImgUrl("https://graph.facebook.com/10153412985307533/picture?type=large");
            users.add(user);
            user = new User();
            user.setName("Sunil Salunkhe");
            user.setProfileImgUrl("https://graph.facebook.com/10153129680603025/picture?type=large");
            users.add(user);
            user = new User();
            user.setName("Jack Pilar");
            user.setProfileImgUrl("https://graph.facebook.com/1403062423356658/picture?type=large");
            users.add(user);
        }
        else{
            User user = new User();
            user.setName("Radha Krishna");
            user.setProfileImgUrl("https://graph.facebook.com/107886419549040/picture?type=large");
            users.add(user);
            user = new User();
            user.setName("Srivats Jayaram");
            user.setProfileImgUrl("https://graph.facebook.com/10152968852941866/picture?type=large");
            users.add(user);
            user = new User();
            user.setName("Emma Tan");
            user.setProfileImgUrl("https://graph.facebook.com/1423238651333625/picture?type=large");
            users.add(user);

        }

        aUsers = new UsersAdapter(getActivity(), users);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

        setUpViews(view);
        setUpAdapter();

        return view;
    }

    public void setUpViews(View v) {
        lvUsers = (ListView) v.findViewById(R.id.lvUsers);
    }

    public void setUpAdapter(){
        lvUsers.setAdapter(aUsers);
    }
}