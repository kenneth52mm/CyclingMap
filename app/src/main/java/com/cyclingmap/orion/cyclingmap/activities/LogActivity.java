package com.cyclingmap.orion.cyclingmap.activities;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.User;
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

public class LogActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

   // private LoginWSHelper helper;
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
    private DBHelper dbHelper;
    private Button btnRegister;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ImageView buttonLogin;

    private TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        dbHelper = new DBHelper(getApplicationContext());
        setContentView(R.layout.activity_log);
        //txtMessage = (TextView) findViewById(R.id.TxtMessage);
        txtUsername = (EditText) findViewById(R.id.TxtUsername);
        txtPassword = (EditText) findViewById(R.id.TxtPassword);
        signinButton = (SignInButton) findViewById(R.id.btnGP);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
        signinButton.setOnClickListener(this);
        signinButton.setSize(SignInButton.SIZE_STANDARD);
        buttonLogin = (ImageView) findViewById(R.id.login);
        loginButton = (LoginButton) findViewById(R.id.login_Button);
        register = (TextView) findViewById(R.id.register);
    }

    public void registerNew(View v) {
        Intent intent = new Intent(LogActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            final User u =new User();
            u.setName(profile.getName());
            GraphRequest request=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    u.setEmail(jsonObject.optString("email"));
                }
            });
            dbHelper.addUser(u);
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
        }

        @Override
        public void onCancel() {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Intento cancelado", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onError(FacebookException e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Intento fallido", Toast.LENGTH_SHORT);
            toast.show();
        }
    };

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
        LoginWSHelper helper = new LoginWSHelper();
        helper.execute(txtUsername.getText().toString(), txtPassword.getText().toString());
        // int resp=helper.validate(txtUsername.getText().toString(), txtPassword.getText().toString());
//        txtMessage.setText("Existe: " + LoginWSHelper.valor);
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
//
                if (responseCode == RESULT_OK) {
                    signedInUser = false;
                    //intent.setClass(this, HomeActivity.class);
////                    Bundle bundle=new Bundle();
////                    bundle.putString("user",userName);
                    //  startActivity(intent);
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                    // startActivity(intent);
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
                if (!signedInUser) {
//                    intent.setClass(this, HomeActivity.class);
//////                    Bundle bundle=new Bundle();
//////                    bundle.putString("user",userName);
//                    startActivity(intent);

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
        // Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();

        if (!signedInUser) {
            getProfileInformation();
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
                User u=new User();
                u.setEmail(email);
                u.setName(currentPerson.getDisplayName());
                dbHelper.addUser(u);
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

        private final ProgressDialog dialog = new ProgressDialog(LogActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Cargando...");
            dialog.show();
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
                Intent intent = new Intent(LogActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            dialog.dismiss();
        }
    }
}
