package com.mycompany.traveljournal.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.detailsscreen.PhotoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sjayaram on 6/14/2015.
 */
public class ImageAdapter extends PagerAdapter {
    Context context;
    private int[] GalImages = new int[] {
            R.drawable.speech_bubble,
            R.drawable.coffee,
            R.drawable.placeholder
    };

    private ArrayList<String> images = new ArrayList<>();
    private Bitmap cameraImage;

    public ImageAdapter(Context context, ArrayList<String> img, Bitmap cameraImage){
        this.context=context;
        this.images = img;
        this.cameraImage = cameraImage;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);
        if(position == 0)
            imageView.setTransitionName("postImg");
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setImageResource(GalImages[position]);

        if(cameraImage == null) {
            Picasso.with(context).load(images.get(position)).fit().centerCrop().placeholder(R.drawable.placeholderwide).into(imageView);
            container.addView(imageView, 0);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PhotoActivity.class);
                    i.putExtra("image_url", images.get(position));
                    context.startActivity(i);
                }
            });
        }
        else{
            imageView.setImageBitmap(cameraImage);
            container.addView(imageView, 0);
        }

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
