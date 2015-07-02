package com.mycompany.traveljournal.wikitude;

import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.StartupConfiguration;

/**
 * Created by sjayaram on 7/1/2015.
 */
public class ArchitectCamActivity extends AbstractArchitectCamActivity {


    public static final String ACTIVITY_TITLE_STRING = "activityTitle";
    public static final String ACTIVITY_ARCHITECT_WORLD_URL = "ArchitectWorld/index.html";

    /**
     * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
     */
    private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();

    @Override
    public String getARchitectWorldPath() {
        return ACTIVITY_ARCHITECT_WORLD_URL;
    }

    @Override
    public String getActivityTitle() {
        return ACTIVITY_TITLE_STRING;
    }

    @Override
    public int getContentViewId() {
        return R.layout.architect_cam;
    }

    @Override
    public int getArchitectViewId() {
        return R.id.architectView;
    }

    @Override
    public String getWikitudeSDKLicenseKey() {
        return WikitudeSDKConstants.WIKITUDE_SDK_KEY;
    }

    @Override
    public ArchitectView.SensorAccuracyChangeListener getSensorAccuracyListener() {
        return new ArchitectView.SensorAccuracyChangeListener() {
            @Override
            public void onCompassAccuracyChanged( int accuracy ) {
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
                if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && ArchitectCamActivity.this != null && !ArchitectCamActivity.this.isFinishing() && System.currentTimeMillis() - ArchitectCamActivity.this.lastCalibrationToastShownTimeMillis > 5 * 1000) {
                    Toast.makeText(ArchitectCamActivity.this, R.string.compass_accuracy_low, Toast.LENGTH_LONG).show();
                    ArchitectCamActivity.this.lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
                }
            }
        };
    }

    @Override
    public ArchitectView.ArchitectUrlListener getUrlListener() {
        return new ArchitectView.ArchitectUrlListener() {

            @Override
            public boolean urlWasInvoked(String uriString) {
                Uri invokedUri = Uri.parse(uriString);
                Log.i("urlWasInvoked", uriString);
//
//				// pressed "More" button on POI-detail panel
				if ("markerselected".equalsIgnoreCase(invokedUri.getHost())) {
					final Intent poiDetailIntent = new Intent(ArchitectCamActivity.this, DetailActivity.class);
					poiDetailIntent.putExtra("post_id", String.valueOf(invokedUri.getQueryParameter("id")) );
                    ArchitectCamActivity.this.startActivity(poiDetailIntent);
					return true;
				}

                return true;
            }
        };
    }

    @Override
    public ILocationProvider getLocationProvider(final LocationListener locationListener) {
        return new LocationProvider(this, locationListener);
    }

    @Override
    public float getInitialCullingDistanceMeters() {
        // you need to adjust this in case your POIs are more than 50km away from user here while loading or in JS code (compare 'AR.context.scene.cullingDistance')
        return ArchitectViewHolderInterface.CULLING_DISTANCE_DEFAULT_METERS;
    }

    @Override
    protected boolean hasGeo() {
        return true;
    }

    @Override
    protected boolean hasIR() {
        return true;
    }

    @Override
    protected StartupConfiguration.CameraPosition getCameraPosition() {
        return StartupConfiguration.CameraPosition.DEFAULT;
    }

    @Override
    protected void onPostCreate( final Bundle savedInstanceState ) {
        super.onPostCreate( savedInstanceState );
        this.injectData();
    }

}

