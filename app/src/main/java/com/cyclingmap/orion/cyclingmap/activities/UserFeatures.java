package com.cyclingmap.orion.cyclingmap.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.cyclingmap.orion.cyclingmap.model.User;

import java.util.ArrayList;


public class UserFeatures extends ActionBarActivity {
    TextView lblHead;
    EditText txtHeight, txtWeight;
    Spinner spinLevel;
    Button btnSave;
   // String[] arrayLevel = {"Principiante", "Intermedio", "Avanzado"};
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] arrayLevel = {getString(R.string.beginner_level), getString(R.string.intermediate_level), getString(R.string.advanced_level)};
        setContentView(R.layout.activity_user_features);
        lblHead = (TextView)findViewById(R.id.tvFeaturesHead);
        txtHeight = (EditText)findViewById(R.id.txtFeaturesHeight);
        txtWeight = (EditText)findViewById(R.id.txtFeaturesWeight);
        spinLevel = (Spinner)findViewById(R.id.spinUserFeatures);
        btnSave = (Button)findViewById(R.id.btnUserFeaturesSave);

        //Se agrega el estilo de la fuente
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        lblHead.setTypeface(tf);
        txtHeight.setTypeface(tf);
        txtWeight.setTypeface(tf);
        btnSave.setTypeface(tf);

        final ArrayAdapter adapterLevel = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayLevel );
        spinLevel.setAdapter(adapterLevel);

        //Registro de datos
        db = new DBHelper(getApplicationContext());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle b = new Bundle();
                //int idUser= b.getInt("id_user");
                //idUser=1;
                String level = spinLevel.getSelectedItem().toString();
                ArrayList<Route> arrayList = new ArrayList<Route>();
                User u = new User(1, "prueba", "prueba","prueba","prueba",0,0,0,arrayList);
                db.addUser(u);
                db.addUserFeatures(u.getId(), Integer.parseInt(txtWeight.getText().toString()), Integer.parseInt(txtHeight.getText().toString()), level);
            }
        });

    }

    private AdapterView.OnItemSelectedListener OnItemSpinner = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                   long id) {
            ((TextView) parent.getChildAt(0)).setTextSize(18);
        }
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
