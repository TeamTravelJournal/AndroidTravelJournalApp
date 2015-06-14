package com.mycompany.traveljournal.detailsscreen;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.common.TouchImageView;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends ActionBarActivity {

    private String m_imageUrl;
    private TouchImageView tivFullImage;
    private static final String TAG = "PhotoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        m_imageUrl = getIntent().getStringExtra("image_url");
        tivFullImage = (TouchImageView) findViewById(R.id.tivFullImage);

        loadImage();
    }

    private void loadImage(){
        if(m_imageUrl!=null){
            Log.d(TAG, "loading image");
            Picasso.with(this)
                    .load(m_imageUrl)
                    .placeholder(R.drawable.placeholder)// do not remove placeholder
                    .fit().centerInside().into(tivFullImage);
        }else{
            Log.d(TAG, "url is null");
        }
    }
}