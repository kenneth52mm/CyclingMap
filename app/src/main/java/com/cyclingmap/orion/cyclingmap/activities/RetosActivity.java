package com.cyclingmap.orion.cyclingmap.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.NetworkUtil;
import com.cyclingmap.orion.cyclingmap.business.UserRoutesAdapter;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Route;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class RetosActivity extends AppCompatActivity {

    ListView lwRoutes;
    UserRoutesAdapter routesAdapter;
    ArrayList<Route> routes = new ArrayList<>();
    Toolbar toolbar;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retos);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DBHelper(RetosActivity.this);
        routes = (ArrayList<Route>) dbHelper.retrieveAllRoutes();
        lwRoutes = (ListView) findViewById(R.id.list);
        routesAdapter = new UserRoutesAdapter(routes, RetosActivity.this);
        lwRoutes.setAdapter(routesAdapter);
        UserChallengesHelper helper = new UserChallengesHelper();
        helper.execute(13);

        for (Route r : routes)
            dbHelper.addChallenges(r);
            lwRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Route route = routes.get(position);
                Intent intent = new Intent(RetosActivity.this, DetallesRuta.class);
                intent.putExtra("Distance", route.getDistance());
                intent.putExtra("Id_Route", route.getIdRoute());
                intent.putExtra("SpeedAveg", route.getAvgSpeed());
                intent.putExtra("Level", route.getDifficultyLevel());
                startActivity(intent);
                validateConnection();
            }
        });
    }

    class UserChallengesHelper extends AsyncTask<Integer, ArrayList, ArrayList> {

        ArrayList<Route> respRoutes;
        private final ProgressDialog dialog = new ProgressDialog(RetosActivity.this);

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
                    // route.setTimeToFin((Time) jsonObject.get("TimeToFin"));
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
