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

/**
 * Created by ekucukog on 6/5/2015.
 */

class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater mInflater;
    Context context;

    public CustomWindowAdapter(Context context, LayoutInflater i){
        mInflater = i;
        this.context = context;
    }

    // This defines the contents within the info window based on the marker
    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file
        View v = mInflater.inflate(R.layout.custom_info_window, null);
        // Populate fields
        TextView title = (TextView) v.findViewById(R.id.tv_info_window_title);
        title.setText(marker.getTitle());

        //TextView description = (TextView) v.findViewById(R.id.tv_info_window_description);
        //description.setText(marker.getSnippet());

        Log.d("DEBUG", "marker title " + marker.getTitle() + "  and img url " + marker.getSnippet());

        ImageView ivThumbnail = (ImageView) v.findViewById(R.id.ivThumbnail);
        ivThumbnail.setImageResource(android.R.color.transparent);
        Picasso.with(context)
                //.load("http://files.parsetfss.com/0ac0f4de-204e-49bb-8e25-e6937c3c11ae/tfss-f7d9ac05-09eb-4d01-adcc-3f63c3ee991f-coffee.jpg")
                .load(marker.getSnippet())
                .centerCrop()
                .resize(100, 100)
                .placeholder(R.drawable.placeholderthumbnail)
                .into(ivThumbnail);

        // Return info window contents
        return v;
    }

    // This changes the frame of the info window; returning null uses the default frame.
    // This is just the border and arrow surrounding the contents specified above
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}