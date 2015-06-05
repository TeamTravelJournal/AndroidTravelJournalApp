package com.mycompany.traveljournal.common;

import com.mycompany.traveljournal.models.Post;

/**
 * Created by sjayaram on 6/4/2015.
 */
public class PostListenerObj {

    public interface PostListener {

        public void onProfileClick(Post data);

        public void onFavourite(Post data);

    }

}

