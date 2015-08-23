package com.cyclingmap.orion.cyclingmap.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author Daniel
 */
public class Town implements Serializable{
    private String nameTown;
    
    public Town(String nTown)
    {
        this.nameTown = nTown;
    }

    /**
     * @return the nameTown
     */
    public String getNameTown() {
        return nameTown;
    }

    /**
     * @param nameTown the nameTown to set
     */
    public void setNameTown(String nameTown) {
        this.nameTown = nameTown;
    }
}
