package com.cyclingmap.orion.cyclingmap.model;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class Province {
    private int idProvince;
    private String nameProvince;
    private ArrayList<Town> townList;

    public Province() {
    }

    public Province(String nameProvince, ArrayList<Town> townList) {
        this.nameProvince = nameProvince;
        this.townList = townList;
    }

    public Province(int idProvince, String nameProvince, ArrayList<Town> townList)
    { 
        this.idProvince = idProvince;
        this.nameProvince = nameProvince;
        this.townList = townList;
    }

    /**
     * @return the idProvince
     */
    public int getIdProvince() {
        return idProvince;
    }

    /**
     * @param idProvince the idProvince to set
     */
    public void setIdProvince(int idProvince) {
        this.idProvince = idProvince;
    }

    /**
     * @return the nameProvince
     */
    public String getNameProvince() {
        return nameProvince;
    }

    /**
     * @param nameProvince the nameProvince to set
     */
    public void setNameProvince(String nameProvince) {
        this.nameProvince = nameProvince;
    }

    /**
     * @return the townList
     */
    public ArrayList<Town> getTownList() {
        return townList;
    }

    /**
     * @param townList the townList to set
     */
    public void setTownList(ArrayList<Town> townList) {
        this.townList = townList;
    }
}
