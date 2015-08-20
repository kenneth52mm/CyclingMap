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
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import org.w3c.dom.Text;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ExistingRoute extends FragmentActivity implements LocationListener {

    private GoogleMap map;
    private ArrayList<LatLng> route = new ArrayList<>();
    private ArrayList<Coordinate> coords = new ArrayList<>();
    private Location lc;
    private boolean RUNNING = false;
    private RouteWsHelper routeWsHelper = new RouteWsHelper();
    private double distance;
    private long speed;
    private DBHelper dbHelper;
    private TextView  txtSpeedExist;
    private ImageView buttonPlay;
    private ImageView stopTrace;
    private TextView txtRouteExist;
    private TextView txtDistExist;
    private TextView txtLevelExist;
    private LocationManager locationManager;
    private PolylineOptions polylineOptions;
    private Location location;
    //Receive Route
    private ArrayList routeCoords;
    private Chronometer chronometerExistRoute;
    private boolean flag = true;
    long timeRunning = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_route);

        txtDistExist = (TextView)findViewById(R.id.txt_dist_exist);
        txtSpeedExist = (TextView)findViewById(R.id.txt_speed_exist);
        chronometerExistRoute = (Chronometer) findViewById(R.id.chrono_exist);
        buttonPlay = (ImageView) findViewById(R.id.btnPlayER);
        stopTrace = (ImageView)findViewById(R.id.imageStop);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
       // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

       // LatLng current = (LatLng) routeCoords.get(0);
       // CameraPosition myPosition = new CameraPosition.Builder()
      //          .target(current).zoom(17).bearing(90).tilt(30).build();
      //  map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapExistRoute);
        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);
        polylineOptions = new PolylineOptions();
        lc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        dbHelper = new DBHelper(getApplicationContext());

        getCurrentLocation();
        test();
        activateLocation();
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

    public void seeRoute(View v) {
        polylineOptions.addAll(route);
        polylineOptions.width(12);
        polylineOptions.color(Color.BLUE);
        map.addPolyline(polylineOptions);

        double distance_trace = getTotalDistance() / 1000; // km
        long timeElapsed = SystemClock.elapsedRealtime() - chronometerExistRoute.getBase();
        long hours = (timeElapsed / 3600000); //H
        long minutes = ((timeElapsed - hours * 3600000) / 60000);
        long seconds = ((timeElapsed - hours * 3600000 - minutes * 60000) / 1000);

        double j = (double) hours;
        double speedavg = distance_trace / j;

        double speedAvg = ((double) distance_trace / timeRunning);
        speed = ((long) speedAvg / timeRunning);

        String totalDistance = distance_trace + " Km" + "";
        String timeTour = hours + " Hrs" + "";
        String speedTour = speedavg + " Km/h" + "";

        Intent i = new Intent(getApplicationContext(), EndTraceActivity.class);
        i.putExtra("route", (Serializable) route);
        i.putExtra("Distance", totalDistance);
        i.putExtra("Duration", timeTour);
        i.putExtra("Speed", speedTour);
        startActivity(i);
    }

    public void startTrace(View v) {
        RUNNING = true;
        chronometerExistRoute.setBase(SystemClock.elapsedRealtime() + timeRunning);
        chronometerExistRoute.start();
    }
    public void pauseTrace(View v){
        timeRunning = chronometerExistRoute.getBase() - SystemClock.elapsedRealtime();
        chronometerExistRoute.getBase();
        chronometerExistRoute.stop();
    }

    public void test() {
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    buttonPlay.setImageResource(R.mipmap.ic_pause);
                    startTrace(v);
                    flag = false;
                } else {
                    buttonPlay.setImageResource(R.mipmap.ic_play);
                    pauseTrace(v);
                    flag = true;
                }
            }
        });
    }

    public void centerMapOnMyLocation() {
        LatLng current = route.get(0);
        CameraPosition myPosition = new CameraPosition.Builder()
                .target(current).zoom(17).bearing(90).tilt(30).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
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

    public void stopTrace(View v) {
        RUNNING = false;
        chronometerExistRoute.stop();
        speed = ((long) distance / timeRunning) / 1000;
        LatLng[] coords = new LatLng[1];
        coords[0] = new LatLng(9.799982, -84.033366);
        LocationAddress.getRouteInfo(coords, getApplicationContext(), new GeocoderHandler());
        NumberFormat numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(2);
        numFormat.setRoundingMode(RoundingMode.DOWN);
        Double distan = this.getTotalDistance()/1000;
        loadMap(v);
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

    public void loadMap(View v) {
        polylineOptions.addAll(route);
        polylineOptions.width(10);
        polylineOptions.color(Color.RED);
        map.addPolyline(polylineOptions);
    }


    //This method start the trail
    public void startRoute(View v){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_existing_route, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {

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
                //txtDistance.setText(p.getNameProvince());
            }
        }
    }

    public void routeToLocalData(Route route){
        dbHelper.addRoute(route);
    }

}
