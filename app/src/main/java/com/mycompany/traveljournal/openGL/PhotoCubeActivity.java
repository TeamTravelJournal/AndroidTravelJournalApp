package com.mycompany.traveljournal.openGL;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.mycompany.traveljournal.mainscreen.MainActivity;
import com.mycompany.traveljournal.models.User;

/**
 * Created by sjayaram on 7/9/2015.
 */
public class PhotoCubeActivity extends Activity {

    private GLSurfaceView glView;   // Use GLSurfaceView

    // Call back when the activity is started, to initialize the view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String postId = (String) getIntent().getSerializableExtra("postId");
        glView = new MyGLSurfaceView(this, postId);
        setContentView(glView);  // Set View (NEW)

        LinearLayout ll = new LinearLayout(this);
        Button b = new Button(this);
        b.setText("Blend");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glView.callOnClick();
            }
        });

        Button b1 = new Button(this);
        b1.setText("Light");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glView.hasOnClickListeners();
            }
        });

        ll.addView(b);
        ll.addView(b1);
        ll.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM );
        this.addContentView(ll,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            super.onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean hasGLES20() {
        ActivityManager am = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x20000;
    }

    // Call back when the activity is going into the background
    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
    }

    // Call back after onPause()
    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }

}
