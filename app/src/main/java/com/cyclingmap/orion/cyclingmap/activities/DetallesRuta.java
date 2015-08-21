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
import com.cyclingmap.orion.cyclingmap.model.Province;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;


public class DetallesRuta extends ActionBarActivity implements LocationListener{

    Button btnPlay;
    Route route;
    private TextView txtProvince;
    private TextView txtTown;
    private TextView txtDistanceDetail;
    private TextView txtDurationDetail;
    private TextView txtLevelDetail;
    ArrayList<Province> routeProvinces;
    private ArrayList routeCoords;
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
        btnPlay = (Button)findViewById(R.id.btnRuta_Det_Play);
        receiveData();
    }
    public void receiveData(){
        Bundle bundle = getIntent().getExtras();
        route = (Route) bundle.get("routeFinded");

        if(route != null) {
            //Aca se recibe los datos PROVINCIA, CANTON, DISTANCIA, DURACION Y NIVEL
            String province = route.getProvinces().get(0).toString();
            //        .getInt("province")+"";
            //String town = b.getDouble("town")+"";
            String distance = bundle.getString("Distance");
            //Long duration = bundle.getLong("Duration");
            String nivel = bundle.getString("Level");

           // String timeDuration = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
           //         TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
           //         TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
           // txtProvince.setText(province);
           // txtTown.setText(town);
            txtDistanceDetail.setText(distance + "");
           // txtDurationDetail.setText(timeDuration + "");
            txtLevelDetail.setText( nivel + "");
            drawRouteDetail();
        }
    }

    public void drawRouteDetail(){
        //Draw the route on fragmentmap
        polylineOptions.addAll(routeCoords);
        polylineOptions.width(12);
        polylineOptions.color(Color.RED);
        mapDetail.addPolyline(polylineOptions);
        LatLng[] coords = new LatLng[2];
        coords[0] = (LatLng) routeCoords.get(0);
        coords[1] = (LatLng) routeCoords.get(routeCoords.size() - 1);
        LocationAddress.getRouteInfo(coords, DetallesRuta.this, new GeocoderHandler());
    }
    public void beginRoute(){
        Intent i = new Intent(DetallesRuta.this, ExistingRoute.class);
        startActivity(i);
    }
    @Override
    public void onLocationChanged(Location location) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

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
