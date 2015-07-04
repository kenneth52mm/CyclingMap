/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package com.cyclingmap.orion.cyclingmap.model;

import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class Friend {
    private ArrayList<User> UserList;

    
    public Friend(){}
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
