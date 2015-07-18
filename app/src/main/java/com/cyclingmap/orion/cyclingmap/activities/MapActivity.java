package com.cyclingmap.orion.cyclingmap.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.RouteWsHelper;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.data.LocationAddress;
import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Province;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;


public class MapActivity extends FragmentActivity implements LocationListener {
    private GoogleMap map;
    private LocationManager locationManager;
    private static ArrayList<LatLng> route = new ArrayList<>();
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
<<<<<<< HEAD
        //  polylineOptions.addAll(route);
        //  polylineOptions.width(12);
        //  polylineOptions.color(Color.RED);
        //  map.addPolyline(polylineOptions);

        String td = getTotalDistance() + "";
        String ch = chronometer.getBase() + "";
        String sp = speed + "";
        double dist = getTotalDistance();
=======
//        polylineOptions.addAll(route);
//        polylineOptions.width(12);
//        polylineOptions.color(Color.RED);
//        map.addPolyline(polylineOptions);

        double dist = getTotalDistance() / 1000; // km
>>>>>>> origin/master

        long timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
        long hours = (timeElapsed / 3600000); //H
        long minutes = ((timeElapsed - hours * 3600000) /60000);
        long seconds = ((timeElapsed - hours * 3600000 - minutes * 60000) / 1000);

        double j = (double) hours;
        double speedavg = dist/j;


<<<<<<< HEAD
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, distance2);

        txtDistance.setText("Distancia: " + distance / 1000 + " otra:" + getTotalDistance());

        // dbHelper.addCoords(coords);

        //Code to go to EndTraceActivity with the extras

        double speedAvg = ((double) dist / chronometer.getBase());
        speed = ((long) speedAvg / chronometer.getBase());
=======
        String rt= dist + " Km" + "";
        String dt = hours + " Hrs"+ "";
        String tm =  speedavg + " Km/h"+ "";
>>>>>>> origin/master

        Intent i = new Intent(getApplicationContext(), EndTraceActivity.class);
        i.putExtra("route", (Serializable) route);
<<<<<<< HEAD
        i.putExtra("Distance", td);
        i.putExtra("Duration", ch);
        i.putExtra("Speed", sp);

=======
        i.putExtra("Distance",rt);
        i.putExtra("Duration", dt);
        i.putExtra("Speed", tm );
>>>>>>> origin/master
        startActivity(i);
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
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
        coordinates.add(new Coordinate(20.0, 30.1));
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
        LatLng[] coords = new LatLng[1];
        coords[0] = new LatLng(9.799982, -84.033366);
        LocationAddress.getRouteInfo(coords, getApplicationContext(), new GeocoderHandler());

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
        //dbHelper.addCoords(coords);
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            ArrayList<Province> locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = (ArrayList<Province>) bundle.getSerializable("direccion");
                    break;
                default:
                    locationAddress = null;
            }
            if (locationAddress != null) {
                Province p = locationAddress.get(0);
                txtDistance.setText(p.getNameProvince());
            }
        }
    }

    public void routeToLocalData(Route route) {
        dbHelper.addRoute(route);
    }
}
