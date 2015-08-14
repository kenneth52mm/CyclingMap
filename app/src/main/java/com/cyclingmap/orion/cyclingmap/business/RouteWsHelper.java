package com.cyclingmap.orion.cyclingmap.business;

import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Province;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.cyclingmap.orion.cyclingmap.model.Town;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenneth on 23/06/2015.
 */
public class RouteWsHelper extends AsyncTask<Object, String, String> {


    @Override
    protected String doInBackground(Object... params) {
        Route route = (Route) params[0];
        String resp = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://orion-group.azurewebsites.net/Api/route/save/");
        post.setHeader("content-type", "application/json");
        try {
            JSONObject object = new JSONObject();
            object.put("distance", route.getDistance());
            object.put("timeToFin", route.getTimeToFin());
            object.put("avgSpeed", route.getAvgSpeed());
            object.put("difficultyLevel", route.getDifficultyLevel());

            JSONArray provinces = new JSONArray();
            for (Province p : route.getProvinces()) {
                JSONObject province = new JSONObject();
                province.put("nameProvince", p.getNameProvince());
                JSONArray towns = new JSONArray();
                for (Town t : p.getTownList()) {
                    JSONObject town = new JSONObject();
                    town.put("nameTown", t.getNameTown());
                    towns.put(town);
                }
                province.put("townList", towns);

            }
            object.put("provinces", provinces);
            JSONArray coords = new JSONArray();
            for (Coordinate c : route.getCoordinateList()) {
                JSONObject coord = new JSONObject();
                coord.put("X", c.getX());
                coord.put("Y", c.getY());
                coords.put(coord);
            }
            object.put("coordinateList", coords);
            object.put("id_user", params[1]);

            StringEntity entity = new StringEntity(object.toString());
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            resp = EntityUtils.toString(response.getEntity());

        } catch (Exception ex) {
            Log.e("route ex", ex.getMessage());
        }

        return resp;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}