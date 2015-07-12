package com.cyclingmap.orion.cyclingmap.activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class EndTraceActivity extends FragmentActivity implements LocationListener {

    private TextView txtdistancefinal;
    private TextView txtDuration;
    private TextView txtSpeedAvg;
    private PolylineOptions polylineOptions;
    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_trace);
        txtdistancefinal = (TextView)findViewById(R.id.TxtDistTotal);
        txtDuration = (TextView)findViewById(R.id.TxtDuration);
        txtSpeedAvg = (TextView)findViewById(R.id.TxtSpeedAvg);



        Bundle bundle = getIntent().getExtras();
        txtdistancefinal.setText(bundle.getString("Distance"));
        txtDuration.setText(bundle.getString("Duration"));
        txtSpeedAvg.setText(bundle.getString("Speed"));
        ArrayList route= (ArrayList) bundle.get("route");

        polylineOptions.addAll(route);
        polylineOptions.width(12);
        polylineOptions.color(Color.RED);
        map.addPolyline(polylineOptions);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end_trace, menu);
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
}
