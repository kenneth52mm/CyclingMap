package com.cyclingmap.orion.cyclingmap.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.cyclingmap.orion.cyclingmap.utils.CustomListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FindedRoutes extends ActionBarActivity {

    CustomListAdapter listAdapter;
    ExpandableListView eListView;
    HashMap<String, List<String>> listWithSubitem;
    List<String> listDataHeader;
    ArrayList<Route> findeRoutes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finded_routes);

        eListView = (ExpandableListView) findViewById(R.id.lfinded_routes);

        routesData();

        listAdapter = new CustomListAdapter(this, listDataHeader, listWithSubitem);

        eListView.setAdapter(listAdapter);
    }

    private void routesData()
    {
        Bundle b = getIntent().getBundleExtra("b_finded");
        if(b!= null) {
            findeRoutes = (ArrayList<Route>) b.getSerializable("finded_routes");

            listDataHeader = new ArrayList<String>();
            listWithSubitem = new HashMap<String, List<String>>();

            for (int i = 0; i <= findeRoutes.size()-1; i++) {
                listDataHeader.add("Ruta " + findeRoutes.get(i).getIdRoute());
            }

            for (int i = 0;  i <= findeRoutes.size()-1; i++) {
                List<String> items = new ArrayList<String>();
                items.add( getString(R.string.find_dist) + findeRoutes.get(i).getDistance());
                items.add( getString(R.string.find_speed) + findeRoutes.get(i).getAvgSpeed());
                items.add(getString(R.string.find_level) + findeRoutes.get(i).getDifficultyLevel());
                listWithSubitem.put(listDataHeader.get(i), items);
            }
        }
    }
}
