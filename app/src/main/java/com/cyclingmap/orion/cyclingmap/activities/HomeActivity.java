package com.cyclingmap.orion.cyclingmap.activities;

import android.content.Entity;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.utils.Dialog_Notification;
import com.cyclingmap.orion.cyclingmap.utils.addBadge;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Iterator;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBar actionBar;
    private TextView txtDistance;
    private TextView txtBestSpeed;
    private TextView txtTotalRoutes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtDistance = (TextView) findViewById(R.id.dist);
        txtBestSpeed = (TextView) findViewById(R.id.speed);
        txtTotalRoutes = (TextView) findViewById(R.id.textView2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        setupNavigationDrawerContent(navigationView);
        LoadUserStats userStats=new LoadUserStats();
        userStats.execute(13);
    }

    public void newRoute(View v) {
        Intent i = new Intent(HomeActivity.this, RutasActivity.class);
        startActivity(i);
    }

    public void searchRoute(View v) {
        Intent i = new Intent(HomeActivity.this, BuscarRutas.class);
        startActivity(i);
    }

    //Opciones del toolbar para mostrar el badge
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_badge);

        // Obtener drawable del item
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Agregando el contador de notificaciones
        //
        addBadge.setBadgeCount(this, icon, 20);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        //muestra las notificaciones en un dialog
        Dialog_Notification notification = new Dialog_Notification();

        String[] noti = {"Notificacion 1", "Notificacion 2", "Notificacion 3"};

        notification.setNotification(noti);
        notification.show(getFragmentManager(), "notification");
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
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_settings:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                                startActivity(intent);
                                return true;

                            // Cerrar sesion
                            //   case R.id.item_navigation_drawer_help_and_feedback:
                            //       menuItem.setChecked(true);
                            //       Toast.makeText(HomeActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                            //       drawerLayout.closeDrawer(GravityCompat.START);
                            //       return true;
                        }
                        return true;
                    }
                });
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
            txtDistance.setText(txtDistance.getText() + " " + distance);
            txtBestSpeed.setText(txtBestSpeed.getText() + " " + speed);
            txtTotalRoutes.setText(txtTotalRoutes.getText()+" "+totalRoutes);
        }
    }
}
