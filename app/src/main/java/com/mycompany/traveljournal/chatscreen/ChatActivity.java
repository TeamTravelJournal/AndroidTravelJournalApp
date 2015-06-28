package com.mycompany.traveljournal.chatscreen;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mapscreen.ProfileMapActivity;
import com.mycompany.traveljournal.models.Message;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjayaram on 6/27/2015.
 */
public class ChatActivity extends AppCompatActivity {
    private static final String TAG = ChatActivity.class.getName();
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private static String sUserId;
    public static final String USER_ID_KEY = "userId";
    private EditText etMessage;
    private Button btSend;
    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    protected JournalService client;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        startWithCurrentUser();
        client = JournalApplication.getClient();
        loadMessages();
    }

    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        sUserId = ParseUser.getCurrentUser().getObjectId();
        setupMessagePosting();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadMessages();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    // Setup message field and posting
    private void setupMessagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList();
        mAdapter = new ChatListAdapter(ChatActivity.this, sUserId, mMessages);
        lvChat.setAdapter(mAdapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // Set the home icon on toolbar
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        btSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String body = etMessage.getText().toString();

                Message msg = new Message();
                msg.setBody(body);
                msg.setUserId(sUserId);
                msg.setProfileImg(Util.getUserFromParseUser(ParseUser.getCurrentUser()).getProfileImgUrl());
                mMessages.add(msg);
                mAdapter.notifyDataSetChanged();

                // Use Message model to create new messages now
                client.createMessage(body, sUserId, new JournalCallBack<Object>(){

                    @Override
                    public void onSuccess(Object o) {
                        client.sendPushMessage(body, sUserId, Util.getUserFromParseUser(ParseUser.getCurrentUser()).getProfileImgUrl());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("message", "Error saving message: " + e.getMessage());
                    }
                });

                etMessage.setText("");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter("com.mycompany.traveljournal.chatscreen.ChatActivity");
        LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver, filter);
        // or `registerReceiver(testReceiver, filter)` for a normal broadcast
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(this).unregisterReceiver(testReceiver);
        // or `unregisterReceiver(testReceiver)` for a normal broadcast
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Message msg = new Message();
            msg.setBody(intent.getStringExtra("message"));
            msg.setUserId(intent.getStringExtra("userId"));
            msg.setProfileImg(intent.getStringExtra("profileImg"));
            mMessages.add(msg);
            mAdapter.notifyDataSetChanged();
        }
    };

    // Query messages from Parse so we can load them into the chat adapter
    private void loadMessages() {
        // Construct query to execute
        client.receiveMessage(MAX_CHAT_MESSAGES_TO_SHOW, new JournalCallBack<List<Message>>(){

            @Override
            public void onSuccess(List<Message> messages) {
                mMessages.clear();
                mMessages.addAll(messages);
                mAdapter.notifyDataSetChanged(); // update adapter
                lvChat.invalidate(); // redraw listview
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("message", "Error receiving message: " + e.getMessage());
            }

        });
    }

}
