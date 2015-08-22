package com.cyclingmap.orion.cyclingmap.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.NetworkUtil;
import com.cyclingmap.orion.cyclingmap.business.RouteWsHelper;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.data.LocationAddress;
import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Province;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.cyclingmap.orion.cyclingmap.model.Town;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class EndTraceActivity extends FragmentActivity implements LocationListener {

    private TextView txtdistancefinal;
    private TextView txtDuration;
    private TextView txtSpeedAvg;
    private GoogleMap map;
    private Location lc;
    private LocationManager locationManager;
    private PolylineOptions polylineOptions;
    private RouteWsHelper routeWsHelper = new RouteWsHelper();
    ArrayList<Province> routeProvinces;
    private ArrayList<LatLng> routeCoords;
    private ArrayList<Coordinate> coordinates = new ArrayList<>();
    //String[] arrayLevel = {"Principiante", "Intermedio", "Avanzado"};
    Spinner spinner_level;
    private Route route;
    private int id_user;
    private DBHelper dbHelper;
    private double distance;
    private long duration;
    private double speed;
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_trace);
        String[] arrayLevel = {getString(R.string.beginner_level), getString(R.string.intermediate_level), getString(R.string.advanced_level)};
        txtdistancefinal = (TextView) findViewById(R.id.TxtDistTotal);
        txtDuration = (TextView) findViewById(R.id.TxtDuration);
        txtSpeedAvg = (TextView) findViewById(R.id.TxtSpeedAvg);
        spinner_level = (Spinner) findViewById(R.id.spinnerEnd);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);
        polylineOptions = new PolylineOptions();
        lc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        dbHelper = new DBHelper(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        routeCoords = (ArrayList<LatLng>) bundle.get("route");
        // coordinates= (ArrayList<Coordinate>) bundle.get("coords");
        for (LatLng latLng : routeCoords)
            coordinates.add(new Coordinate(latLng.latitude, latLng.longitude));
        distance = bundle.getDouble("Distance");
        duration = bundle.getLong("Duration");
        speed = bundle.getDouble("Speed");
        String time = String.valueOf(duration);
        txtdistancefinal.setText(distance + "Km");
        String timeFinished = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
                TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        txtDuration.setText(timeFinished + " Min");
        txtSpeedAvg.setText(speed + "Km/h");
        LatLng current = (LatLng) routeCoords.get(0);
        CameraPosition myPosition = new CameraPosition.Builder()
                .target(current).zoom(17).bearing(90).tilt(30).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));

        //Draw the route on fragmentmap
        polylineOptions.addAll(routeCoords);
        polylineOptions.width(12);
        polylineOptions.color(Color.GREEN);
        map.addPolyline(polylineOptions);
        ArrayAdapter a_level = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayLevel);
        a_level.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_level.setAdapter(a_level);
    }

    public void sendData(View v) {
        route = new Route();
        route.setDistance(distance);
        route.setTimeToFin(new Time(duration));
        route.setAvgSpeed(speed);
        route.setDifficultyLevel(spinner_level.getSelectedItemPosition()+1);
        route.setCoordinateList(coordinates);
        int netStatus = NetworkUtil.getConnectivityState(EndTraceActivity.this);
        switch (netStatus) {
            case 0:
                long resp = dbHelper.addRoute(route);
                if (resp != -1)
                    Toast.makeText(this, "Guardado", Toast.LENGTH_LONG);
                break;
            case 1:
                LatLng[] coords = new LatLng[2];
                coords[0] = (LatLng) routeCoords.get(0);
                LocationAddress.getRouteInfo(coords, EndTraceActivity.this, new GeocoderHandler());

                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

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

    private class GeocoderHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    routeProvinces = (ArrayList<Province>) bundle.getSerializable("direccion");
                    route.setProvinces(routeProvinces);
                    RouteWsHelper helper = new RouteWsHelper();
                    helper.setContext(EndTraceActivity.this);
                    Object[] obj = new Object[2];
                    obj[0] = route;
                    obj[1] = 13;
                    helper.execute(obj);
                    break;
                default:
                    routeProvinces = null;
            }
        }
    }
}
