package com.mycompany.traveljournal.mapscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mycompany.traveljournal.R;
import com.mycompany.traveljournal.base.PostsListActivity;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.mainscreen.MainPostFragment;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by ekucukog on 6/5/2015.
 */
public class MapActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraChangeListener,
        LocationListener {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    private final static String TAG = "MapActivityDebug";

    private ArrayList<LatLng> points=null;
    private boolean ready = false;
    private LatLng fixAddress = new LatLng(37.533278, -122.237933);//my redwoodshores address
    private ArrayList<Marker> markers = null;
    private int markerIndex =0;
    private ArrayList<Boolean> shown= null;
    private String m_query;
    private LatLng m_location;
    private JournalService client;

    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        markers = new ArrayList<Marker>();
        m_query = getIntent().getStringExtra("query");
        m_location = Util.getLocationFromQuery(this, m_query);
        client = JournalApplication.getClient();

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                    map.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
                }
            });
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }

    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Attach long click listener to the map here
            map.setOnMapLongClickListener(this);
            map.setOnCameraChangeListener(this);

            // Map is ready
            Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map.setMyLocationEnabled(true);

            // Now that map has loaded, let's get our location!
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            //make sure map camera goes to target location
            setTargetLocation(m_location);

            connectClient();

        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTargetLocation(LatLng location) {

        if(location!=null){
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 10/*17*/);
            map.animateCamera(cameraUpdate);
            ready = true;
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        Log.d("DEBUG", "on camera change");

        //if(points==null && ready) {
            //map.clear();

            //Log.d("DEBUG", "camera change - location is set/ready");
            // Create random points within the boundaries of the map
            //points = createRandomPointsOnVisibleMap();//this method will be replaces by querying parse
            getPostsOnCurrentWindowAndPutPins();

            // Put a pin for each.
            //putPins(points);
        //}
    }

    private void getPostsOnCurrentWindowAndPutPins(){

        points = new ArrayList<>();

        LatLngBounds curScreen = map.getProjection().getVisibleRegion().latLngBounds;

        LatLng ne = curScreen.northeast;
        LatLng sw = curScreen.southwest;

        Log.d("DEBUG", "Screen boundaries. ne: " + ne.toString() + ", sw: " + sw.toString());

        // west - x coordinate
        double rangeMinLng = sw.longitude;
        // east - x coordinate
        double rangeMaxLng = ne.longitude;
        // north - y coordinate
        double rangeMaxLat = ne.latitude;
        // south - y coordinate
        double rangeMinLat = sw.latitude;

        client.getPostsWithinWindow(rangeMinLat, rangeMinLng, rangeMaxLat, rangeMaxLng, Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                //Toast.makeText(, "parse call succesful", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < posts.size(); i++) {

                    Post post = posts.get(i);

                    if (post == null) {
                        Log.d(TAG, "post is null");
                    } else {
                        Log.d(TAG, "post is: " + post.toString());

                        //LatLng point = new LatLng(37.5513928, -122.2865121);
                        LatLng point = new LatLng(post.getLatitude(), post.getLongitude());

                        Log.d("DEBUG", "Adding post point: " + point.toString());

                        points.add(point);
                    }
                }

                putPins(points);
            }
            @Override
            public void onFailure(Exception e) {
                //Toast.makeText(this, "parse call failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to get posts");
            }
        });
    }

    private ArrayList<LatLng> createRandomPointsOnVisibleMap(){

        Random r = new Random();

        ArrayList<LatLng> points = new ArrayList<>();

        LatLngBounds curScreen = map.getProjection().getVisibleRegion().latLngBounds;

        LatLng ne = curScreen.northeast;
        LatLng sw = curScreen.southwest;

        Log.d("DEBUG", "Screen boundaries. ne: " + ne.toString() + ", sw: " + sw.toString());

        // west - x coordinate
        double rangeMinLng = sw.longitude;
        // east - x coordinate
        double rangeMaxLng = ne.longitude;
        // north - y coordinate
        double rangeMaxLat = ne.latitude;
        // south - y coordinate
        double rangeMinLat = sw.latitude;

        for(int i =0; i<10; i++){

            double randomValueLng = rangeMinLng + (rangeMaxLng - rangeMinLng) * r.nextDouble();
            double randomValueLat = rangeMinLat + (rangeMaxLat - rangeMinLat) * r.nextDouble();

            //LatLng point = new LatLng(37.5513928, -122.2865121);
            LatLng point = new LatLng(randomValueLat, randomValueLng);

            Log.d("DEBUG", "Adding random point: " + point.toString());

            points.add(point);
        }
        return points;
    }

    private void putPins(ArrayList<LatLng> points){

        Log.d("DEBUG", "Entering put pins");

        for(LatLng point: points){


            // Define color of marker icon
            BitmapDescriptor defaultMarker =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

            // Creates and adds marker to the map
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(point)
                            //        .title(title)
                            //        .snippet(snippet)
                    .icon(defaultMarker));

            markers.add(marker);
            Log.d("DEBUG", "Marking pin for point: " + point.toString());

            //dropPinEffect(marker);

        }
    }

    protected void connectClient() {
        // Connect the client.
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /*
     * Called when the Activity becomes visible.
    */
    @Override
    protected void onStart() {
        super.onStart();
        connectClient();
    }

    /*
	 * Called when the Activity is no longer visible.
	 */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mGoogleApiClient.connect();
                        break;
                }

        }
    }

    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the client
     * finishes successfully. At this point, you can request the current
     * location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            Toast.makeText(this, "GPS location was found!", Toast.LENGTH_SHORT).show();
            if(m_location==null) {// if query location is null, use current location
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                /*map.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        ready=true;
                    }

                    @Override
                    public void onCancel() {
                    }
                });*/
                map.moveCamera(cameraUpdate);
                ready = true;
            }
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d("DEBUG", msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the connection to the location client
     * drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        //Toast.makeText(this, "Long Press to " + point.toString(), Toast.LENGTH_LONG).show();
        //showAlertDialogForPoint(point);

        doAnimation();
    }

    private void doAnimation(){

        if(markers!=null){

            // Handler allows us to repeat a code block after a specified delay
            final android.os.Handler handler = new android.os.Handler();
            final long start = SystemClock.uptimeMillis();
            final long durationForPic = 2000;//2 sec
            final long durationTransition = 15;//15ms

            int size = markers.size();
            shown = new ArrayList<Boolean>();

            for(int i=0; i<size; i++){
                shown.add(new Boolean(false));
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;

                    if(markerIndex >= markers.size()){
                        Log.d("DEBUG", "end of animation");
                    }
                    else if(shown.get(markerIndex)==false){
                        //pic not shown before

                        markers.get(markerIndex).showInfoWindow();
                        shown.remove(markerIndex);
                        shown.add(markerIndex, new Boolean(true));
                        // Post this event again 1s from now.
                        handler.postDelayed(this, durationForPic);
                    }
                    else{
                        //pic has been shown, switch to next photo
                        markers.get(markerIndex).hideInfoWindow();

                        // Post this event again 15ms from now.
                        handler.postDelayed(this, durationTransition);
                        markerIndex ++;
                    }                }
            });

        }
    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

}
