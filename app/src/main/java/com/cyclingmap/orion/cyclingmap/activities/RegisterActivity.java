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
import com.cyclingmap.orion.cyclingmap.business.Encript;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends ActionBarActivity {

    Button btnReg;
    EditText txtName, txtEmail, txtPass, txtConfirnPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtName = (EditText) findViewById(R.id.txtuserna);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtpass);
        //txtConfirnPass = (EditText) findViewById(R.id.editText4);
        btnReg = (Button) findViewById(R.id.btnRegister);

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
                //validaciones
                if(txtName.getText().toString().trim().length() > 0)//nombre
                {
                    if(isEmailValid(txtEmail.getText().toString()))//email
                    {
//                        if(txtPass.getText().toString().equals(txtConfirnPass.getText().toString()))//claves
//                        {
                            Encript encript =new Encript();
                            registerPerson(txtEmail.getText().toString(), txtName.getText().toString(), encript.encryptAndEncode(txtPass.getText().toString()));
                            //resetear los texts
                            txtName.setText("");
                            txtEmail.setText("");
                            txtPass.setText("");
                            txtConfirnPass.setText("");
//                        }
//                        else
//                        {
//                            Toast.makeText(getApplicationContext(), "Datos de Contrasena incorrectos",
//                                    Toast.LENGTH_SHORT).show();
//                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Correo invalido",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "El nombre esta vacio",
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

            PersonWSHelper personWsHelper = new PersonWSHelper();
            personWsHelper.setPerson(person);
            personWsHelper.execute();
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
