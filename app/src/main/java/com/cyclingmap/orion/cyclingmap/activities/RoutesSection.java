package com.cyclingmap.orion.cyclingmap.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.UserRoutesAdapter;
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
import java.util.ArrayList;
import java.util.List;


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
        lwRoutes = (ListView) rootView.findViewById(R.id.list);
        routes.add(new Route(10.0, 53.41));
        routesAdapter = new UserRoutesAdapter(routes, getActivity().getApplicationContext());
        lwRoutes.setAdapter(routesAdapter);
        return rootView;
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


    class UserWSHelper extends AsyncTask<Integer, Integer, Integer> {

        ArrayList<Route> respRoutes;

        @Override
        protected Integer doInBackground(Integer... params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://orion-group.azurewbsites.net/Api/user/routes/");
            httpGet.setHeader("content-type", "application/json");
            try {
                HttpResponse response = client.execute(httpGet);
                JSONArray jsonArray = new JSONArray(EntityUtils.toString(response.getEntity()));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                }
            } catch (Exception ex) {

            } finally {
                routes = respRoutes;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            lwRoutes.setAdapter(routesAdapter);
        }
    }
}
