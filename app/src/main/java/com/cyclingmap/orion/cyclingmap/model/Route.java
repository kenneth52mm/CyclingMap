package com.cyclingmap.orion.cyclingmap.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

/**
 * @author Daniel
 */
public class Route implements Serializable{
    private int idRoute;
    private double distance;
    private Time timeToFin; //Tiempo de duracion
    private double avgSpeed;
    private int difficultyLevel;
    private ArrayList<Province> provinces;
    private ArrayList<Coordinate> coordinateList;

    public Route() {
    }

    public Route(int idRoute, double distance, Time timeToFinish, double avgSpeed,
                 int dLevel, ArrayList<Province> provinceArrayList, ArrayList<Coordinate> coordinateList) {
        this.idRoute = idRoute;
        this.distance = distance;
        this.timeToFin = timeToFinish;
        this.avgSpeed = avgSpeed;
        this.difficultyLevel = dLevel;
        this.provinces = provinceArrayList;
        this.coordinateList = coordinateList;
    }

    public Route(double i, double s) {
        this.distance = i;
        this.avgSpeed = s;
    }

    /**
     * @return the idRoute
     */
    public int getIdRoute() {
        return idRoute;
    }

    /**
     * @param idRoute the idRoute to set
     */
    public void setIdRoute(int idRoute) {
        this.idRoute = idRoute;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the runTime
     */


    /**
     * @return the avgSpeed
     */
    public double getAvgSpeed() {
        return avgSpeed;
    }

    /**
     * @param avgSpeed the avgSpeed to set
     */
    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    /**
     * @return the difficultyLevel
     */
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    /**
     * @param difficultyLevel the difficultyLevel to set
     */
    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    /**
     * @return the townList
     */
    public ArrayList<Province> getProvinces() {
        return provinces;
    }

    /**
     * @param provinceArrayList the townList to set
     */
    public void setProvinces(ArrayList<Province> provinceArrayList) {
        this.provinces = provinceArrayList;
    }

    /**
     * @return the coordinateList
     */
    public ArrayList<Coordinate> getCoordinateList() {
        return coordinateList;
    }

    /**
     * @param coordinateList the coordinateList to set
     */
    public void setCoordinateList(ArrayList<Coordinate> coordinateList) {
        this.coordinateList = coordinateList;
    }

    /**
     * @return the timeToFin
     */
    public Time getTimeToFin() {
        return timeToFin;
    }

    /**
     * @param timeToFin the timeToFin to set
     */
    public void setTimeToFin(Time timeToFin) {
        this.timeToFin = timeToFin;
    }

}
