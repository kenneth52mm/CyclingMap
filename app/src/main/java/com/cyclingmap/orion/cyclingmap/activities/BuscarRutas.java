package com.cyclingmap.orion.cyclingmap.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Route;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class BuscarRutas extends ActionBarActivity {

    Spinner spinner_level;
    ListView lvRutasBuscadas;
    Button btnRutasBusca;
    TextView txtDistance;
    TextView txtTexto;
    LinearLayout contedorListaBus;
    TableLayout tablaBusca;

    int difficuLevel = 0 ;

    String town= "";

    String province = "";

    String distance = "";

    String[] arraNivel = {"Principiante", "Intermedio", "Avanzado"};

    ArrayList<Route> routes = new ArrayList<>();

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_rutas);

        spinner_level = (Spinner) findViewById(R.id.spinnerLevel);

        ArrayAdapter adapterNi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraNivel );
        adapterNi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_level.setAdapter(adapterNi);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        btnRutasBusca.setTypeface(typeface);
        txtTexto.setTypeface(typeface);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        province =  LocationManager.GPS_PROVIDER;

        final SearhByCriteriaWSHelper wsHelper = new SearhByCriteriaWSHelper();

        btnRutasBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficuLevel =  difficultyLevel(spinner_level.getSelectedItem().toString());
                distance = txtDistance.getText().toString();
                wsHelper.execute();
            }
        });
    }
    class SearhByCriteriaWSHelper extends AsyncTask<String,  ArrayList, ArrayList> {
        private final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Cargando...");
            dialog.show();
        }
        @Override
        protected ArrayList doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            params[0] = difficuLevel+"";
            params[1] = town;
            params[2] = province;
            params[3] = distance;
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
        protected void onPostExecute(ArrayList array) {
            //super.onPostExecute(array);
            //lwRoutes = (ListView)findViewById(R.id.list);
            //routesAdapter = new UserRoutesAdapter(routes, getApplicationContext());
            //lwRoutes.setAdapter(routesAdapter);
            //dialog.dismiss();
            // routesAdapter.setRoutesList(routes);
            //routesAdapter.notifyDataSetChanged();
        }
    }
    private Integer difficultyLevel(String p)
    {
        int level = 0;
        switch (p)
        {
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

}
