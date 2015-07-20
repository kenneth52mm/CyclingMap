package com.cyclingmap.orion.cyclingmap.model;

/**
 * Created by peter on 6/30/2015.
 */
public class Login {
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Login() { }

    public Login(String mail, String pass){
        setEmail(mail);
        setPassword(pass);
    }
}
