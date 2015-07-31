package com.cyclingmap.orion.cyclingmap.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;


import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.util.Timer;
import java.util.TimerTask;


public class SplashScreenActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long SPLASH_SCREEN_DELAY = 3500;
    private GoogleApiClient mGoogleApiClient;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
        mGoogleApiClient.connect();
        dbHelper = new DBHelper(getApplicationContext());

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = null;
                if (mGoogleApiClient.isConnected()||dbHelper.isLogged()) {
                    mainIntent = new Intent().setClass(
                            SplashScreenActivity.this, LogActivity.class);
                } else {
                    mainIntent = new Intent().setClass(

                            SplashScreenActivity.this, LogActivity.class);


                }
                startActivity(mainIntent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
