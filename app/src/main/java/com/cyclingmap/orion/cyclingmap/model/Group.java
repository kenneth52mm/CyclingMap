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
public class Group {
    private int idGroup;
    private String nameGroup;
    private ArrayList<User> UserList;

    
    public Group(int idGroup, String nameGroup, ArrayList<User> userList)
    {
        this.idGroup = idGroup;
        this.nameGroup = nameGroup;
        this.UserList = userList;    
    }
    /**
     * @return the idGroup
     */
    public int getIdGroup() {
        return idGroup;
    }

    /**
     * @param idGroup the idGroup to set
     */
    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    /**
     * @return the nameGroup
     */
    public String getNameGroup() {
        return nameGroup;
    }

    /**
     * @param nameGroup the nameGroup to set
     */
    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    /**
     * @return the UserList
     */
    public ArrayList<User> getUserList() {
        return UserList;
    }

    /**
     * @param UserList the UserList to set
     */
    public void setUserList(ArrayList<User> UserList) {
        this.UserList = UserList;
    }
    
}
