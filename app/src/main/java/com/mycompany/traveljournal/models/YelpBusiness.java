package com.mycompany.traveljournal.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ekucukog on 7/1/2015.
 */
public class YelpBusiness {

    private String id;
    private String name;
    private String mobile_url;
    private String snippet_text;
    private String image_url; //this is very small!!!
    private double rating;
    private String rating_img_url_small;

    private final static String TAG = "YelpBusiness";

    public static YelpBusiness fromJson(JSONObject jsonObject){

        YelpBusiness yb = new YelpBusiness();

        try{

            if(jsonObject.optString("id")!=null){
                yb.id = jsonObject.getString("id");
            }
            if(jsonObject.optString("name")!=null){
                yb.name = jsonObject.getString("name");
            }
            if(jsonObject.optString("mobile_url")!=null){
                yb.mobile_url = jsonObject.getString("mobile_url");
            }
            if(jsonObject.optString("snippet_text")!=null){
                yb.snippet_text = jsonObject.getString("snippet_text");
            }
            if(jsonObject.optString("image_url")!=null){
                yb.image_url = jsonObject.getString("image_url");
            }
            if(jsonObject.optString("rating")!=null){
                yb.rating = jsonObject.getDouble("rating");
            }
            if(jsonObject.optString("rating_img_url_small")!=null){
                yb.rating_img_url_small = jsonObject.getString("rating_img_url_small");
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "yb: " + yb.toString());
        return yb;
    }

    public static ArrayList<YelpBusiness> fromJsonArray(JSONArray jsonArray) {

        ArrayList<YelpBusiness> businesses = new ArrayList<>();

        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject businessJson = jsonArray.getJSONObject(i);
                YelpBusiness yb = YelpBusiness.fromJson(businessJson);
                if(yb!=null){
                    businesses.add(yb);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return businesses;
    }

    @Override
    public String toString() {
        return "YelpBusiness{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mobile_url='" + mobile_url + '\'' +
                ", snippet_text='" + snippet_text + '\'' +
                ", image_url='" + image_url + '\'' +
                ", rating=" + rating +
                ", rating_img_url_small='" + rating_img_url_small + '\'' +
                '}';
    }

    public String toStringShort() {
        return "YelpBusiness{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getRating_img_url_small() {
        return rating_img_url_small;
    }

    public void setRating_img_url_small(String rating_img_url_small) {
        this.rating_img_url_small = rating_img_url_small;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public String getSnippet_text() {
        return snippet_text;
    }

    public void setSnippet_text(String snippet_text) {
        this.snippet_text = snippet_text;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}