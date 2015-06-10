package com.mycompany.traveljournal.mapscreen;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

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
import com.mycompany.traveljournal.common.LocationOnConnectListener;
import com.mycompany.traveljournal.common.LocationService;
import com.mycompany.traveljournal.detailsscreen.DetailActivity;
import com.mycompany.traveljournal.helpers.Util;
import com.mycompany.traveljournal.models.Post;
import com.mycompany.traveljournal.service.JournalApplication;
import com.mycompany.traveljournal.service.JournalCallBack;
import com.mycompany.traveljournal.service.JournalService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by ekucukog on 6/5/2015.
 */
public class MapActivity extends ActionBarActivity implements
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraChangeListener{

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private final static String TAG = "MapActivityDebug";

    private boolean ready = false;
    private ArrayList<Marker> markers = null;
    private int markerIndex =0;
    private ArrayList<Boolean> shown= null;
    private ArrayList<Post> currentPosts = null;
    private String m_query;
    private LatLng m_location;
    private JournalService client;
    private LocationService locationService;
    private HashMap markersToPosts = new HashMap();

    //private ArrayList<LatLng> points=null;
    //private LatLng fixAddress = new LatLng(37.533278, -122.237933);//my redwood shores address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        markers = new ArrayList<>();
        m_query = getIntent().getStringExtra("query");
        m_location = Util.getLocationFromQuery(this, m_query);
        client = JournalApplication.getClient();

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                    map.setInfoWindowAdapter(new CustomWindowAdapter(MapActivity.this, getLayoutInflater()));
                }
            });
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Attach long click listener to the map here
            map.setOnMapLongClickListener(this);
            map.setOnCameraChangeListener(this);

            // Map is ready
            Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map.setMyLocationEnabled(true);

            //get current location
            locationService = new LocationService(this, new LocationOnConnectListener() {
                @Override
                public void onLocationAvailable(Location location) {
                    if (m_location == null) {// if query location is null, use current location
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, Util.ZOOM_MEDIUM);
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
                }
            });

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(final Marker mark) {

                    mark.showInfoWindow();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mark.showInfoWindow();

                        }
                    }, 200);

                    return true;
                }
            });

            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    //Call detail page from here
                    Post post = (Post)markersToPosts.get(marker);
                    Intent i = new Intent(MapActivity.this, DetailActivity.class);
                    i.putExtra("post_id", post.getPostID());
                    startActivity(i);
                }
            });

            //make sure map camera goes to target location
            setTargetLocation(m_location);

            locationService.connectClient();

        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTargetLocation(LatLng location) {

        if(location!=null){
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, Util.ZOOM_MEDIUM);
            map.animateCamera(cameraUpdate);
            ready = true;
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        Log.d(TAG, "on camera change");

        //if(points==null && ready) {

            //map.clear();
            //clear also all hashmaps and arraylists

            //Log.d(TAG, "camera change - location is set/ready");
            // Create random points within the boundaries of the map
            //points = createRandomPointsOnVisibleMap();//this method will be replaces by querying parse
            getPostsOnCurrentWindowAndPutPins();

            // Put a pin for each.
            //putPins(points);
        //}
    }

    private void getPostsOnCurrentWindowAndPutPins(){

        currentPosts = new ArrayList<>();

        LatLngBounds curScreen = map.getProjection().getVisibleRegion().latLngBounds;

        LatLng ne = curScreen.northeast;
        LatLng sw = curScreen.southwest;

        Log.d(TAG, "Screen boundaries. ne: " + ne.toString() + ", sw: " + sw.toString());

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
                //Toast.makeText(, "parse call successful", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < posts.size(); i++) {

                    Post post = posts.get(i);

                    if (post == null) {
                        Log.d(TAG, "post is null");
                    } else {
                        Log.d(TAG, "post is: " + post.toString());

                        //LatLng point = new LatLng(37.5513928, -122.2865121);
                        LatLng point = new LatLng(post.getLatitude(), post.getLongitude());

                        Log.d(TAG, "Adding post point: " + point.toString());

                        currentPosts.add(post);
                        putSinglePin(post);
                    }
                }

                //putPins(points);
            }
            @Override
            public void onFailure(Exception e) {
                //Toast.makeText(this, "parse call failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to get posts");
            }
        });
    }

    private void putSinglePin(Post post){

        Log.d(TAG, "Will put pin for post: " + post.toString());

        // Define color of marker icon
        BitmapDescriptor defaultMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

        LatLng point = new LatLng(post.getLatitude(), post.getLongitude());

        String imageUrl = post.getImageUrl();
        String caption = post.getCaption();

        // Creates and adds marker to the map
        Marker marker = map.addMarker(new MarkerOptions()
                .position(point)
                .title(caption)
                .snippet(imageUrl)
                .icon(defaultMarker));

        markers.add(marker);
        markersToPosts.put(marker, post);
        Log.d(TAG, "Marked pin at point: " + point.toString());
    }

    /*
     * Called when the Activity becomes visible.
    */
    @Override
    protected void onStart() {
        super.onStart();
        if(locationService!=null) {
            locationService.connectClient();
        }
    }

    /*
	 * Called when the Activity is no longer visible.
	 */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if(locationService!=null) {
            locationService.disconnect();
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

            case Util.CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        locationService.connect();
                        break;
                }
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        //Toast.makeText(this, "Long Press to " + point.toString(), Toast.LENGTH_LONG).show();
        //showAlertDialogForPoint(point);

        //doAnimation();
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
                        Log.d(TAG, "end of animation");
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
                    }
                }
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

    private ArrayList<LatLng> createRandomPointsOnVisibleMap(){

        Random r = new Random();

        ArrayList<LatLng> points = new ArrayList<>();

        LatLngBounds curScreen = map.getProjection().getVisibleRegion().latLngBounds;

        LatLng ne = curScreen.northeast;
        LatLng sw = curScreen.southwest;

        Log.d(TAG, "Screen boundaries. ne: " + ne.toString() + ", sw: " + sw.toString());

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

            Log.d(TAG, "Adding random point: " + point.toString());

            points.add(point);
        }
        return points;
    }

    private void putPins(ArrayList<LatLng> points){

        Log.d(TAG, "Entering put pins");

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
            Log.d(TAG, "Marking pin for point: " + point.toString());

            //dropPinEffect(marker);
        }
    }
}
