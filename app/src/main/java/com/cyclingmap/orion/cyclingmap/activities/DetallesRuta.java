package com.cyclingmap.orion.cyclingmap.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.UserRoutesAdapter;
import com.cyclingmap.orion.cyclingmap.model.Route;

import java.util.ArrayList;


public class DetallesRuta extends ActionBarActivity {

    TextView lblEncabez, lblNombre,lblDistancia, lblVelMedia, lblNivel, lblTipCalle;
    Button btnPlay;

    UserRoutesAdapter routesAdapter;
    ArrayList<Route> routes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ruta);

        lblEncabez = (TextView) findViewById(R.id.txt_Ruta_Detalle_Encabez);
        lblNombre = (TextView) findViewById(R.id.txtRuta_Det_Nombre);
        lblDistancia = (TextView) findViewById(R.id.txtRuta_Det_Distancia);
        lblVelMedia = (TextView) findViewById(R.id.txtRutas_Det_Vel_Media);
        lblNivel = (TextView) findViewById(R.id.txtRuta_Det_Nivel);
        lblTipCalle = (TextView) findViewById(R.id.txtRuta_Det_TipCalle);
        btnPlay = (Button)findViewById(R.id.btnRuta_Det_Play);

        //Se agrega el estilo de la fuente
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        lblEncabez.setTypeface(tf);
        lblNombre.setTypeface(tf);
        lblDistancia.setTypeface(tf);
        lblVelMedia.setTypeface(tf);
        lblNivel.setTypeface(tf);
        lblTipCalle.setTypeface(tf);
        btnPlay.setTypeface(tf);

        //Mostrar los datos
        Bundle b = getIntent().getExtras();

        //controla que haya datos si no se muestra nada
        if(b != null) {
            String distance = b.getString("Distance");
            String velMedia = b.getString("AvgSpeed");
            String nivel = b.getString("Medio");

            lblDistancia.setText(lblDistancia.getText().toString() + distance + "");
            lblVelMedia.setText(lblVelMedia.getText().toString() + velMedia + "");
            lblNivel.setText(lblNivel.getText() + nivel + "");
            lblNombre.setText(lblNombre.getText() + "Ruta 1");
            lblTipCalle.setText(lblTipCalle.getText() + "Combinado");
        }
        else
        {
            lblDistancia.setText(lblDistancia.getText().toString() + " 0 Km");
            lblVelMedia.setText(lblVelMedia.getText().toString()+ " 0 km/h");
            lblNivel.setText(lblNivel.getText() +" 0");
            lblNombre.setText(lblNombre.getText() + " 0");
            lblTipCalle.setText(lblTipCalle.getText() + " Calle");

        }

    }

}
