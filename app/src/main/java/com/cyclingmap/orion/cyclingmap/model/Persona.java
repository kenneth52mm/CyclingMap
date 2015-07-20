package com.cyclingmap.orion.cyclingmap.model;

import java.util.Date;

/**
 * Created by peter on 6/30/2015.
 */
public class Persona {
    private Login login;
    public Login getLogin() {
        return login;
    }
    public void setLogin(Login login) {
        this.login = login;
    }

    private int Id;
    public int getIdPerson() {
        return Id;
    }
    public void setIdPerson(int idPerson) {
        this.Id = idPerson;
    }

    private String Nombre;
    public String getName() {
        return Nombre;
    }
    public void setName(String name) {
        this.Nombre = name;
    }

    private Double TotalDistance;
    public Double getTotalDistance() {
        return TotalDistance;
    }
    public void setTotalDistance(Double totalDistance) {
        this.TotalDistance = totalDistance;
    }

    private Date BestTime;
    public Date getBestTime() {
        return BestTime;
    }
    public void setBestTime(Date bestTime) {
        this.BestTime = bestTime;
    }

    public Persona() { }

    public Persona(int idPer, String n)
    {
        this.Id = idPer;
        this.Nombre = n;
    }

    public Persona(int id, String nombre, Login l)
    {
        this.Id = id;
        this.Nombre = nombre;
        login = l;
    }

}
