package com.cyclingmap.orion.cyclingmap.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.UserRoutesAdapter;
import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Route;

import org.apache.http.HttpException;
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
import java.util.List;


/**
 * Created by Daniel on 03/07/2015.
 */
public class RoutesSection extends Fragment {


    ListView lwRoutes;
    UserRoutesAdapter routesAdapter;
    ArrayList<Route> routes = new ArrayList<>();
    UserWSHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_route_section, container, false);
        lwRoutes = (ListView) rootView.findViewById(R.id.list);
        helper = new UserWSHelper();
        helper.execute(13);
        lwRoutes.setAdapter(routesAdapter);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void onRouteClick() {
        lwRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Route route = routes.get(position);
//                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
//                intent.putExtra("Route", (Serializable) route);
//                startActivity(intent);
            }
        });
    }


    class UserWSHelper extends AsyncTask<Integer, ArrayList, ArrayList> {

        ArrayList<Route> respRoutes;

        @Override
        protected ArrayList doInBackground(Integer... params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://orion-group.azurewbsites.net/Api/user/routes/" + params[0]);
            httpGet.setHeader("content-type", "application/json");
            try {
                HttpResponse response = client.execute(httpGet);
                JSONArray jsonArray = new JSONArray(EntityUtils.toString(response.getEntity()));
                for (int i = 0; i < jsonArray.length(); i++) {
                    Route route = new Route();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Log.i("entro", "" + i);
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
                    route.setTimeToFin((Time) jsonObject.get("TimeToFin"));
                    respRoutes.add(route);
                }
            } catch (Exception ex) {

            } finally {
                routes = respRoutes;
            }
            return respRoutes;
        }

        @Override
        protected void onPostExecute(ArrayList array) {
            super.onPostExecute(array);
            routesAdapter = new UserRoutesAdapter(array, getActivity().getApplicationContext());
            lwRoutes.setAdapter(routesAdapter);
        }
    }
}
