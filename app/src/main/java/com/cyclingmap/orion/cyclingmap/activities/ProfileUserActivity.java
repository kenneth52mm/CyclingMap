package com.cyclingmap.orion.cyclingmap.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.NetworkUtil;
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
    private DBHelper dbHelper;


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
        dbHelper = new DBHelper(ProfileUserActivity.this);
        userProfile = (TextView) findViewById(R.id.txtUsernameProfile);
        emailProfile = (TextView) findViewById(R.id.txtEmailProfile);
        heightProfile = (TextView) findViewById(R.id.txtHeight);
        weightProfile = (TextView) findViewById(R.id.txtWeight);
        int id_user = dbHelper.getIdUser();
        userProfileDetails userDetails = new userProfileDetails();
        userDetails.execute(id_user);
      // String name = u.getName();
      // String mail= u.getEmail();

     //   Bundle bundle = getIntent().getExtras();
     //   String uName = bundle.getString("username");
     //   String uEmail = bundle.getString("email");
     //   String uHeight = bundle.getString("height");
     //   String uWeight = bundle.getString("weight");

      //  userProfile.setText(uName);
       // emailProfile.setText(uEmail);
     //   heightProfile.setText(uHeight);//
      //  weightProfile.setText(uWeight);
    }

    class userProfileDetails extends AsyncTask<Integer, String, String> {
        String username;
        String email;
        String height;
        String weight;
        private final ProgressDialog dialog = new ProgressDialog(ProfileUserActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getString(R.string.loading_dialog));
            dialog.show();
        }

        @Override
        protected String doInBackground(Integer... params) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://orion-group.azurewebsites.net/Api/user/profile/" + params[0]);
            httpGet.setHeader("content-type", "application/json");
            Log.i("Load", "Load Profile");
            try {
                HttpResponse response = client.execute(httpGet);
                JSONArray jsonArray = new JSONArray(EntityUtils.toString(response.getEntity()));
                username = jsonArray.getString(0);
                email = jsonArray.getString(1);
                height = jsonArray.getString(2);
                weight = jsonArray.getString(3);
            } catch (Exception ex) {
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

    public void profileConfig(View v){
        validateConnection();
        Intent intent = new Intent(ProfileUserActivity.this, UserFeatures.class);
        startActivity(intent);
    }

    public int validateConnection(){
        int pConnected;
        pConnected = NetworkUtil.getConnectivityStatus(this);
        if(pConnected == 0){
            dialogConnection();
        }
        return  pConnected;
    }
    //Method that show message dialog
    public void dialogConnection(){
        AlertDialog.Builder msgConn = new AlertDialog.Builder(this);
        msgConn.setTitle(getString(R.string.connection_title));
        msgConn.setMessage(getString(R.string.connection_msg));
        msgConn.setPositiveButton(getString(R.string.button_dismiss), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog alertDialog = msgConn.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}