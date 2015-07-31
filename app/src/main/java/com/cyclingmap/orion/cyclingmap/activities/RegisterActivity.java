package com.cyclingmap.orion.cyclingmap.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.Login;
import com.cyclingmap.orion.cyclingmap.model.Persona;
import com.cyclingmap.orion.cyclingmap.business.Encript;
import com.cyclingmap.orion.cyclingmap.model.User;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends ActionBarActivity {

    Button btnReg;
    EditText txtName, txtEmail, txtPass, txtConfirnPass;
    private DBHelper dbHelper;
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
        dbHelper = new DBHelper(getApplicationContext());
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
                            Toast.makeText(getApplicationContext(), "Por favor ingrese contrasena",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Por favor ingrese nombre",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Correo invalido",
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
            User u=new User();
            u.setName(name);
            u.setEmail(email);
            u.setPassword(pass);
            dbHelper.addUser(u);
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
}
