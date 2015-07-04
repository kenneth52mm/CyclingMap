package com.cyclingmap.orion.cyclingmap.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Daniel
 */
public class Notification {
    private int idNotification;
    private String message;

    public Notification(int idNotification, String message){
        
        this.idNotification = idNotification;
        this.message = message;
    }
    /**
     * @return the idNotification
     */
    public int getIdNotification() {
        return idNotification;
    }

    /**
     * @param idNotification the idNotification to set
     */
    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
