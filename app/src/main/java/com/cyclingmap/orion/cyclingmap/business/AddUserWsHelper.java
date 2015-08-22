package com.cyclingmap.orion.cyclingmap.business;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.cyclingmap.orion.cyclingmap.activities.HomeActivity;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 * Created by kenneth on 20/08/2015.
 */
public class AddUserWsHelper extends AsyncTask<User, User, Integer> {

    private Context context;
    private int id_user;
    private DBHelper dbHelper;
    private User u = null;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(User... params) {
        int resp = 0;
        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost postPerson = new HttpPost("http://orion-group.azurewebsites.net/Api/user/add");
            postPerson.setHeader("content-type", "application/json");
            JSONObject object = new JSONObject();
            object.put("User", params[0]);
            StringEntity objetoString = new StringEntity(object.toString());
            postPerson.setEntity(objetoString);

            HttpResponse response = client.execute(postPerson);
            resp = Integer.parseInt(response.toString());
            id_user = resp;
            u = params[0];
            Log.i("Resultado=", " " + response);

        } catch (Exception ex) {
            Log.e("Error en registar: ", ex.getMessage());

        }
        return resp;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        id_user = integer;
        if (id_user > 0) {
            u.setId(id_user);
            dbHelper.addUser(u);
            Intent i = new Intent(getContext(), HomeActivity.class);
            context.startActivity(i);
        }
    }
}
