package com.cyclingmap.orion.cyclingmap.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.UserRoutesAdapter;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.cyclingmap.orion.cyclingmap.utils.CustomListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FindedRoutes extends ActionBarActivity {


   private  ArrayList<Route> findeRoutes;
   private ListView routeList;
    private UserRoutesAdapter routesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finded_routes);
        routeList= (ListView) findViewById(R.id.list);
        routesData();
    }

    private void routesData()
    {
        Bundle b = getIntent().getExtras();
        findeRoutes= (ArrayList<Route>) b.getSerializable("finded_routes");
        routeList= (ListView) findViewById(R.id.list);
        routesAdapter=new UserRoutesAdapter(findeRoutes,FindedRoutes.this);
        routeList.setAdapter(routesAdapter);
        routesAdapter.notifyDataSetChanged();
    }
}
