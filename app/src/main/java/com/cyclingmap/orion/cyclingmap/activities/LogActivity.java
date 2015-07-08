package com.cyclingmap.orion.cyclingmap.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cyclingmap.orion.cyclingmap.R;
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

public class LogActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LoginWSHelper helper;
    private TextView txtMessage;
    private boolean mIntentInProgress;
    private EditText txtUsername;
    private static final int RC_SIGN_IN = 0;
    private ConnectionResult mConnectionResult;
    private EditText txtPassword;
    private SignInButton signinButton;
    private GoogleApiClient mGoogleApiClient;
    private boolean signedInUser;
    private String userName;

    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        //txtMessage = (TextView) findViewById(R.id.TxtMessage);
        txtUsername = (EditText) findViewById(R.id.TxtUsername);
        txtPassword = (EditText) findViewById(R.id.TxtPassword);
        signinButton = (SignInButton) findViewById(R.id.btnGP);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
        signinButton.setOnClickListener(this);
        signinButton.setSize(SignInButton.SIZE_ICON_ONLY);

        //btnRegister = (Button)findViewById(R.id.BtnRegister);

//        btnRegisr.tesetOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LogActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
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
        helper = new LoginWSHelper();
        helper.execute(txtUsername.getText().toString(), txtPassword.getText().toString());
        // int resp=helper.validate(txtUsername.getText().toString(), txtPassword.getText().toString());
//        txtMessage.setText("Existe: " + LoginWSHelper.valor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
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
//                intent.setClass(this, MainActivity.class);
                if (responseCode == RESULT_OK) {
                    signedInUser = false;
                    intent.setClass(this, PrincipalActivity.class);
////                    Bundle bundle=new Bundle();
////                    bundle.putString("user",userName);
                    startActivity(intent);
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.btnGP:
                googlePlusLogin();
                if(!signedInUser)
                {
                    intent.setClass(this, PrincipalActivity.class);
////                    Bundle bundle=new Bundle();
////                    bundle.putString("user",userName);
                    startActivity(intent);

                }
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
        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
//        if (!signedInUser) {
//            Intent intent = new Intent(this, PrincipalActivity.class);
//            startActivity(intent);
//        }
        getProfileInformation();
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
                userName = currentPerson.getDisplayName();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class LoginWSHelper extends AsyncTask<String, Integer, Integer> {

        private int valor = 0;

        public int getValor() {
            return valor;
        }

        public void setValor(int valor) {
            this.valor = valor;
        }

        @Override
        protected Integer doInBackground(String... params) {
            int resul = 0;
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://orion-group.azurewebsites.net/Api/Login/" + params[0] + "/" + params[1]);
            httpGet.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = client.execute(httpGet);
                String respStr = EntityUtils.toString(resp.getEntity());
                resul = Integer.parseInt(respStr);
                //txtMessage.setText("Resultado: " + resul);
                Log.i("Existe", " " + resul);

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = 0;
            }
            return resul;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer != 0) {
                Intent intent = new Intent(LogActivity.this, PrincipalActivity.class);
                startActivity(intent);
            }

        }
    }
}
