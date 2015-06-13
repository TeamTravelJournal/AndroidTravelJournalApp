package com.mycompany.traveljournal.commentscreen;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.models.Comment;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {
    private final static String TAG = "CommentFragment";
    private String postId;
    private Toolbar toolbar;

    private CommentsAdapter aComments;
    private ArrayList<Comment> comments;

    private ListView lvComments;
    private EditText etAddComment;
    private Button btnAddComment;
    protected JournalService client;
    private String commentText;

    public static CommentFragment newInstance(String postId) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString("post_id", postId);
        commentFragment.setArguments(args);
        return commentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        setUpViews(view);
        setToolbar();
        setUpListeners();
        setUpAdapters();
        populateComments();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getArguments().getString("post_id", "");

        Log.wtf(TAG, "postid = "+postId);

        client = JournalApplication.getClient();

        comments = new ArrayList<>();
        aComments = new CommentsAdapter(getActivity(), comments);
    }

    public void setUpViews(View v) {
        lvComments = (ListView) v.findViewById(R.id.lvComments);
        etAddComment = (EditText) v.findViewById(R.id.etAddComment);
        btnAddComment = (Button) v.findViewById(R.id.btnAddComment);
    }

    public void setUpListeners() {
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateComment(v);
            }
        });
    }

    private void setToolbar() {
        if (toolbar != null) {
            ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
            //toolbar.setBackgroundColor(Color.parseColor(user.getProfileBackgroundColor()));

            // Set the home icon on toolbar
            ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_up_menu);
        }
    }

    private void setUpAdapters() {
        lvComments.setAdapter(aComments);
    }

    private void populateComments() {
        client.getCommentsForPost(postId, 1000, new JournalCallBack<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> comments) {
                aComments.addAll(comments);
                Log.wtf(TAG, "got comments! "+comments.size());
                for (int i = 0 ; i < comments.size() ; i++) {
                    Log.wtf(TAG, comments.get(i).toString());
                }

            }

            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Failed to get comments!");
            }
        });
    }

    public void onClickCreateComment(View v) {
        commentText = etAddComment.getText().toString();

        client.getPostWithId(postId, new JournalCallBack<Post>() {
            @Override
            public void onSuccess(Post post) {
                client.createComment(post, commentText, new JournalCallBack<Comment>() {
                    @Override
                    public void onSuccess(Comment comment) {
                        Log.wtf(TAG, "Created comment "+comment.getBody());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.wtf(TAG, "Failed to comment " + e.toString());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.wtf(TAG, "Can't create comment, failed to get post with id " + postId);
            }
        });

    }

}
