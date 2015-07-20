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

    TextView lblEncabez, lblNombre,lblDistancia, lblVelMedia, lblNivel, lblTipCalle, txtNameRoad, txtDistanceRoad, txtAveSpeed, txtLevel, txtTipCalle;
    Button btnPlay;

    UserRoutesAdapter routesAdapter;
    ArrayList<Route> routes = new ArrayList<>();
    Route route;

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
        txtNameRoad = (TextView) findViewById(R.id.txtNameRoad);
        txtDistanceRoad = (TextView) findViewById(R.id.txtDistance);
        txtAveSpeed = (TextView) findViewById(R.id.txtSpeedAveg);
        txtLevel = (TextView) findViewById(R.id.txtNivelRoad);
        txtTipCalle = (TextView) findViewById(R.id.txtRoadType);
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
        txtNameRoad.setTypeface(tf);
        txtDistanceRoad.setTypeface(tf);
        txtAveSpeed.setTypeface(tf);
        txtLevel.setTypeface(tf);
        txtTipCalle.setTypeface(tf);

        //Mostrar los datos
        Bundle b = getIntent().getExtras();

        //controla que haya datos si no se muestra nada
        if(b != null) {
            //route =(Route) b.get("Route");

            String id = b.getInt("Id_Route")+"";
            String distance = b.getDouble("Distance")+"";
            String velMedia = b.getDouble("SpeedAveg")+"";
            String nivel = b.getString("Level");

            txtDistanceRoad.setText( distance + "");
            txtAveSpeed.setText( velMedia + "");
            txtLevel.setText( nivel + "");
            txtNameRoad.setText( "Ruta " + id);
            txtTipCalle.setText( "No aplica");
        }
        else
        {
            txtDistanceRoad.setText("0");
            txtAveSpeed.setText("");
            txtLevel.setText("");
            txtNameRoad.setText("");
            txtTipCalle.setText("");

        }

    }



}
