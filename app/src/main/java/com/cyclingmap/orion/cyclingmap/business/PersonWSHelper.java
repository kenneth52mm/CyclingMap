package com.cyclingmap.orion.cyclingmap.business;

import android.os.AsyncTask;
import android.util.Log;

import com.cyclingmap.orion.cyclingmap.model.Persona;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 * Created by peter on 6/30/2015.
 */
public class PersonWSHelper extends AsyncTask <Persona, String, Integer> {

    public Persona person;
    public Persona getPerson() {
        return person;
    }
    public void setPerson(Persona p) {
        this.person = p;
    }


    @Override
    protected Integer doInBackground(Persona... params) {
        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost postPerson = new HttpPost("http://orion-group.azurewebsites.net/Api/registerPerson");
            postPerson.setHeader("content-type", "application/json");

            JSONObject object=new JSONObject();
            object.put("Persona", getPerson());
            StringEntity objetoString=new StringEntity(object.toString());
            postPerson.setEntity(objetoString);

            HttpResponse response=client.execute(postPerson);

            Log.i("Resultado=", " " + response );

        } catch (Exception ex) {
            Log.e("Error en registar: ", ex.getMessage());

        }
        return null;
    }


}

