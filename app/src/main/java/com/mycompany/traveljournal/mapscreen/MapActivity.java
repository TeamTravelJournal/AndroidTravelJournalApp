package com.mycompany.traveljournal.mapscreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
    private JournalService client;
    private LocationService locationService;
    private String m_query;
    private LatLng m_location;

    private ArrayList<Marker> markers = null;
    private ArrayList<Post> currentPosts = null;
    private HashMap markersToPosts = new HashMap();

    private int animationIndex =0;
    private ArrayList<Marker> sortedMarkers = null;
    private ArrayList<Boolean> shown= null;
    private ArrayList<Polyline> polylines = null;
    private Toolbar toolbarForMap;

    //private LatLng fixAddress = new LatLng(37.533278, -122.237933);//my redwood shores address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        setToolbar();

        m_query = getIntent().getStringExtra("query");
        m_location = Util.getLocationFromQuery(this, m_query);
        client = JournalApplication.getClient();

        markers = new ArrayList<>();
        currentPosts = new ArrayList<>();
        polylines = new ArrayList<>();

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

    private void setToolbar() {
        toolbarForMap = (Toolbar) findViewById(R.id.toolbarmap);
        if (toolbarForMap != null) {
            setSupportActionBar(toolbarForMap);

            // Set the home icon on toolbar
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_up_menu);
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
                        map.moveCamera(cameraUpdate);
                        //ready = true;
                    }
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
            //ready = true;
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        Log.d(TAG, "on camera change");

        //if(points==null && ready) {

            //map.clear();
            clearOutOfMap();

            //Log.d(TAG, "camera change - location is set/ready");
            // Create random points within the boundaries of the map
            //points = createRandomPointsOnVisibleMap();//this method will be replaces by querying parse
            getPostsOnCurrentWindowAndPutPins();

            // Put a pin for each.
            //putPins(points);
        //}
    }

    private void getPostsOnCurrentWindowAndPutPins(){

        double[] boundaries = getMapBoundaries();

        client.getPostsWithinWindow(boundaries[3], boundaries[0], boundaries[2], boundaries[1], Util.LIMIT_POST, new JournalCallBack<List<Post>>() {
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

                        Log.d(TAG, "Returned point: " + point.toString());
                        takeActionForPost(post);
                    }
                }
            }
            @Override
            public void onFailure(Exception e) {
                //Toast.makeText(this, "parse call failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to get posts");
            }
        });
    }

    private void takeActionForPost(Post post){

        if(!currentPosts.contains(post)){
            currentPosts.add(post);
            putSinglePin(post);
        }
    }

    private void clearOutOfMap(){

        LatLngBounds bounds = getMapBoundariesAsLatLngBounds();

        //for all markers, if it is outside the boundary, remove marker, remove post as well
        //start from end, as removing will mess up the greater indices
        for(int i=markers.size()-1; i>=0; i--){

            Post post = (Post) markersToPosts.get(markers.get(i));
            LatLng point = new LatLng(post.getLatitude(), post.getLongitude());

            if(!bounds.contains(point)){

                Log.d(TAG, "marker is out of map bounds for post : " + post.getPostID());
                markers.get(i).remove();//remove marker from map

                currentPosts.remove(post);//remove post from currentPost
                markersToPosts.remove(markers.get(i));//remove marker from hash
                markers.remove(i);//remove marker from array

            }else{
                //keep it.
                Log.d(TAG, "marker is still in map bounds for post : " + post.getPostID());
            }
        }

        Log.d(TAG, "current posts " + currentPosts.toString());
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

    private double[] getMapBoundaries(){

        if(map==null){
            return new double[]{0.0d,0.0d,0.0d,0.0d};
        }

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

        return new double[]{rangeMinLng, rangeMaxLng, rangeMaxLat, rangeMinLat};
    }

    private LatLngBounds getMapBoundariesAsLatLngBounds(){


        double[] boundaries = getMapBoundaries();

        LatLng sw = new LatLng(boundaries[3], boundaries[0]);
        LatLng ne = new LatLng(boundaries[2], boundaries[1]);

        return new LatLngBounds(sw, ne);
    }


    public ArrayList<Marker> getSortedMarkers(){

        ArrayList<Marker> sorted = new ArrayList<>();

        for(Marker marker: markers){

            //insert into right position
            int i =0;
            for(; i<sorted.size(); i++){
                Post currentPost = (Post)markersToPosts.get(marker);
                Post sortedPost = (Post)markersToPosts.get(sorted.get(i));
                if(currentPost.getCreatedAt().before(sortedPost.getCreatedAt())){
                    break;
                }
            }
            sorted.add(i, marker);
        }

        //Log.d(TAG, "sorted");
        return sorted;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        //Toast.makeText(this, "Long Press to " + point.toString(), Toast.LENGTH_LONG).show();
        doAnimation();
    }

    private void doAnimation(){

        sortedMarkers = getSortedMarkers();

        if(sortedMarkers!=null){

            //map.getUiSettings().setZoomGesturesEnabled(false);
            //map.getUiSettings().setScrollGesturesEnabled(false);

            // Handler allows us to repeat a code block after a specified delay
            final android.os.Handler handler = new android.os.Handler();
            final long start = SystemClock.uptimeMillis();
            final long durationForPic = 2000;//2 sec
            final long durationTransition = 15;//15ms

            shown = new ArrayList<>();
            for(int i=0; i<sortedMarkers.size(); i++){
                shown.add(new Boolean(false));
            }
            for(int i=0; i<polylines.size(); i++){
                polylines.get(i).remove();
            }
            polylines = new ArrayList<>();
            animationIndex =0;

            handler.post(new Runnable() {
                @Override
                public void run() {

                    if(animationIndex >= sortedMarkers.size()){
                        Log.d(TAG, "end of animation");

                    }
                    else if(shown.get(animationIndex)==false){
                        //pic not shown before

                        sortedMarkers.get(animationIndex).showInfoWindow();
                        shown.remove(animationIndex);
                        shown.add(animationIndex, new Boolean(true));
                        // Post this event again 1s from now.
                        handler.postDelayed(this, durationForPic);
                    }
                    else{
                        //pic has been shown, switch to next photo
                        sortedMarkers.get(animationIndex).hideInfoWindow();

                        //if not last marker, then
                        //draw a line between this and next point
                        if( animationIndex < sortedMarkers.size()-1 ){

                            Post current = (Post)markersToPosts.get(sortedMarkers.get(animationIndex));
                            Post next = (Post)markersToPosts.get(sortedMarkers.get(animationIndex+1));

                            LatLng currentPoint = new LatLng(current.getLatitude(), current.getLongitude());
                            LatLng nextPoint = new LatLng(next.getLatitude(), next.getLongitude());

                            Polyline polyline = map.addPolyline(new PolylineOptions()
                                    .add(currentPoint)
                                    .add(nextPoint)
                                    .color(Color.MAGENTA)
                                    .width(12));

                            polylines.add(polyline);
                            Log.d(TAG, "drawing a line here");
                        }

                        // Post this event again 15ms from now.
                        handler.postDelayed(this, durationTransition);
                        animationIndex ++;
                    }
                }
            });

            //map.getUiSettings().setZoomGesturesEnabled(true);
            //map.getUiSettings().setScrollGesturesEnabled(true);
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
        double[] boundaries = getMapBoundaries();

        for(int i =0; i<10; i++){
            double randomValueLng = boundaries[0] + (boundaries[1] - boundaries[0]) * r.nextDouble();
            double randomValueLat = boundaries[3] + (boundaries[2] - boundaries[3]) * r.nextDouble();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
