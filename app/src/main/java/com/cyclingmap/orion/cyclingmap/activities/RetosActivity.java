package com.cyclingmap.orion.cyclingmap.activities;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.model.Route;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import com.cyclingmap.orion.cyclingmap.utils.ExpandableListAdapter;


public class RetosActivity extends Activity {

    ListView lvRetos;
    ArrayAdapter retosAdapter;
    String[] arrayRetos = {"Reto 1", "Reto 2", "Reto 3"};
    ArrayList<Route> routes = new ArrayList<>();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // lvRetos = (ListView) findViewById(R.id.lv_retos);

       //this.setListAdapter(new ArrayAdapter<String>(this, R.layout.item_retos_lv, R.id.txt_item_retos_lv, arrayRetos));

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                 Toast.makeText(getApplicationContext(),
                 "Group Clicked " + listDataHeader.get(groupPosition),
                 Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }

    /*
         * Preparing the list data
         */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Reto Chiripo");
        listDataHeader.add("Reto Jaco");
        listDataHeader.add("Reto U de la Paz");

        // Adding child data
        List<String> reto_chiripo = new ArrayList<String>();
        reto_chiripo.add("Distancia 24km");
        reto_chiripo.add("Tipo ruta: combinado");
        reto_chiripo.add("Nivel: Avanzado");

        List<String> reto_jaco = new ArrayList<String>();
        reto_jaco.add("Distancia 30 km");
        reto_jaco.add("Tipo ruta: lastre");
        reto_jaco.add("Nivel: Intermedio");

        List<String> reto_u_paz = new ArrayList<String>();
        reto_u_paz.add("Distancia 20km");
        reto_u_paz.add("Tipo de ruta: lastre y tierra");
        reto_u_paz.add("Nivel Intermedio");


        listDataChild.put(listDataHeader.get(0), reto_chiripo); // Header, Child data
        listDataChild.put(listDataHeader.get(1), reto_jaco);
        listDataChild.put(listDataHeader.get(2), reto_u_paz);
    }


}
