package com.cyclingmap.orion.cyclingmap.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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



import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.utils.CustomListAdapter;


public class RetosActivity extends Activity {

    CustomListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retos);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.explv_retos);

        // preparing list data
        prepareListData();

        listAdapter = new CustomListAdapter (this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
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
        listDataHeader.add("Reto Jaco");
        listDataHeader.add("Reto U de la Paz");
        listDataHeader.add("Reto Chirripo");

        // Adding child data
        List<String> retoJaco = new ArrayList<String>();
        retoJaco.add("Distancia 24 km");
        retoJaco.add("Nivel Principiante");
        retoJaco.add("Tipo de ruta lastre");


        List<String> RetoUPaz = new ArrayList<String>();
        RetoUPaz.add("Distancia 34 km");
        RetoUPaz.add("Nivel Avanzado");
        RetoUPaz.add("Tipo de ruta tierra");


        List<String> RetoChirripo = new ArrayList<String>();
        RetoChirripo.add("Distancia 14 km");
        RetoChirripo.add("Nivel Intermedio");
        RetoChirripo.add("Tipo de ruta montana");


        listDataChild.put(listDataHeader.get(0), retoJaco); // Header, Child data
        listDataChild.put(listDataHeader.get(1), RetoUPaz);
        listDataChild.put(listDataHeader.get(2), RetoChirripo);
    }

}
