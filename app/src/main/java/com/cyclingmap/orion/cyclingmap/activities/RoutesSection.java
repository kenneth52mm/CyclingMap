package com.cyclingmap.orion.cyclingmap.activities;

import android.app.Activity;
import android.app.ProgressDialog;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;


/**
 * Created by Daniel on 03/07/2015.
 */
public class RoutesSection extends Fragment {


    ListView lwRoutes;
    UserRoutesAdapter routesAdapter;
    ArrayList<Route> routes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_route_section, container, false);
//        lwRoutes = (ListView) rootView.findViewById(R.id.list);
//        routesAdapter = new UserRoutesAdapter(new ArrayList<Route>(), rootView.getContext());
//        lwRoutes.setAdapter(routesAdapter);
        UserWSHelper helper = new UserWSHelper();
        helper.execute(13);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        private final ProgressDialog dialog = new ProgressDialog(getActivity());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Cargando...");
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
            lwRoutes = (ListView) getActivity().findViewById(R.id.list);
            routesAdapter = new UserRoutesAdapter(routes, getActivity());
            lwRoutes.setAdapter(routesAdapter);
            dialog.dismiss();
            // routesAdapter.setRoutesList(routes);
            routesAdapter.notifyDataSetChanged();
        }
    }
}
