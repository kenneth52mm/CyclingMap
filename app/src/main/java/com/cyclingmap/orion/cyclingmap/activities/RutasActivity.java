package com.cyclingmap.orion.cyclingmap.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import android.content.Intent;
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

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

import com.cyclingmap.orion.cyclingmap.R;



public class RutasActivity extends FragmentActivity implements LocationListener{

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

    private TextView tvTiempo, tvDistan, tvVeloProm, tvKm, tvKmH;
    private Chronometer chrono;
    private Button btnRutaStar, btnRutasStop, btnMapShow, btnEndTrace;
    private LinearLayout layoutMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);

        tvTiempo = (TextView) findViewById(R.id.lblRutasTiempo);
        tvDistan = (TextView) findViewById(R.id.lblRutasDistancia);
        tvVeloProm = (TextView) findViewById(R.id.lblRutasVelProm);
        tvKm =  (TextView) findViewById(R.id.lblRtuasKm);
        tvKmH =  (TextView) findViewById(R.id.lblRutasKmH);
        chrono = (Chronometer) findViewById(R.id.chrono);
        btnRutaStar = (Button) findViewById(R.id.btnStarRutas);
        btnRutasStop = (Button) findViewById(R.id.btnRutasStop);
        btnMapShow = (Button) findViewById(R.id.btnShowMap);
        btnEndTrace = (Button) findViewById(R.id.btnEndTraceActivity);
        layoutMap = (LinearLayout) findViewById(R.id.linearLayMapRoad);

        //Area de mapa
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapRutas);
        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);
        polylineOptions = new PolylineOptions();
        lc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        dbHelper = new DBHelper(getApplicationContext());
        getCurrentLocation();

        //Estilos para fuente
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        tvTiempo.setTypeface(tf);
        tvDistan.setTypeface(tf);
        tvVeloProm.setTypeface(tf);
        tvKm.setTypeface(tf);
        tvKmH.setTypeface(tf);
        chrono.setTypeface(tf);
        btnRutaStar.setTypeface(tf);
        btnRutasStop.setTypeface(tf);

        //Accion del boton start
        btnRutaStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnRutaStar.setVisibility(View.INVISIBLE);
                btnRutasStop.setVisibility(View.VISIBLE);
            }
        });

        //Accion del boton stop
        btnRutasStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRutasStop.setVisibility(View.INVISIBLE);
                btnRutaStar.setVisibility(View.INVISIBLE);
                btnEndTrace.setVisibility(View.VISIBLE);
            }
        });

        //Go to entrace activity
        btnEndTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRutasStop.setVisibility(View.INVISIBLE);
                btnRutaStar.setVisibility(View.VISIBLE);
                btnEndTrace.setVisibility(View.INVISIBLE);

                Intent i = new Intent( getApplicationContext(), EndTraceActivity.class);
                startActivity(i);
            }
        });

        //Accion del boton mostrar mapa
        btnMapShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutMap.setVisibility(View.VISIBLE);
            }
        });

    }//END ON Create

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

        String td= getTotalDistance() + "";
        String ch = chrono.getBase() + "";
        double dist = getTotalDistance();
        tvDistan.setText("Distancia: "+ getTotalDistance());
        dbHelper.addCoords(coords);
        double speedAvg = ((double) dist / chrono.getBase());
        Intent i = new Intent(getApplicationContext(), EndTraceActivity.class);
        i.putExtra("route", (Serializable) route);
        i.putExtra("Distance",td);
        i.putExtra("Duration", ch);
        i.putExtra("Speed", speedAvg );

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


    public void startTrace(View v) {
        RUNNING = true;
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();

    }

    public void stopTrace(View v) {
        RUNNING = false;
        chrono.stop();
        Log.i("Tiempo ", "" + chrono.getBase());
        speed = ((long) distance / chrono.getBase()) / 1000;
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
            centerMapOnMyLocation();
        }
        if (route.size() == 1) {
            centerMapOnMyLocation();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, this);
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        chrono.setBase(-chronSystemClock.elapsedRealtime()-chrono.getBase());
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
       chrono.setBase(chronSystemClock.elapsedRealtime()-chronoo.getBase());
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
