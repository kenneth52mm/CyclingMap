package com.cyclingmap.orion.cyclingmap.activities;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.PersonWSHelper;
import com.cyclingmap.orion.cyclingmap.model.Login;
import com.cyclingmap.orion.cyclingmap.model.Persona;


public class RegisterActivity extends ActionBarActivity {

    Button btnReg;
    EditText txtName, txtEmail, txtPass, txtConfirnPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtName = (EditText) findViewById(R.id.editText);
        txtEmail = (EditText) findViewById(R.id.editText2);
        txtPass = (EditText) findViewById(R.id.editText3);
        txtConfirnPass = (EditText) findViewById(R.id.editText4);
        btnReg = (Button) findViewById(R.id.BtnRegister);

        //Estilos para fuente
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        txtName.setTypeface(tf);
        txtEmail.setTypeface(tf);
        txtPass.setTypeface(tf);
        txtConfirnPass.setTypeface(tf);
        btnReg.setTypeface(tf);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrarPersona(txtEmail.getText().toString(), txtName.getText().toString(), txtPass.getText().toString());
            }
        });
    }

    //Registrar
    public void registrarPersona(String email, String name, String pass){
        try
        {
            Persona person = new Persona();
            person.setName(name);
            Login login = new Login(email,pass);
            person.setLogin(login);
            person.setTotalDistance(0.0);
            //person.setBestTime(null);

            PersonWSHelper personWsHelper = new PersonWSHelper();
            personWsHelper.setPersona(person);
            personWsHelper.execute();

            Toast.makeText(getApplicationContext(), "Punto register", Toast.LENGTH_LONG).show();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
}
