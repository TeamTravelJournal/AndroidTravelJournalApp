package com.mycompany.traveljournal.datasource;

import com.mycompany.traveljournal.common.MyCustomReceiver;
import com.mycompany.traveljournal.helpers.Util;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class PostCreatorHelper {

    private final static String TAG = "PostCreator";

    /**
     * This uploads the image and updates the post with the image url
     */
    public void uploadAndAddImageToPost(String postID, byte[] imageInBytes) {
        ImageUploader uploader = new ImageUploader(postID, imageInBytes);
        uploader.upload();
        sendPush(postID);
    }

    private void sendPush(String postId){
        JSONObject obj;
        try {
            obj = new JSONObject();
            obj.put("alert", "New Trip posted by " + Util.getUserFromParseUser(ParseUser.getCurrentUser()).getName());
            obj.put("action", MyCustomReceiver.intentAction);
            obj.put("customdata", postId);
            obj.put("userId", ParseUser.getCurrentUser().getObjectId());

            ParsePush push = new ParsePush();
            ParseQuery query = ParseInstallation.getQuery();

            // Push the notification to Android users
            query.whereEqualTo("deviceType", "android");
            query.whereNotEqualTo("user", ParseUser.getCurrentUser());
            push.setQuery(query);
            //push.setChannel("Travel");
            push.setData(obj);
            push.sendInBackground();
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

}
