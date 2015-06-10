package com.mycompany.traveljournal.helpers;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalService;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sjayaram on 6/9/2015.
 */
public class FacebookUtil {

    public static final String APP_TAG = "JournalApp";

    public static void makeFBProfileRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        if (jsonObject != null) {

                            try {
                                String fbId = jsonObject.getString("id").toString();
                                String profile_img_url = "https://graph.facebook.com/" + fbId +"/picture?type=small";
                                updateParseUser(jsonObject.getString("name"), fbId, profile_img_url);
                            } catch (JSONException e) {
                                Log.d(APP_TAG,
                                        "Error parsing returned user data. " + e);
                            }
                        } else if (graphResponse.getError() != null) {
                            switch (graphResponse.getError().getCategory()) {
                                case LOGIN_RECOVERABLE:
                                    Log.d(APP_TAG,
                                            "Authentication error: " + graphResponse.getError());
                                    break;

                                case TRANSIENT:
                                    Log.d(APP_TAG,
                                            "Transient error. Try again. " + graphResponse.getError());
                                    break;

                                case OTHER:
                                    Log.d(APP_TAG,
                                            "Some other error: " + graphResponse.getError());
                                    break;
                            }
                        }
                    }
                });

        request.executeAsync();

    }


    private static void updateParseUser(final String name, final String fbId, final String profileImgUrl){

        AsyncHttpClient client = new AsyncHttpClient();
        String token = AccessToken.getCurrentAccessToken().getToken();
        client.get("https://graph.facebook.com/" + fbId + "?fields=cover&access_token=" + token , new JsonHttpResponseHandler(){

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                String coverUrl = "";
                if(response.optString("cover") != null) {
                    try
                    {
                        coverUrl = response.getJSONObject("cover").getString("source");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JournalService client = JournalApplication.getClient();
                client.createUser(name, profileImgUrl, coverUrl);

            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(APP_TAG, "error getting cover iamge : " + responseString);
            }
        });

    }

}
