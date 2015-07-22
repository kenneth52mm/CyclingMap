package com.cyclingmap.orion.cyclingmap.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;

import java.util.ArrayList;


public class BuscarRutas extends ActionBarActivity {

    Spinner spPopularidad, spTipoCalle, spNivel;
    ListView lvRutasBuscadas;
    Button btnRutasBusca;
    TextView tvTexto;
    LinearLayout contedorListaBus;
    TableLayout tablaBusca;

    String[] arrayPopu = {"Alta", "Media", "Baja"};
    String[] arraTipoCalle = {"Lastre", "Asfalto", "Combinado"};
    String[] arraNivel = {"Principiante", "Intermedio", "Avanzado"};
    ArrayList arraRutasBusc = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_rutas);

        spPopularidad = (Spinner) findViewById(R.id.spinnerPopu);
        spTipoCalle = (Spinner) findViewById(R.id.spinnerTipCalle);
        spNivel = (Spinner) findViewById(R.id.spinnerNivel);
        lvRutasBuscadas = (ListView) findViewById(R.id.lvRutasEncontradas);
        btnRutasBusca = (Button) findViewById(R.id.btnRutas_Buscar);
        tvTexto = (TextView) findViewById(R.id.txtRut_Buscar_Enca);
        contedorListaBus = (LinearLayout) findViewById(R.id.layoutContListaBus);
        tablaBusca = (TableLayout) findViewById(R.id.tabla1);

        //Spinner custom
        ArrayAdapter adapterPop = new ArrayAdapter<CharSequence>(this, R.layout.spin_style_item, arrayPopu );
        adapterPop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPopularidad.setAdapter(adapterPop);

        ArrayAdapter adapterTipoC = new ArrayAdapter<String>(this, R.layout.spin_style_item, arraTipoCalle );
        adapterTipoC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoCalle.setAdapter(adapterTipoC);

        ArrayAdapter adapterNi = new ArrayAdapter<String>(this, R.layout.spin_style_item, arraNivel );
        adapterNi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNivel.setAdapter(adapterNi);


        final ArrayAdapter rutasBuscado = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraRutasBusc );

        lvRutasBuscadas.setAdapter(rutasBuscado);

        //Agrega la fuente a los controles
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        btnRutasBusca.setTypeface(tf);
        tvTexto.setTypeface(tf);


        //Click btnBuscar
        btnRutasBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rutasBuscado.clear();

                arraRutasBusc.add(spPopularidad.getSelectedItem());
                arraRutasBusc.add(spTipoCalle.getSelectedItem());
                arraRutasBusc.add(spNivel.getSelectedItem());

                lvRutasBuscadas.setAdapter(rutasBuscado);

                contedorListaBus.setVisibility(View.VISIBLE);
                lvRutasBuscadas.setVisibility(View.VISIBLE);

                Intent i = new Intent(getApplicationContext(), DetallesRuta.class);
                startActivity(i);
            }
        });
    }




}
