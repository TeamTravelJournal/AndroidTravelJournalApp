package com.mycompany.traveljournal.yelp;


import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
/**
 * Created by ekucukog on 7/1/2015.
 */

/**
 * Generic service provider for two-step OAuth10a.
 */
public class TwoStepOAuth extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token arg0) {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }
}