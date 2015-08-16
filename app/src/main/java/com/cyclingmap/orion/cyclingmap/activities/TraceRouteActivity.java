package com.cyclingmap.orion.cyclingmap.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
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
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;


public class TraceRouteActivity extends FragmentActivity implements LocationListener {

    private GoogleMap map;
    private LocationManager locationManager;
    private ArrayList<LatLng> route = new ArrayList<>();
    private ArrayList<Coordinate> coords = new ArrayList<>();
    private PolylineOptions polylineOptions;
    private Location lc;
    private boolean RUNNING = false;
    private RouteWsHelper routeWsHelper = new RouteWsHelper();
    private double distance;
    private long speed;
    private DBHelper dbHelper;
    // labels and chronometer
    private TextView txtDistance;
    private TextView txtSpeed;
    private Chronometer chronometer;
    private ImageView btnPlay;
    private ImageView btnStop;
    private ArrayList routeCoords;
    private boolean flag = true;
    long timeRan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_route);

        chronometer = (Chronometer) findViewById(R.id.chronometerTrace);
        txtDistance = (TextView) findViewById(R.id.txt_dist_trace);
        txtSpeed = (TextView)findViewById(R.id.txt_speed_trace);
        btnPlay = (ImageView) findViewById(R.id.btnPlayER);
        btnStop = (ImageView) findViewById(R.id.BtnStop);

        //Area de mapa
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTrace);
        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);
        polylineOptions = new PolylineOptions();
        lc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        dbHelper = new DBHelper(getApplicationContext());
        getCurrentLocation();
        buttonTest();
        activateLocation();
    }
    public void seeRoute(View v){
        chronometer.stop();
        loadMap(v);
        String td = getTotalDistance() + "";
        String ch = chronometer.getBase() + "";
        String sp = speed + "";

        double dist = getTotalDistance() / 1000; // km
        long timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
        long hours = (timeElapsed / 3600000); //H
        long minutes = ((timeElapsed - hours * 3600000) /60000);
        long seconds = ((timeElapsed - hours * 3600000 - minutes * 60000) / 1000);

        double j = (double) hours;
        double speedavg = dist/j;

        double speedAvg = (double) dist / timeElapsed;
        speed = ((long) speedAvg / chronometer.getBase());
        String rt= dist + " Km" + "";
        String dt = hours + " Hrs"+ "";
        String tm =  speedavg + " Km/h"+ "";

        Intent i = new Intent(TraceRouteActivity.this, EndTraceActivity.class);
        i.putExtra("route", (Serializable) route);
        i.putExtra("Distance", dist);
        i.putExtra("Duration", timeElapsed);
        i.putExtra("Speed", speedAvg);
        startActivity(i);
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
    public void startTrace(View v) {
        RUNNING = true;
        chronometer.setBase(SystemClock.elapsedRealtime() + timeRan);
        chronometer.start();
    }
    public void pauseTrace(View v){
        timeRan = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.getBase();
        chronometer.stop();
    }
    public void stopTrace(View v) {
        RUNNING = false;
        //chrono.stop();
        // chrono.setBase(SystemClock.elapsedRealtime());
        //  timeStopped = 0;
        speed = ((long) distance / chronometer.getBase()) / 1000;
        LatLng[] coords = new LatLng[1];
        coords[0] = new LatLng(9.799982, -84.033366);
        LocationAddress.getRouteInfo(coords, getApplicationContext(), new GeocoderHandler());
        //Muestro distancia y velocidad
        NumberFormat numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(2);
        numFormat.setRoundingMode(RoundingMode.DOWN);
        Double distan = this.getTotalDistance() / 1000;
        txtDistance.setText(numFormat.format(distan) + "");
        txtSpeed.setText(numFormat.format(this.getTotalDistance() / (chronometer.getBase() / 3600000)) + "");
        loadMap(v);
    }
    public void centerMapOnMyLocation() {
        LatLng current = route.get(0);
        CameraPosition myPosition = new CameraPosition.Builder()
                .target(current).zoom(17).bearing(90).tilt(30).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
    }
    public void loadMap(View v) {
        polylineOptions.addAll(route);
        polylineOptions.width(10);
        polylineOptions.color(Color.RED);
        map.addPolyline(polylineOptions);
    }
    public void buttonTest() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    btnPlay.setImageResource(R.mipmap.ic_pause);
                    RUNNING = true;
                    startTrace(v);
                    flag = false;
                } else {
                    btnPlay.setImageResource(R.mipmap.ic_play);
                    pauseTrace(v);
                    flag = true;
                }
            }
        });
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
    public void activateLocation(){
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_title));
            builder.setMessage(R.string.dialog_message);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }
    //  @Override
    //   protected void onResumeFragments() {
    //       super.onResumeFragments();
    //       chronometer.setBase(SystemClock.elapsedRealtime() - chronometer.getBase());
    //  }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

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
                //txtDistance.setText(p.getNameProvince());
            }
        }
    }

    public void routeToLocalData(Route route) {
        dbHelper.addRoute(route);
    }
}
