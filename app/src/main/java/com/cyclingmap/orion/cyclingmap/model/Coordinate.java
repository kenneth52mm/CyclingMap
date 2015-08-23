/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyclingmap.orion.cyclingmap.model;

import java.io.Serializable;

/**
 *
 * @author Daniel
 */
public final class Coordinate implements Serializable {
    private double x;
    private double y;

    public Coordinate() {

    }

    public Coordinate(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }
    
    @Override
    public String toString (){
        return ("(" + x + "," + y + " )");
    }
    
}
