package com.cyclingmap.orion.cyclingmap.data;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cyclingmap.orion.cyclingmap.model.Province;
import com.cyclingmap.orion.cyclingmap.model.Town;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kenneth on 15/07/2015.
 */
public class LocationAddress {
    private static final String TAG = "LocationAddress";
    private static ArrayList<Province> provinces = new ArrayList<>();

    public static void getRouteInfo(final LatLng[] coords, final Context context, final Handler handler) {
       // android.os.Debug.waitForDebugger();
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                ArrayList<Town> towns = new ArrayList<>();
                try {
                    for (int j = 0; j < 1; j++) {
                        double latitude = coords[j].latitude;
                        double longitude = coords[j].longitude;
                        List<Address> addressList = geocoder.getFromLocation(
                                latitude, longitude, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i)).append("\n");
                            }
                            String provinceName = address.getAdminArea();
                            String townName = address.getLocality();
                            // Log.i("Canton ",townName);
                            // if (!province.getTownList().contains(townName))
                            Province p = null;
                            if (provinces.size() > 0) {
                                p = getProvinceByName(provinceName);
                                Log.i("Nombre", p.getNameProvince());
                                if (!provinceHasTown(townName, p.getNameProvince()))
                                    p.getTownList().add(new Town(townName));
                            } else if (p == null) {
                                p = new Province();
                                towns.add(new Town(townName));
                                p.setTownList(towns);
                            }

                            provinces.add(p);

                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "No se puede conectar al geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (provinces != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("direccion", provinces);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("direccion", provinces);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

    public static Province getProvinceByName(String name) {
        Province p = null;
        for (Province province : provinces) {
            if (province.getNameProvince().equals(name)) {
                p = province;
                Log.i("provincia", " existe");
                break;
            }
        }
        return p;
    }

    public static boolean provinceHasTown(String townName, String provinceName) {
        boolean flag = false;
        Province province = getProvinceByName(provinceName);
        ArrayList<Town> towns = province.getTownList();
        for (Town town : towns) {
            if (town.getNameTown().equals(townName))
                flag = true;
        }
        return flag;
    }

    private static class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("");
                    break;
                default:
                    locationAddress = null;
            }
            // tvAddress.setText(locationAddress);
        }
    }
}
