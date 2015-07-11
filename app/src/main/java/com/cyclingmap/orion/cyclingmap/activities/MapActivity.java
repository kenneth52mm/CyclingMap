package com.cyclingmap.orion.cyclingmap.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.RouteWsHelper;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


import java.sql.Time;
import java.util.ArrayList;


public class MapActivity extends FragmentActivity implements LocationListener {
    private GoogleMap map;
    private LocationManager locationManager;
    private ArrayList<LatLng> route = new ArrayList<>();
    private ArrayList<Coordinate> coords = new ArrayList<>();
    private PolylineOptions polylineOptions;
    private Location lc;
    private boolean RUNNING = false;
    private RouteWsHelper routeWsHelper = new RouteWsHelper();
    private double distance;
    private TextView txtDistance;
    private Chronometer chronometer;
    private long speed;
    private DBHelper dbHelper;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);
        polylineOptions = new PolylineOptions();
        lc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        dbHelper = new DBHelper(getApplicationContext());
        getCurrentLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getCurrentLocation() {

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Location loc = map.getMyLocation();
                if (loc != null)
                    Log.i("Current loc", loc.getLatitude() + " " + loc.getLongitude());
                else
                    Log.i("Current loc", " cant get");
                return false;
            }
        });
    }

    public void centerMapOnMyLocation() {
        LatLng current = route.get(0);
        CameraPosition myPosition = new CameraPosition.Builder()
                .target(current).zoom(17).bearing(90).tilt(30).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
    }

    public void verRuta(View v) {
        polylineOptions.addAll(route);
        polylineOptions.width(12);
        polylineOptions.color(Color.RED);
        map.addPolyline(polylineOptions);
        LatLng start = route.get(0);
        LatLng end = route.get(route.size() - 1);
        distance = getDistance(start.latitude, end.latitude, start.longitude, end.longitude);
        float[] distance2 = new float[1];
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, distance2);
        txtDistance.setText("Distancia: " + distance / 1000 + " otra:" + getTotalDistance());
        dbHelper.addCoords(coords);
    }

    public double getDistance(double startOne, double endOne, double startTwo, double endTwo) {
        Location locationA = new Location("");
        locationA.setLatitude(startOne);
        locationA.setLongitude(startTwo);
        Location locationB = new Location("");
        locationB.setLatitude(endOne);
        locationB.setLongitude(endTwo);
        double distance = locationA.distanceTo(locationB);
        return distance;
    }

    private double getTotalDistance() {
        double totalDistance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            LatLng start = route.get(i);
            LatLng end = route.get(i + 1);
            Location locationA = new Location("");
            locationA.setLatitude(start.latitude);
            locationA.setLongitude(start.longitude);
            Location locationB = new Location("");
            locationB.setLatitude(end.latitude);
            locationB.setLongitude(end.longitude);
            totalDistance += locationA.distanceTo(locationB);
        }
        return totalDistance;
    }

    public void sendData(View v) {
        //ArrayList<Coordinate> coordinates= (ArrayList<Coordinate>) dbHelper.retrieveAll();
        ArrayList<Coordinate> coordinates=new ArrayList<>();
        coordinates.add(new Coordinate(20.0,30.1));
        routeWsHelper.execute(coordinates);
    }

    public void startTrace(View v) {
        RUNNING = true;
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

    }

    public void stopTrace(View v) {
        RUNNING = false;
        chronometer.stop();
        Log.i("Tiempo ", "" + chronometer.getBase());
        speed = ((long) distance / chronometer.getBase()) / 1000;
        Log.i("Velocidad ", "" + speed);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onLocationChanged(Location location) {
        if (RUNNING) {
            route.add(new LatLng(location.getLatitude(), location.getLongitude()));
            coords.add(new Coordinate(location.getLatitude(), location.getLongitude()));
        }
        if (route.size() == 1) {
            centerMapOnMyLocation();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, this);
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void coordsToLocalData(ArrayList<Coordinate> coords) {
        dbHelper.addCoords(coords);
    }

    public void routeToLocalData(Route route){
        dbHelper.addRoute(route);
    }
}
