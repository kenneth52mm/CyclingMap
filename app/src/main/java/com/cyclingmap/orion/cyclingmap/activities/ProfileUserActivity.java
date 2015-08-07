package com.cyclingmap.orion.cyclingmap.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileUserActivity extends AppCompatActivity {

    Toolbar toolbar;

    private TextView userProfile;
    private TextView emailProfile;
    private TextView heightProfile;
    private TextView weightProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TypedValue typedValueColorPrimaryDark = new TypedValue();
        ProfileUserActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        userProfile = (TextView) findViewById(R.id.txtUsernameProfile);
        emailProfile = (TextView) findViewById(R.id.txtEmailProfile);
        heightProfile = (TextView) findViewById(R.id.txtHeight);
        weightProfile = (TextView) findViewById(R.id.txtWeight);

      //  User u = new User();
      //  int idUser = u.getId();
        userProfileDetails userInfo = new userProfileDetails();
        userInfo.execute(1);
    }

    class userProfileDetails extends AsyncTask<Integer, String, String>{

        String username;
        String email;
        String height;
        String weight;

        private final ProgressDialog dialog = new ProgressDialog(ProfileUserActivity.this);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog.setMessage(getString(R.string.loading_dialog));
            dialog.show();
        }

        @Override
        protected String doInBackground(Integer... params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://localhost:31408/Api/profile/" + params[0]);
            httpGet.setHeader("content-type", "application/json");
            try
            {
                HttpResponse response = client.execute(httpGet);
                JSONArray jsonArray = new JSONArray(EntityUtils.toString(response.getEntity()));
                username = jsonArray.getString(0);
                email = jsonArray.getString(1);
                height = jsonArray.getString(2);
                weight = jsonArray.getString(3);
            }
            catch (Exception ex)
            {
                Log.i("Error", "" + ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            userProfile.setText(username);
            emailProfile.setText(email);
            heightProfile.setText(height);
            weightProfile.setText(weight);
            dialog.dismiss();
        }
    }

}
