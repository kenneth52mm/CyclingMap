package com.cyclingmap.orion.cyclingmap.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.NetworkUtil;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.User;
import com.cyclingmap.orion.cyclingmap.activities.HomeActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;

public class LogActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private boolean mIntentInProgress;
    private EditText txtUsername;
    private static final int RC_SIGN_IN = 0;
    private ConnectionResult mConnectionResult;
    private EditText txtPassword;
    private SignInButton signinButton;
    private GoogleApiClient mGoogleApiClient;
    private boolean signedInUser;
    private DBHelper dbHelper;
    private CallbackManager callbackManager;
    private ImageView imgProfilePic;
    private int id_user;
    private User u=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            dbHelper = new DBHelper(getApplicationContext());
            setContentView(R.layout.activity_log);
            txtUsername = (EditText) findViewById(R.id.TxtUsername);
            txtPassword = (EditText) findViewById(R.id.TxtPassword);
            signinButton = (SignInButton) findViewById(R.id.btnGP);
            imgProfilePic = (ImageView) findViewById(R.id.imgProfile);
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
            signinButton.setOnClickListener(this);
            signinButton.setSize(SignInButton.SIZE_STANDARD);

    }
    public void registerNew(View v) {
        Intent intent = new Intent(LogActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    public void validarUsuario(View v) {
        validateConnection();
        if(txtUsername.getText().toString().trim().length() > 0){

            if(txtPassword.getText().toString().trim().length() > 0){
                LoginWSHelper helper = new LoginWSHelper();
                helper.execute(txtUsername.getText().toString(), txtPassword.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.input_pass),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.input_username),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            //GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            return;
        }
        if (!mIntentInProgress) {
            // store mConnectionResult
            mConnectionResult = connectionResult;

            if (signedInUser) {
                resolveSignInError();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (responseCode == RESULT_OK) {
                    signedInUser = false;
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
        }

        callbackManager.onActivityResult(requestCode, responseCode, intent);
        if (responseCode == RESULT_OK) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnGP:
                googlePlusLogin();
                if (!signedInUser) {}
                break;
        }
    }
    public void signIn(View v) {
        googlePlusLogin();
    }
    private void googlePlusLogin() {
        if (!mGoogleApiClient.isConnecting()) {
            signedInUser = true;
            resolveSignInError();
        }
    }
    @Override
    public void onConnected(Bundle arg0) {
        signedInUser = false;
        if (!signedInUser) {
          //  getProfileInformation();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
               u=new User();
                u.setEmail(email);
                u.setName(currentPerson.getDisplayName());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class LoginWSHelper extends AsyncTask<String, Integer, Integer> {

        private int valor = 0;

        public int getValor()
        {
            return valor;
        }
        public void setValor(int valor)
        {
            this.valor = valor;
        }
        private final ProgressDialog dialog = new ProgressDialog(LogActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getString(R.string.loading_dialog));
            dialog.show();
        }
        @Override
        protected Integer doInBackground(String... params) {
            //android.os.Debug.waitForDebugger();
            int resul = 0;
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://orion-group.azurewebsites.net/Api/Login/" + params[0] + "/" + params[1]);
            httpGet.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = client.execute(httpGet);
                String respStr = EntityUtils.toString(resp.getEntity());
                resul = Integer.parseInt(respStr);
                Log.i("Existe", " " + resul);
            } catch (Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                resul = 0;
            }
            return resul;
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer != 0) {
                id_user=integer;
                int resp=dbHelper.loggin();
                dbHelper.addId(id_user);
                Intent intent = new Intent(LogActivity.this, HomeActivity.class);
                intent.putExtra("id_user",id_user);
                startActivity(intent);
                finish();
            }
            dialog.dismiss();
        }
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
