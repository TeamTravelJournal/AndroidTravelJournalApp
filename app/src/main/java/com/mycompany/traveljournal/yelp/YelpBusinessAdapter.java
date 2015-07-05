package com.mycompany.traveljournal.yelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.models.YelpBusiness;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ekucukog on 7/1/2015.
 */
public class YelpBusinessAdapter extends ArrayAdapter<YelpBusiness> {

    private static class ViewHolder {
        ImageView ivBusinessImage;
        TextView tvBusinessName;
        TextView tvSnippet;
        TextView tvRating;
        ImageView ivRating;
    }

    public YelpBusinessAdapter(Context context, ArrayList<YelpBusiness> ybs) {
        super(context, R.layout.item_yelp_business, ybs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        YelpBusiness yelpBusiness = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_yelp_business, parent, false);

            viewHolder.ivBusinessImage = (ImageView) convertView.findViewById(R.id.ivBusinessImage);
            viewHolder.tvBusinessName = (TextView) convertView.findViewById(R.id.tvBusinessName);
            viewHolder.tvSnippet = (TextView) convertView.findViewById(R.id.tvSnippet);
            viewHolder.tvRating = (TextView) convertView.findViewById(R.id.tvRating);
            viewHolder.ivRating = (ImageView) convertView.findViewById(R.id.ivRating);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvBusinessName.setText(yelpBusiness.getName());
        viewHolder.tvSnippet.setText(yelpBusiness.getSnippet_text());
        viewHolder.tvRating.setText(yelpBusiness.getRating() + "");

        viewHolder.ivBusinessImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(yelpBusiness.getImage_url())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholderthumbnail)
                .transform(Util.getTransformation(30))//give radius as half of the size of the image
                .into(viewHolder.ivBusinessImage);

        viewHolder.ivRating.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(yelpBusiness.getRating_img_url())
                .fit()
                .placeholder(R.drawable.ratings_placeholder_medium)
                .into(viewHolder.ivRating);

        return convertView;
    }
}
