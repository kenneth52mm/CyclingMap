package com.cyclingmap.orion.cyclingmap.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class User {
    private int id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private double bestSpeed;
    private double bestTime;
    private double totalDistance;
    private ArrayList<Route> routeList;
    
    public User(int id, String name, String lastName, String email, String password,
                double bestSpeed,double bestTime, double totalDistance, ArrayList<Route> routeList)
    {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.bestSpeed = bestSpeed;
        this.bestTime = bestTime;
        this.totalDistance = totalDistance;
        this.routeList = routeList;
    } 
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the bestSpeed
     */
    public double getBestSpeed() {
        return bestSpeed;
    }

    /**
     * @param bestSpeed the bestSpeed to set
     */
    public void setBestSpeed(double bestSpeed) {
        this.bestSpeed = bestSpeed;
    }

    /**
     * @return the bestTime
     */
    public double getBestTime() {
        return bestTime;
    }

    /**
     * @param bestTime the bestTime to set
     */
    public void setBestTime(double bestTime) {
        this.bestTime = bestTime;
    }

    /**
     * @return the totalDistance
     */
    public double getTotalDistance() {
        return totalDistance;
    }

    /**
     * @param totalDistance the totalDistance to set
     */
    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    /**
     * @return the routeList
     */
    public ArrayList<Route> getRouteList() {
        return routeList;
    }

    /**
     * @param routeList the routeList to set
     */
    public void setRouteList(ArrayList<Route> routeList) {
        this.routeList = routeList;
    }
    
}
