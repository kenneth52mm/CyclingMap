package com.cyclingmap.orion.cyclingmap.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;

public class RutasActivity extends ActionBarActivity {

    TextView tvTiempo, tvDistan, tvVeloProm, tvKm, tvKmH;
    Chronometer chrono;
    Button btnRutaStar, btnRutasStop, btnMapShow;
    ImageView imageViewMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);

        tvTiempo = (TextView) findViewById(R.id.lblRutasTiempo);
        tvDistan = (TextView) findViewById(R.id.lblRutasDistancia);
        tvVeloProm = (TextView) findViewById(R.id.lblRutasVelProm);
        tvKm =  (TextView) findViewById(R.id.lblRtuasKm);
        tvKmH =  (TextView) findViewById(R.id.lblRutasKmH);
        chrono = (Chronometer) findViewById(R.id.chrono);
        btnRutaStar = (Button) findViewById(R.id.btnStarRutas);
        btnRutasStop = (Button) findViewById(R.id.btnRutasStop);

        //Estilos para fuente
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        tvTiempo.setTypeface(tf);
        tvDistan.setTypeface(tf);
        tvVeloProm.setTypeface(tf);
        tvKm.setTypeface(tf);
        tvKmH.setTypeface(tf);
        chrono.setTypeface(tf);
        btnRutaStar.setTypeface(tf);
        btnRutasStop.setTypeface(tf);

        //Accion del boton start
        btnRutaStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnRutaStar.setVisibility(View.INVISIBLE);
                btnRutasStop.setVisibility(View.VISIBLE);
            }
        });

        //Accion del boton stop
        btnRutasStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRutasStop.setVisibility(View.INVISIBLE);
                btnRutaStar.setVisibility(View.VISIBLE);
            }
        });


        //Zona del mapa
        btnMapShow = (Button) findViewById(R.id.btnShowMap);
        imageViewMap = (ImageView) findViewById(R.id.imgMap);
        btnMapShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewMap.setVisibility(View.VISIBLE);
            }
        });

        imageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewMap.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rutas, menu);
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
