package com.cyclingmap.orion.cyclingmap.business;

import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;


import com.cyclingmap.orion.cyclingmap.model.Coordinate;

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
public class RouteWsHelper extends AsyncTask<ArrayList<Coordinate>, String, String> {


    @Override
    protected String doInBackground(ArrayList<Coordinate>... params) {
        String resp = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://orion-group.azurewebsites.net/Api/route/save/");
        post.setHeader("content-type", "application/json");
        try {
            JSONArray jsonArray = new JSONArray(params[0]);
            JSONObject object = new JSONObject();
            for (Coordinate coordinate : params[0]) {
                object.put("X", coordinate.getX());
                object.put("Y", coordinate.getY());
            }
            StringEntity entity = new StringEntity(jsonArray.toString());
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            resp = EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            Log.e("route ex", ex.getMessage());
        }
        return resp;
    }
}
