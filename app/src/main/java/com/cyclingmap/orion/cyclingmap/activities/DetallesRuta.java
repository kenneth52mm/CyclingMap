package com.cyclingmap.orion.cyclingmap.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;


public class DetallesRuta extends ActionBarActivity {

    TextView lblEncabez, lblNombre,lblDistancia, lblVelMedia, lblNivel, lblTipCalle;
    Button btnPlay;
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
    }


}
