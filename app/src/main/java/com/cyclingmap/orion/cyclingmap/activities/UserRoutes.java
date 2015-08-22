package com.cyclingmap.orion.cyclingmap.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.NetworkUtil;
import com.cyclingmap.orion.cyclingmap.business.UserRoutesAdapter;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Province;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.cyclingmap.orion.cyclingmap.model.Town;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

public class UserRoutes extends AppCompatActivity {

    ListView lwRoutes;
    UserRoutesAdapter routesAdapter;
    ArrayList<Route> routes = new ArrayList<>();
    Toolbar toolbar;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_routes);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DBHelper(UserRoutes.this);
        lwRoutes = (ListView) findViewById(R.id.list);
        routesAdapter = new UserRoutesAdapter(new ArrayList<Route>(), UserRoutes.this);
        lwRoutes.setAdapter(routesAdapter);
        int id_user = dbHelper.getIdUser();
        UserWSHelper helper = new UserWSHelper();
        helper.execute(id_user);

        lwRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                validateConnection();
                Route route = routes.get(position);
                Intent intent = new Intent(UserRoutes.this, DetallesRuta.class);
            //    intent.putExtra("Province", route.getProvinces().get(0).toString());
            //    intent.putExtra("Town"), route.getTown());
                  intent.putExtra("Distance", route.getDistance());
            //    intent.putExtra("Duration"), route.getDuration());
                  intent.putExtra("Level", route.getDifficultyLevel());
                startActivity(intent);
            }
        });
    }

    class UserWSHelper extends AsyncTask<Integer, ArrayList, ArrayList> {

        ArrayList<Route> respRoutes;
        private final ProgressDialog dialog = new ProgressDialog(UserRoutes.this);


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
            HttpGet httpGet = new HttpGet("http://orion-group.azurewebsites.net/Api/user/routes/" + params[0]);
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
                    routes.add(route);
                }
            } catch (Exception ex) {
                Log.i("Error", "" + ex);
            }
            return routes;
        }

        @Override
        protected void onPostExecute(ArrayList array) {
            //super.onPostExecute(array);
            lwRoutes = (ListView) findViewById(R.id.list);
            routesAdapter = new UserRoutesAdapter(routes, getApplicationContext());
            lwRoutes.setAdapter(routesAdapter);
            dialog.dismiss();
            // routesAdapter.setRoutesList(routes);
            routesAdapter.notifyDataSetChanged();
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
