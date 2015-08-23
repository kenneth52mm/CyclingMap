package com.cyclingmap.orion.cyclingmap.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.UserRoutesAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;


public class DetallesRuta extends ActionBarActivity implements LocationListener {

    Button btnPlay;
    Route route;
    private TextView txtProvince;
    private TextView txtTown;
    private TextView txtDistanceDetail;
    private TextView txtDurationDetail;
    private TextView txtLevelDetail;
    ArrayList<Province> routeProvinces;
    private ArrayList<LatLng> routeCoords = new ArrayList<LatLng>();
    ArrayList<Route> routes = new ArrayList<>();
    ArrayList<Province> provinces;
    ArrayList<Coordinate> coordinates;
    int id_route = 0;
    private GoogleMap mapDetail;
    private Location lc;
    private LocationManager locationManager;
    private PolylineOptions polylineOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ruta);
        txtProvince = (TextView) findViewById(R.id.TxtProvince);
        txtTown = (TextView) findViewById(R.id.TxtTown);
        txtDistanceDetail = (TextView) findViewById(R.id.TxtDistance);
        txtDurationDetail = (TextView) findViewById(R.id.TxtDurationDetail);
        txtLevelDetail = (TextView) findViewById(R.id.TxtNivelRoad);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapDetalle);
        mapDetail = mapFragment.getMap();
        mapDetail.setMyLocationEnabled(true);
        polylineOptions = new PolylineOptions();
        lc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        receiveRoute();
        drawRouteDetail();
    }

    //Method that receive the route from user routes or challenges
    public void receiveRoute() {

        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        Route route = (Route) bundle.getSerializable("route");
        coordinates =
                coordinates = (ArrayList) bundle.get("routeFinded");
        ////provinces = (ArrayList<Province>) bundle.get("provinces");
        provinces = route.getProvinces();
        coordinates = route.getCoordinateList();
        //id_route = (int) bundle.get("idRoute");
        for (int j = 0; j < coordinates.size(); j++) {
            Coordinate c = coordinates.get(j);
            routeCoords.add(new LatLng(c.getX(), c.getY()));
        }
        if (bundle != null) {
            if (!routeCoords.isEmpty()) {
                drawRouteDetail();
            }
//            String distance = bundle.get("Distance") + "";
//            txtDistanceDetail.setText(distance + "");
//            route = (Route) bundle.get("Route");
            String distance = String.valueOf(route.getDistance());
            int level = route.getDifficultyLevel() + 1;
            String province = provinces.get(0).getNameProvince();
            String town = provinces.get(0).getTownList().get(0).getNameTown();

//            long duration = Long.parseLong(bundle.get("Duration") + "");
//            String level = bundle.getString("Level") + "";
            long duration = route.getTimeToFin().getTime();

            String timeDuration = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                    TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
            txtProvince.setText(province);
            txtTown.setText(town);
            txtDistanceDetail.setText(distance + "km");
            txtDurationDetail.setText(timeDuration + "min");
            txtLevelDetail.setText(level + "");
            drawRouteDetail();

        } else {
            txtProvince.setText("No");
            txtTown.setText("No");
            txtDistanceDetail.setText("0");
            txtDurationDetail.setText("00:00");
            txtLevelDetail.setText("No");
        }
    }


    //Method that draw the route on the map
    public void drawRouteDetail() {
        polylineOptions.addAll(routeCoords);
        polylineOptions.width(12);
        polylineOptions.color(Color.BLUE);
        mapDetail.addPolyline(polylineOptions);
//        LatLng[] coords = new LatLng[2];
        LatLng current = routeCoords.get(0);
//        coords[1] = (LatLng) routeCoords.get(routeCoords.size() - 1);
//        LocationAddress.getRouteInfo(coords, DetallesRuta.this, new GeocoderHandler());  LatLng current = (LatLng) routeCoords.get(0);
        CameraPosition myPosition = new CameraPosition.Builder()
                .target(current).zoom(30).bearing(90).tilt(30).build();
        mapDetail.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
    }

    //Method go to Existing route for start the route selected
    public void beginRoute() {
        Intent i = new Intent(DetallesRuta.this, ExistingRoute.class);
        i.putExtra("routeCoords", (Serializable) routeCoords);
        startActivity(i);
    }

    //Methods of locationListener
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

    //Method that find the provinces
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    routeProvinces = (ArrayList<Province>) bundle.getSerializable("direccion");
                    break;
                default:
                    routeProvinces = null;
            }
        }
    }
}
