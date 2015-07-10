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
public class PersonWSHelper extends AsyncTask<String, String, Integer> {

    public Persona persona;
    public Persona getPersona() {
        return persona;
    }
    public void setPersona(Persona p) {
        this.persona = p;
    }

    @Override
    protected Integer doInBackground(String... params) {
        try {

            /*Persona p = new Persona();
            p.setName("pruebaregAndroid");
            Login l= new Login();
            l.setEmail("emaildesandroid@");
            l.setPassword("123456");
            p.setLogin(l);
            //Date tiempo = new Date();
            //SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
            //formato.format(tiempo);
            p.setTotalDistance(12.0);*/

            HttpClient client = new DefaultHttpClient();
            HttpPost postPerson = new HttpPost("http://orion-group.azurewebsites.net/Ap/registerPersona");
            postPerson.setHeader("content-type", "application/json");

            JSONObject object=new JSONObject();
            object.put("Persona", getPersona());
            StringEntity objetoString=new StringEntity(object.toString());
            postPerson.setEntity(objetoString);

            HttpResponse response=client.execute(postPerson);



        } catch (Exception ex) {
            Log.e("Error en registar: ", ex.getMessage());
        }
        return null;
    }

}

