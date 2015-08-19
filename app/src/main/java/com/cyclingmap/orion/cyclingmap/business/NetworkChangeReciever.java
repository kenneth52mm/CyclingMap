package com.cyclingmap.orion.cyclingmap.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cyclingmap.orion.cyclingmap.data.DBHelper;
import com.cyclingmap.orion.cyclingmap.model.Route;

/**
 * Created by kenneth on 13/08/2015.
 */
public class NetworkChangeReciever extends BroadcastReceiver {


    private Route route;
    private int id_ser;
    private DBHelper dbHelper;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityState(context);
        switch (status) {
            case -1:
                break;
            case 0:
                dbHelper=new DBHelper(context);
                dbHelper.addRoute(getRoute());
                Toast.makeText(context, "No hay conexion", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Object object[] = new Object[2];
                object[0] = getRoute();
                object[1] = getId_ser();
                RouteWsHelper wsHelper = new RouteWsHelper();
                wsHelper.execute(object);
                Toast.makeText(context, "Datos guardados", Toast.LENGTH_LONG).show();
                break;
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
