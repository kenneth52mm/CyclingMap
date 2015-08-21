package com.cyclingmap.orion.cyclingmap.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.Route;

import java.util.ArrayList;

/**
 * Created by kenneth on 13/08/2015.
 */
public class NetworkChangeReciever extends BroadcastReceiver {


    private Route route;
    private int id_ser;
    private DBHelper dbHelper;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.i("Reciver","entro");
        dbHelper = new DBHelper(context);
        int status = NetworkUtil.getConnectivityState(context);
        if (status == 1 && dbHelper.hasRoutes()) {
            Log.i("Reciever","hay rutas!!");
            ArrayList<Route> routes = (ArrayList<Route>) dbHelper.retrieveAllRoutes();
            Object object[] = new Object[2];
            object[0] = routes.get(0);
            object[1] = dbHelper.getIdUser();
            RouteWsHelper wsHelper = new RouteWsHelper();
            wsHelper.execute(object);
            Toast.makeText(context, "Datos guardados", Toast.LENGTH_LONG).show();
        }
        else{
            Log.i("Reciever","no hay rutas");
        }
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getId_ser() {
        return id_ser;
    }

    public void setId_ser(int id_ser) {
        this.id_ser = id_ser;
    }
}
