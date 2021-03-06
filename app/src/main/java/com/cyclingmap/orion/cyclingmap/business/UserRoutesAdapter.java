package com.cyclingmap.orion.cyclingmap.business;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cyclingmap.orion.cyclingmap.R;
import com.cyclingmap.orion.cyclingmap.model.Province;
import com.cyclingmap.orion.cyclingmap.model.Route;

import java.util.List;

/**
 * Created by kenneth on 07/07/2015.
 */
public class UserRoutesAdapter extends ArrayAdapter<Route> {

    public List<Route> routesList;
    private Context context;

    public UserRoutesAdapter(List<Route> mroutesList, Context mContext) {
        super(mContext, R.layout.list_user_routes, mroutesList);
        routesList = mroutesList;
        context=mContext;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Route route=getRoutesList().get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.list_user_routes, null);
        TextView txt1 = (TextView) item.findViewById(R.id.route_name);
        Province province=route.getProvinces().get(0);
        String nameProvince=province.getNameProvince();
        String townName=province.getTownList().get(0).getNameTown();
        txt1.setText(nameProvince+" - "+townName);
        TextView txt2 = (TextView) item.findViewById(R.id.text2);
        txt2 .setText(route.getDistance()+" km");
        return item;
    }

    public List<Route> getRoutesList() {
        return routesList;
    }

    public void setRoutesList(List<Route> routesList) {
        this.routesList = routesList;
    }
}
