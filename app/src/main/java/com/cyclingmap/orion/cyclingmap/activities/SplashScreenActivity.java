package com.cyclingmap.orion.cyclingmap.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;


import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.cyclingmap.orion.cyclingmap.model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.readystatesoftware.viewbadger.BadgeView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class SplashScreenActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long SPLASH_SCREEN_DELAY = 3500;
    private GoogleApiClient mGoogleApiClient;
    private DBHelper dbHelper;
    private ArrayList<Route> routes = new ArrayList<>();
    private int id_user = 0;
    private String[] user_stats = new String[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
        mGoogleApiClient.connect();
        dbHelper = new DBHelper(getApplicationContext());
        id_user = 13;
        if (id_user > 0) {
            LoadUserStats userStats = new LoadUserStats();
            userStats.execute(id_user);
        } else {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent mainIntent = null;
                    if (mGoogleApiClient.isConnected() || dbHelper.isLogged()) {
                        //id_user = dbHelper.getIdUser();

//                    mainIntent = new Intent().setClass(
//                            SplashScreenActivity.this, HomeActivity.class);
//                    getProfileInformation();
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

    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null && mGoogleApiClient.isConnected()) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                User u = new User();
                u.setId(13);
                u.setEmail(email);
                u.setName(currentPerson.getDisplayName());
                dbHelper.addUser(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    class LoadUserStats extends AsyncTask<Integer, String, String> {

        String distance;
        String speed;
        String totalRoutes;

        @Override
        protected String doInBackground(Integer... params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://orion-group.azurewebsites.net/Api/user/stats/" + params[0]);
            httpGet.setHeader("content-type", "application/json");
            try {
                HttpResponse response = client.execute(httpGet);
                JSONArray array = new JSONArray(EntityUtils.toString(response.getEntity()));
                distance = array.getString(0);
                speed = array.getString(1);
                totalRoutes = array.getString(2);
            } catch (Exception ex) {
                Log.i("Error", "" + ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            user_stats[0] = distance;
            user_stats[1] = speed;
            user_stats[2] = totalRoutes;
//            txtDistance.setText(distance + " Km");
//            txtBestSpeed.setText(speed + " Km/h");
//            txtTotalRoutes.setText(totalRoutes);
            UserChallengesHelper userChallengesHelper = new UserChallengesHelper();
            userChallengesHelper.execute(id_user);
        }
    }

    class UserChallengesHelper extends AsyncTask<Integer, ArrayList, ArrayList> {

        ArrayList<Route> respRoutes;
        private final ProgressDialog dialog = new ProgressDialog(SplashScreenActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getString(R.string.loading_dialog));
            dialog.show();
        }

        @Override
        protected ArrayList doInBackground(Integer... params) {
            // android.os.Debug.waitForDebugger();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://orion-group.azurewebsites.net/Api/user/challenges/" + params[0]);
            httpGet.setHeader("content-type", "application/json");
            Log.i("entro", "entro");
            try {
                HttpResponse response = client.execute(httpGet);
                JSONArray jsonArray = new JSONArray(EntityUtils.toString(response.getEntity()));
                for (int i = 0; i < jsonArray.length(); i++) {
                    Route route = new Route();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Log.i("ENTRO", "" + i);
                    route.setIdRoute(jsonObject.getInt("IdRoute"));
                    route.setDistance(jsonObject.getDouble("Distance"));
                    route.setAvgSpeed(jsonObject.getDouble("AvgSpeed"));
                    JSONArray jsonCoords = jsonObject.getJSONArray("CoordinateList");
                    ArrayList<Coordinate> coordinates = new ArrayList<>();
                    for (int j = 0; j < jsonCoords.length(); j++) {
                        JSONObject coord = jsonCoords.getJSONObject(j);
                        double x = coord.getDouble("X");
                        double y = coord.getDouble("Y");
                        coordinates.add(new Coordinate(x, y));
                    }
                    route.setCoordinateList(coordinates);
                    route.setTimeToFin(Time.valueOf(jsonObject.getString("TimeToFin")));
                    route.setDifficultyLevel(String.valueOf(jsonObject.getInt("DifficultyLevel")));
                    routes.add(route);
                }
            } catch (Exception ex) {
                Log.i("Error", "" + ex);
            }
            return routes;
        }

        @Override
        protected void onPostExecute(ArrayList array) {
            routes = array;
            for (Route r : routes) {
                dbHelper.addChallenges(r);
            }
            int size = routes.size();
            View target = findViewById(R.id.action_badge);
//            BadgeView badge = new BadgeView(.this, target);
//            badge.setText(routes.size() + "");
//            badge.show();
            dialog.dismiss();
            Intent mainIntent = new Intent().setClass(SplashScreenActivity.this, HomeActivity.class);
            mainIntent.putExtra("user_stats", user_stats);
            mainIntent.putExtra("challenges", size);
            startActivity(mainIntent);
        }
    }
}
