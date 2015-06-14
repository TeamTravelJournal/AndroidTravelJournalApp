package com.mycompany.traveljournal.detailsscreen;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.common.TouchImageView;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends ActionBarActivity {

    private JournalService client;
    private String m_imageUrl;
    private TouchImageView tivFullImage;
    private static final String TAG = "PhotoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        m_imageUrl = getIntent().getStringExtra("image_url");
        client = JournalApplication.getClient();
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
