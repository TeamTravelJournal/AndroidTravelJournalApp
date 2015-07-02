package com.mycompany.traveljournal.yelp;

import android.content.Context;

import com.mycompany.traveljournal.R;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.model.Token;

/**
 * Created by ekucukog on 7/1/2015.
 *
 * source:
 * https://github.com/Pretz/android-example/blob/master/src/com/pretzlav/hello/Yelp.java
 */
public class Yelp {

    OAuthService service;
    Token accessToken;

    public static Yelp getYelp(Context context) {
        return new Yelp(context.getString(R.string.yelp_consumer_key), context.getString(R.string.yelp_consumer_secret),
                context.getString(R.string.yelp_token), context.getString(R.string.yelp_token_secret));
    }

    /**
     * Setup the Yelp API OAuth credentials.
     *
     * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
     *
     * @param consumerKey Consumer key
     * @param consumerSecret Consumer secret
     * @param token Token
     * @param tokenSecret Token secret
     */
    public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    /**
     * Search with term and location.
     *
     * @param term Search term
     * @param latitude Latitude
     * @param longitude Longitude
     * @return JSON string response
     */
    public String search(String term, double latitude, double longitude) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    /**
     * Search with term string location.
     *
     * @param term Search term
     * @return JSON string response
     */
    public String search(String term, String location) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    public String searchRestaurant(String location) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", "restaurant");
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", 3 + "");
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    // CLI
    public static void main(String[] args) {
        // Update tokens here from Yelp developers site, Manage API access.
        String consumerKey = "";
        String consumerSecret = "";
        String token = "";
        String tokenSecret = "";

        Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
        String response = yelp.search("burritos", 30.361471, -87.164326);

        System.out.println(response);
    }
}