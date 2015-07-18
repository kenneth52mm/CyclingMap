package com.cyclingmap.orion.cyclingmap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cyclingmap.orion.cyclingmap.R;


/**
 * Created by Daniel on 03/07/2015.
 */
public class Principal extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lay_principal, container, false);
        newRoute(v);
        seachRoute(v);
        return v;
    }

    public void newRoute(View v) {
        ImageView newR = (ImageView)v.findViewById(R.id.ImageV1);
        newR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),RutasActivity.class);
                getActivity().startActivity(intent);
            }
        });}
    public void seachRoute(View v){
        ImageView searchR = (ImageView)v.findViewById(R.id.ImageV2);
        searchR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), BuscarRutas.class);
                getActivity().startActivity(i);
            }
        });
    }

}
