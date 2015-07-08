package com.cyclingmap.orion.cyclingmap.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.business.UserRoutesAdapter;
import com.cyclingmap.orion.cyclingmap.model.Route;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Daniel on 03/07/2015.
 */
public class RoutesSection extends Fragment {


    ListView lwRoutes;
    UserRoutesAdapter routesAdapter;
    ArrayList<Route> routes=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_route_section, container, false);
        lwRoutes= (ListView) rootView.findViewById(R.id.list);
        routes.add(new Route(10.0,53.41));
        routesAdapter = new UserRoutesAdapter(routes, getActivity().getApplicationContext());
        lwRoutes.setAdapter(routesAdapter);
        return rootView;
    }
}
