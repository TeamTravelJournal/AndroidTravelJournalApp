package com.mycompany.traveljournal.mapscreen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.mycompany.traveljournal.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by ekucukog on 6/5/2015.
 */

class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater mInflater;
    Context context;
    HashMap<Marker, Boolean> infoWindowClicked;

    public CustomWindowAdapter(/*HashMap infoWindowClicked, */Context context, LayoutInflater i){
        mInflater = i;
        this.context = context;
        //this.infoWindowClicked = infoWindowClicked;
        infoWindowClicked = new HashMap<>();
    }

    // This defines the contents within the info window based on the marker
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    // This changes the frame of the info window; returning null uses the default frame.
    // This is just the border and arrow surrounding the contents specified above
    @Override
    public View getInfoWindow(Marker marker) {
        // Getting view from the layout file
        View v = mInflater.inflate(R.layout.custom_info_window, null);
        // Populate fields
        TextView title = (TextView) v.findViewById(R.id.tv_info_window_title);
        title.setText(marker.getTitle());

        Log.d("DEBUG", "marker title " + marker.getTitle() + "  and img url " + marker.getSnippet());

        ImageView ivThumbnail = (ImageView) v.findViewById(R.id.ivThumbnail);
        ivThumbnail.setImageResource(android.R.color.transparent);

        if (infoWindowClicked.get(marker)!=null) {
            Picasso.with(context)
                    .load(marker.getSnippet())
                    .centerCrop()
                    .resize(100, 100)
                    .placeholder(R.drawable.placeholderthumbnail)
                    .into(ivThumbnail);
        } else { // if it's the first time, load the image with the callback set
            infoWindowClicked.put(marker, new Boolean(true));
            //not_first_time_showing_info_window=true;
            Picasso.with(context)
                    .load(marker.getSnippet())
                    .centerCrop()
                    .resize(100, 100)
                    .placeholder(R.drawable.placeholderthumbnail)
                    .into(ivThumbnail, new InfoWindowRefresher(marker));
        }

        // Return info window contents
        return v;
    }
}