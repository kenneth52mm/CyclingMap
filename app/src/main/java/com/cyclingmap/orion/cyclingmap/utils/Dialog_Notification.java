package com.cyclingmap.orion.cyclingmap.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cyclingmap.orion.cyclingmap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 7/29/2015.
 */
public class Dialog_Notification extends DialogFragment {
    ListView lvNotificacion;
    ArrayAdapter arrayAdapter;
    String[] notificaciones;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater;
        inflater = LayoutInflater.from(getActivity());
        View dialogview = inflater.inflate(R.layout.dialog_notification, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        lvNotificacion = (ListView) dialogview.findViewById(R.id.lv_dialog_notific);

        arrayAdapter = new ArrayAdapter<String> (getActivity(), android.R.layout.simple_list_item_1, notificaciones);

        lvNotificacion.setAdapter(arrayAdapter);

        dialog.setView(dialogview);
        dialog.setMessage("Notificaciones ");

        dialog.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return dialog.create();
    }

    public void setNotification(String[] al){
        notificaciones = al;
    }
}
