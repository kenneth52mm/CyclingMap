package com.cyclingmap.orion.cyclingmap.activities;

import android.app.ProgressDialog;
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


        Bundle bundle = getIntent().getExtras();
        String uName = bundle.getString("username");
        String uEmail = bundle.getString("email");
        String uHeight = bundle.getString("height");
        String uWeight = bundle.getString("weight");

        userProfile.setText(uName);
        emailProfile.setText(uEmail);
        heightProfile.setText(uHeight);
        weightProfile.setText(uWeight);
        // userProfileDetails userInfo = new userProfileDetails();
        // userInfo.execute(1);
    }

    public void profileConfig(View v){
        Intent intent = new Intent(ProfileUserActivity.this, UserFeatures.class);
        startActivity(intent);
    }
}