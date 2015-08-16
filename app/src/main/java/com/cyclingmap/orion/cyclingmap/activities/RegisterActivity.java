package com.cyclingmap.orion.cyclingmap.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.PersonWSHelper;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.Login;
import com.cyclingmap.orion.cyclingmap.model.Persona;
import com.cyclingmap.orion.cyclingmap.business.Encript;
import com.cyclingmap.orion.cyclingmap.model.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends ActionBarActivity {

    Button btnReg;
    EditText txtName, txtEmail, txtPass, txtConfirnPass;
    private DBHelper dbHelper;
    int id_user;
//    private final ProgressDialog dialog = new ProgressDialog(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtName = (EditText) findViewById(R.id.txtuserna);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtpass);
        //txtConfirnPass = (EditText) findViewById(R.id.editText4);
        btnReg = (Button) findViewById(R.id.btnRegister);
        dbHelper = new DBHelper(RegisterActivity.this);
        //Estilos para fuente
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        txtName.setTypeface(tf);
        txtEmail.setTypeface(tf);
        txtPass.setTypeface(tf);
//        txtConfirnPass.setTypeface(tf);
        btnReg.setTypeface(tf);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validaciones
                if(isEmailValid(txtEmail.getText().toString()))//email
                {
                    if(txtName.getText().toString().trim().length() > 0)//nombre
                    {
                        if(txtPass.getText().toString().trim().length() > 0)//clave
                        {
                            Encript encript =new Encript();
                            registerPerson(txtEmail.getText().toString(), txtName.getText().toString(), (txtPass.getText().toString()));
                            //resetear los texts
                            txtName.setText("");
                            txtEmail.setText("");
                            txtPass.setText("");
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), getString(R.string.input_pass),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.input_username),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_email),
                        Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void registerPerson(String email, String name, String pass){
        try
        {
            Persona person = new Persona();
            person.setName(name);
            Login login = new Login(email,pass);
            person.setLogin(login);
            person.setTotalDistance(0.0);
            Date date = new Date(0);
            person.setBestTime(date);

//            PersonWSHelper personWsHelper = new PersonWSHelper();
//            personWsHelper.setPerson(person);
//            personWsHelper.execute();

            User u=new User();
            u.setName(name);
            u.setEmail(email);
            u.setPassword(pass);
            dbHelper.addUser(u);
            RegisterHelper helper=new RegisterHelper();
            helper.execute(u);
            //Progress bar
        //    dialog.setMessage("Registrando...");
        //    dialog.show();
        }
        catch (Exception e )
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error en registrar");
            builder.setMessage(e.toString());
            builder.setPositiveButton("OK", null);
            builder.create();
            builder.show();
        }
    }

    //Validar el correo
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    class RegisterHelper extends AsyncTask<User,User,Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(User... params) {
            int resp=0;
            try {

                HttpClient client = new DefaultHttpClient();
                HttpPost postPerson = new HttpPost("http://orion-group.azurewebsites.net/Api/user/add");
                postPerson.setHeader("content-type", "application/json");
                JSONObject object=new JSONObject();
                object.put("User", params[0]);
                StringEntity objetoString=new StringEntity(object.toString());
                postPerson.setEntity(objetoString);

                HttpResponse response=client.execute(postPerson);
                resp= Integer.parseInt(response.toString());
                id_user=resp;
                Log.i("Resultado=", " " + response);

            } catch (Exception ex) {
                Log.e("Error en registar: ", ex.getMessage());

            }
            return resp;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            id_user=integer;
            if(id_user>0){
                Intent i=new Intent(RegisterActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}
