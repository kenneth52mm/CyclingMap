package com.cyclingmap.orion.cyclingmap.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.UserRoutesAdapter;
import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Province;
import com.cyclingmap.orion.cyclingmap.model.Route;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.cyclingmap.orion.cyclingmap.data.LocationAddress;
import com.google.android.gms.maps.model.LatLng;

public class BuscarRutas extends ActionBarActivity implements LocationListener {

    ListView lRoutes_Search;
    UserRoutesAdapter routesAdapter;
    LocationAddress locationAddress;
    Spinner spinner_level;
    ListView lvRutasBuscadas;
    Button btnRutasBusca;
    TextView txtDistance;
    RadioButton radioButton;
    int diffLevel = 0;
    String town = "";
    String province = "";
    String distance = "";
   // String[] arraNivel = {"Principiante", "Intermedio", "Avanzado"};
    ArrayList<Route> routes = new ArrayList<>();
    private LocationManager locationManager;
    private Location lc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_rutas);
        spinner_level = (Spinner) findViewById(R.id.spinnerLevel);
        txtDistance = (EditText)findViewById(R.id.txt_distance_search);
        radioButton = (RadioButton)findViewById(R.id.check_near);
        String[] arraNivel = {"Principiante", "Intermedio", "Avanzado"};

    //    lRoutes_Search = (ListView) findViewById(R.id.list);
    //    routesAdapter = new UserRoutesAdapter(new ArrayList<Route>(), BuscarRutas.this);
    //    lRoutes_Search.setAdapter(routesAdapter); // Error
        btnRutasBusca = (Button) findViewById(R.id.btnRutas_Buscar);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        lc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        spinner_level = (Spinner) findViewById(R.id.spinnerLevel);
        ArrayAdapter adapterNi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraNivel);
        adapterNi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_level.setAdapter(adapterNi);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // province = LocationAddress.getProvinceByName("San Jose");
        //LocationManager.GPS_PROVIDER;

        btnRutasBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
     public void routesSearch(View v)
     {
         final LatLng[] coords = new LatLng[2];
         coords[0] = new LatLng(lc.getLatitude(),lc.getLongitude()); //Error
         LocationAddress.getRouteInfo(coords, getApplicationContext(), new GeocoderHandler());
         diffLevel = difficultyLevel(spinner_level.getSelectedItem().toString());
         distance = txtDistance.getText().toString();
         String[] data = new String[4];
         data[0] = String.valueOf(diffLevel);
         data[1] = province;
         data[2] = town;
         data[3] = distance;
         final SearhByCriteriaWSHelper wsHelper = new SearhByCriteriaWSHelper();
         wsHelper.execute(data);

     }
    @Override
    public void onLocationChanged(Location location) {    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {    }
    @Override
    public void onProviderEnabled(String provider) {    }
    @Override
    public void onProviderDisabled(String provider) {    }

    class SearhByCriteriaWSHelper extends AsyncTask<String, ArrayList, ArrayList> {
        private final ProgressDialog dialog = new ProgressDialog(BuscarRutas.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Cargando...");
            dialog.show();
        }

        @Override
        protected ArrayList doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            //Api/route/search/{difficulty}/{town}/{province}/{distance}  parametros
            HttpGet httpGet = new HttpGet("http://orion-group.azurewebsites.net/Api/route/search/"
                    + params[0] + "/" + params[1] + "/" + params[2] + "/" + params[3]);
            httpGet.setHeader("content-type", "application/json");
            try {
                HttpResponse response = client.execute(httpGet);
                JSONArray jsonArray = new JSONArray(EntityUtils.toString(response.getEntity()));
                for (int i = 0; i < jsonArray.length(); i++) {
                    Route route = new Route();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
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
        protected void onPostExecute(ArrayList route) {
            lRoutes_Search = (ListView) findViewById(R.id.list);
            routesAdapter = new UserRoutesAdapter(routes, getApplicationContext());
            lRoutes_Search.setAdapter(routesAdapter);
            dialog.dismiss();
        }
    }

    private Integer difficultyLevel(String p) {
        int level = 0;
        switch (p) {
            case "Principiante":
                level = 1;
                break;
            case "Intermedio":
                level = 2;
                break;
            case "Avanzado":
                level = 3;
                break;
        }
        return level;
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            ArrayList datos;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    datos = (ArrayList<Province>) bundle.getSerializable("direccion");
                    Province p = (Province) datos.get(0);
                    province = p.getNameProvince();
                    town = p.getTownList().get(0).getNameTown();
                    break;
                default:
                    datos = null;
            }
        }
    }

}
