package com.cyclingmap.orion.cyclingmap.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Entity;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.NetworkUtil;
import com.cyclingmap.orion.cyclingmap.business.UserRoutesAdapter;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Province;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.cyclingmap.orion.cyclingmap.model.Town;
import com.cyclingmap.orion.cyclingmap.model.User;
import com.cyclingmap.orion.cyclingmap.utils.BadgeDrawable;
import com.cyclingmap.orion.cyclingmap.utils.Dialog_Notification;
import com.cyclingmap.orion.cyclingmap.utils.addBadge;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
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
import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBar actionBar;
    private TextView txtDistance;
    private TextView txtBestSpeed;
    private TextView txtTotalRoutes;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Route> routes = new ArrayList<>();
    private DBHelper dbHelper;
    private String userProfile;
    private String emailProfile;
    private String heightProfile;
    private String weightProfile;
    private int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtDistance = (TextView) findViewById(R.id.dist);
        txtBestSpeed = (TextView) findViewById(R.id.speed);
        txtTotalRoutes = (TextView) findViewById(R.id.totalRoute);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
        dbHelper = new DBHelper(HomeActivity.this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        setupNavigationDrawerContent(navigationView);
//        Bundle bundle = getIntent().getExtras();
//        size = (int) bundle.get("challenges");
//        String[] user_stats = (String[]) bundle.get("user_stats");
//        txtDistance.setText(user_stats[0] + " km");
//        txtBestSpeed.setText(user_stats[1] + "km/h");
//        txtTotalRoutes.setText(user_stats[2]);
//
//        View target = findViewById(R.id.action_badge);
//        BadgeView badge = new BadgeView(this, target);
//        String not = String.valueOf(size);
//        badge.setText(not);
//        badge.show();
        int id_user=dbHelper.getIdUser();
        LoadUserStats userStats = new LoadUserStats();
        userStats.execute(id_user);
        UserChallengesHelper challengesHelper = new UserChallengesHelper();
        challengesHelper.execute(id_user);
        validateConnection();
    }

    public void newRoute(View v) {
        Intent i = new Intent(HomeActivity.this, TraceRouteActivity.class);
        startActivity(i);
    }

    public void searchRoute(View v) {
        Intent i = new Intent(HomeActivity.this, BuscarRutas.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        Intent i = new Intent(HomeActivity.this, RetosActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }
    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_home:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_profile:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent1 = new Intent(HomeActivity.this, ProfileUserActivity.class);
                                intent1.putExtra("username", userProfile);
                                intent1.putExtra("email", emailProfile);
                                intent1.putExtra("height", heightProfile);
                                intent1.putExtra("weight", weightProfile);
                                startActivity(intent1);
                                startActivity(intent1);
                                return true;
                            case R.id.item_navigation_drawer_routes:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent2 = new Intent(HomeActivity.this, UserRoutes.class);
                                startActivity(intent2);
                                return true;

                            case R.id.item_navigation_drawer_challenge:
                                menuItem.setChecked(true);
                                Intent intent = new Intent(HomeActivity.this, RetosActivity.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback:
                                menuItem.setChecked(true);
                                messageClose();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });
    }

    public void messageClose(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage(getString(R.string.title_message_close))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes_option), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeSession();
                    }
                }).setNegativeButton(getString(R.string.no_option), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.setTitle(getString(R.string.title_close));
        alert.show();
    }

    public void closeSession(){
        //if (dbHelper.isLogged()) {
        //dbHelper.loggout();
        //finish();
        //} else if (accessToken != null) {
        // LoginManager.getInstance().logOut();
        //finish();
        //} else {
        mGoogleApiClient.disconnect();
        if (!mGoogleApiClient.isConnected())
            finish();
        Intent i = new Intent(HomeActivity.this, LogActivity.class);
        startActivity(i);
        //}
    }

    @Override
    public void onConnected(Bundle bundle) {
        getProfileInformation();
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                User u = new User();
                u.setEmail(email);
                u.setName(currentPerson.getDisplayName());
                dbHelper.addUser(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            txtDistance.setText(distance + " Km");
            txtBestSpeed.setText(speed + " Km/h");
            txtTotalRoutes.setText(totalRoutes);
        }
    }

    class UserChallengesHelper extends AsyncTask<Integer, ArrayList, ArrayList> {

        ArrayList<Route> respRoutes;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                    JSONArray jsonProvinces = jsonObject.getJSONArray("Provinces");
                    ArrayList<Province> provinces = new ArrayList<>();
                    for (int k = 0; k < jsonProvinces.length(); k++) {
                        JSONObject province=jsonProvinces.getJSONObject(k);
                        String provinceName=province.getString("NameProvince");
                        JSONArray jsonTowns=province.getJSONArray("TownList");
                        ArrayList<Town> towns=new ArrayList<>();
                        for(int h=0;h<jsonTowns.length();h++){
                            JSONObject town=jsonTowns.getJSONObject(h);
                            String townName=town.getString("NameTown");
                            towns.add(new Town(townName));
                        }
                        provinces.add(new Province(provinceName,towns));
                    }
                    route.setProvinces(provinces);
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
                    route.setDifficultyLevel(jsonObject.getInt("DifficultyLevel"));
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
            View target = findViewById(R.id.action_badge);
            BadgeView badge = new BadgeView(HomeActivity.this, target);
            badge.setText(routes.size() + "");
            badge.show();
        }
    }

    public int validateConnection(){
        int pConnected;
        pConnected = NetworkUtil.getConnectivityStatus(this);
        if(pConnected == 0){
            dialogConnection();
        }
        return  pConnected;
    }
    //Method that show message dialog
    public void dialogConnection(){
        AlertDialog.Builder msgConn = new AlertDialog.Builder(this);
        msgConn.setTitle(getString(R.string.connection_title));
        msgConn.setMessage(getString(R.string.connection_msg));
        msgConn.setPositiveButton(getString(R.string.button_dismiss), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog alertDialog = msgConn.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


}
