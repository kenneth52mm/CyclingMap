package com.cyclingmap.orion.cyclingmap.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Route;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    String sqlCreateCoords = "CREATE TABLE coords (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " lat REAL,lng REAL,id_route INTEGER,FOREIGN KEY(id_route) REFERENCES route(id_route))";
    String sqlCreateRoute = "CREATE TABLE route (id_route INTEGER PRIMARY KEY AUTOINCREMENT" +
            " NOT NULL, distance REAL, time_to_finish NUMERIC, avg_speed REAL,difficulty_level INTEGER);";
    String sqlCreateUser = "CREATE TABLE user (id_user INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "name TEXT, email TEXT, pass TEXT);";
    String sqlCreateUserRoute = "CREATE TABLE user_route (id_route INTEGER, id_user INTEGER," +
            " FOREIGN KEY(id_route) REFERENCES route(id_route),FOREIGN KEY(id_user) REFERENCES " +
            "user(id_user),PRIMARY KEY (id_route,id_user))";
    SQLiteDatabase helper;
    int id;

    public DBHelper(Context contexto) {
        super(contexto, "orion.db", null, 1);
        helper = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateRoute);
        db.execSQL(sqlCreateCoords);
        db.execSQL(sqlCreateUser);
        db.execSQL(sqlCreateUserRoute);
        //id = getIdRoute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS route");
        db.execSQL(sqlCreateRoute);
    }

    public boolean addCoords(List<Coordinate> coordinates) {
        boolean resp = true;
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coord = coordinates.get(i);
            ContentValues values = new ContentValues();
            values.put("lat", coord.getX());
            values.put("lng", coord.getY());
            helper.insert("coords", null, values);
        }
        return resp;
    }

    public List<Coordinate> retrieveAll() {
        List<Coordinate> coordinates = new ArrayList<>();
       // Cursor c = helper.rawQuery("Select lat,lng From coords Where id_route='" + id + "';", null);
        Cursor c = helper.rawQuery("Select lat,lng From coords;", null);
        if (c.moveToFirst()) {
            do {
                double lat = c.getDouble(0);
                double lng = c.getDouble(1);
                coordinates.add(new Coordinate(lat, lng));
            } while (c.moveToNext());
        }
        return coordinates;
    }

    public void addRoute(Route route) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("distance", route.getDistance());
        values.put("time_to_finish", route.getTimeToFin().toString());
        values.put("avg_speed", route.getAvgSpeed());
        values.put("difficulty_level", route.getDifficultyLevel());
        helper.insert("route", null, values);
    }

    public int getIdRoute() {
        int res = 0;
        Cursor c = helper.rawQuery("Select count(*) from route", null);
        if (c.moveToFirst()) {
            do {
                res = c.getInt(0);
            } while (c.moveToNext());
        }
        return res;
    }
}