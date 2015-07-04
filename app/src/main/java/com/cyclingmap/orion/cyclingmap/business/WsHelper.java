package com.cyclingmap.orion.cyclingmap.business;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WsHelper extends AsyncTask<String, Integer, Boolean> {
    private String personas;
    private TextView txtMessage;

    public String getPersonas() {
        return personas;
    }

    public void setPersonas(String personas) {
        this.personas = personas;
    }


    @Override
    protected Boolean doInBackground(String... params) {
        boolean resul = true;
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://orion-group.azurewebsites.net/Api/Personas");
        httpGet.setHeader("content-type", "application/json");
        try {
            HttpResponse resp = client.execute(httpGet);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONObject obj = new JSONObject(respStr);

            int id = obj.getInt("id");
            String nombre = obj.getString("nombre");
            String clave = obj.getString("Clave");
            setPersonas("Id " + id + " Nombre " + nombre + " Clave " + clave);
            Log.i("Persona", "" + id + "-" + nombre + "-" + clave);

        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
            resul = false;
        }
        return resul;
    }

}
