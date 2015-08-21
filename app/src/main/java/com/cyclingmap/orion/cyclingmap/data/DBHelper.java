package com.cyclingmap.orion.cyclingmap.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cyclingmap.orion.cyclingmap.model.Coordinate;
import com.cyclingmap.orion.cyclingmap.model.Province;
import com.cyclingmap.orion.cyclingmap.model.Route;
import com.cyclingmap.orion.cyclingmap.model.Town;
import com.cyclingmap.orion.cyclingmap.model.User;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    String sqlCreateCoords = "CREATE TABLE coords (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " lat REAL,lng REAL,id_route INTEGER,FOREIGN KEY(id_route) REFERENCES route(id_route))";
    String sqlCreateRoute = "CREATE TABLE route (id_route INTEGER PRIMARY KEY AUTOINCREMENT" +
            " NOT NULL, distance REAL, time_to_finish NUMERIC, avg_speed REAL,difficulty_level INTEGER);";
    String sqlCreateUser = "CREATE TABLE user (id_user INTEGER PRIMARY KEY NOT NULL, " +
            "name TEXT, email TEXT, pass TEXT,weigth INTEGER,heigth INTEGER, ex_level TEXT, logged INTEGER);";
    String sqlCreateUserRoute = "CREATE TABLE user_route (id_route INTEGER, id_user INTEGER," +
            " FOREIGN KEY(id_route) REFERENCES route(id_route),FOREIGN KEY(id_user) REFERENCES " +
            "user(id_user),PRIMARY KEY (id_route,id_user))";
    String sqlCreateChallenges = "CREATE TABLE challenge (id_challenge INTEGER PRIMARY" +
            " KEY NOT NULL, distance REAL, time_to_finish NUMERIC, avg_speed REAL," +
            "difficulty_level INTEGER);";
    String getSqlCreateRegions = "CREATE TABLE regions (id_route INTEGER NOT NULL, province TEXT, " +
            "town TEXT,FOREIGN KEY (id_route) REFERENCES route(id_route));";
    SQLiteDatabase helper;

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
        db.execSQL(sqlCreateChallenges);
        db.execSQL(getSqlCreateRegions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS route");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS challenge");
        db.execSQL("DROP TABLE IF EXISTS coords");
        db.execSQL("DROP TABLE IF EXISTS regions");
        db.execSQL(sqlCreateRoute);
        db.execSQL(sqlCreateCoords);
        db.execSQL(sqlCreateUser);
        db.execSQL(sqlCreateUserRoute);
        db.execSQL(sqlCreateChallenges);
        db.execSQL(getSqlCreateRegions);
        deleteChallenges();
    }

    public boolean isLogged() {
        boolean resp = true;
        Cursor c = helper.rawQuery("Select logged from user;", null);
        if (c.moveToFirst()) {
            do {
                resp = (c.getInt(0) != 0);
            } while (c.moveToNext());
        }
        return resp;
    }

    public int addUser(User user) {
        ContentValues values = new ContentValues();
        values.put("id_user", user.getId());
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        if (user.getPassword() != null)
            values.put("pass", user.getPassword());
        values.put("logged", 1);
        int resp = (int) helper.insert("user", null, values);
        return resp;
    }

    public boolean addCoords(List<Coordinate> coordinates, int id_route) {
        boolean resp = true;
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coord = coordinates.get(i);
            ContentValues values = new ContentValues();
            values.put("lat", coord.getX());
            values.put("lng", coord.getY());
            values.put("id_route", id_route);
            helper.insert("coords", null, values);
        }
        return resp;
    }

    public void addRegions(String town, String province, int id_route) {
        ContentValues values = new ContentValues();
        values.put("id_route", id_route);
        values.put("province", province);
        values.put("town", town);
        helper.insert("regions", null, values);
    }

    public void addRoute(Route route) {
        ContentValues values = new ContentValues();
        values.put("distance", route.getDistance());
        values.put("time_to_finish", route.getTimeToFin().toString());
        values.put("avg_speed", route.getAvgSpeed());
        values.put("difficulty_level", route.getDifficultyLevel());
        helper.insert("route", null, values);
        int idRoute = getIdRoute();
        addCoords(route.getCoordinateList(), idRoute);
        if (route.getProvinces() != null) {
            for (Province p : route.getProvinces()) {
                String province = p.getNameProvince();
                String town = p.getTownList().get(0).getNameTown();
                addRegions(town, province, idRoute);
            }
        }
    }


    public void addChallenges(Route route) {

        try {
            ContentValues values = new ContentValues();
            values.put("id_challenge", route.getIdRoute());
            values.put("distance", route.getDistance());
            values.put("time_to_finish", route.getTimeToFin().toString());
            values.put("avg_speed", route.getAvgSpeed());
            values.put("difficulty_level", route.getDifficultyLevel());
            helper.insertOrThrow("challenge", null, values);
            int idRoute = getIdRoute();
            if (route.getCoordinateList() != null)
                addCoords(route.getCoordinateList(), idRoute);
            if (route.getProvinces() != null) {
                for (Province p : route.getProvinces()) {
                    String province = p.getNameProvince();
                    String town = p.getTownList().get(0).getNameTown();
                    addRegions(town, province, idRoute);
                }
                if (route.getProvinces() != null) {
                    for (Province p : route.getProvinces()) {
                        String province = p.getNameProvince();
                        String town = p.getTownList().get(0).getNameTown();
                        addRegions(town, province, idRoute);
                    }
                }
            }
        } catch (SQLiteConstraintException ex) {
            Log.e("Sqlite", ex.getMessage());
        }
//        deleteChallenges();

    }
    public boolean hasChallenges(){
        boolean flag=false;
        Cursor c=helper.rawQuery("Select * from challenges;",null);
        if(c.moveToFirst()){
            do{
                flag=true;
            }while (c.moveToNext());
        }
        return flag;
    }

    public void deleteChallenges() {
        String query = "Delete from challenges;";
        helper.execSQL(query);
    }


    public void addUserFeatures(int user_id, int weigth, int heigth, String ex_level) {
        String query = "Update user set weigth,heigth,ex_level  where id='" + user_id + "'";
        ContentValues values = new ContentValues();
        values.put("weitgh", weigth);
        values.put("heitgh", heigth);
        values.put("ex_level", ex_level);
        helper.update("user", values, query, null);
    }

    public int loggin() {
        ContentValues values = new ContentValues();
        values.put("logged", 1);
        int resp = helper.update("user", values, null, null);
        return resp;
    }
    public void addId(int id){
        ContentValues values=new ContentValues();
        values.put("id_user",id);
        helper.update("user",values,null,null);
    }

    public void loggout() {
        ContentValues values = new ContentValues();
        values.put("logged", 0);
        helper.update("user", values, null, null);
    }


    public List<Route> retrieveAllRoutes() {
        ArrayList<Route> routes = new ArrayList<>();
        Cursor c = helper.rawQuery("Select * from challenge;", null);
        if (c.moveToFirst()) {
            do {
                Route route = new Route();
                int id = c.getInt(0);
                double distance = c.getDouble(1);
                Time time = Time.valueOf(c.getString(2));
                double avg_speed = c.getDouble(3);
                int level = c.getInt(4);
                ArrayList<Coordinate> coordinates = (ArrayList<Coordinate>) retrieveAll(id);
                route.setIdRoute(id);
                route.setDistance(distance);
                route.setTimeToFin(time);
                route.setAvgSpeed(avg_speed);
                route.setCoordinateList(coordinates);
                Cursor c1 = helper.rawQuery("Select town,province from regions where id_route='" + id + "';", null);
                if (c1.moveToFirst()) {
                    do {
                        String town = c1.getString(0);
                        String province = c1.getString(1);
                        ArrayList<Town> towns = new ArrayList<>();
                        towns.add(new Town(town));
                        ArrayList<Province> provinces = new ArrayList<>();
                        provinces.add(new Province(province, towns));
                        route.setProvinces(provinces);
                    } while (c1.moveToNext());
                    c1.close();
                }
                routes.add(route);
            } while (c.moveToNext());
            c.close();
        }
        return routes;
    }

    public List<Coordinate> retrieveAll(int id) {
        List<Coordinate> coordinates = new ArrayList<>();
        Cursor c = helper.rawQuery("Select lat,lng From coords Where id_route='" + id + "';", null);
        if (c.moveToFirst()) {
            do {
                double lat = c.getDouble(0);
                double lng = c.getDouble(1);
                coordinates.add(new Coordinate(lat, lng));
            } while (c.moveToNext());
        }
        c.close();
        return coordinates;
    }

    public int getIdUser() {
        int id_ser = 0;
        Cursor c = helper.rawQuery("Select id_user from user;", null);
        if (c.moveToFirst()) {
            do {
                id_ser = c.getInt(0);
                break;
            } while (c.moveToNext());
        }
        return id_ser;
    }

    public User getUser() {
        User u = new User();
        Cursor c = helper.rawQuery("Select * from user;", null);
        //int id=c.getInt(0);
        if (c.moveToFirst()) {
            do {
                String name = c.getString(1);
                String mail = c.getString(2);
                //  u.setId(id);
                u.setName(name);
                u.setEmail(mail);
            } while (c.moveToNext());
        }

        return u;
    }

    public boolean isUser(String mail) {
        boolean resp = false;
        Cursor c = helper.rawQuery("Select * from user where email='" + mail + "';", null);
        if (c.moveToFirst()) {
            do {
                resp = true;
            } while (c.moveToNext());
        }
        return resp;
    }


    public int getIdRoute() {
        int res = 0;
        Cursor c = helper.rawQuery("Select max(id_route) from route", null);
        if (c.moveToFirst()) {
            do {
                res = c.getInt(0);
            } while (c.moveToNext());
        }
        return res;
    }
}
