package com.cyclingmap.orion.cyclingmap.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyclingmap.orion.cyclingmap.R;


/**
 * Created by Daniel on 03/07/2015.
 */
public class RoutesSection extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_route_section, container, false);
        return rootView;
    }
}
