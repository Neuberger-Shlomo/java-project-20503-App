package com.example.myapplication.Model;


public class BasicUser {
    private   String username;
    private   String id;
    private    String password;
    private   String authToken;
    private    RoleLevel level;
    public BasicUser() {
        this.username = "";
        this.password = "";
        this.authToken = "";
        this.level = null;
    }
    public BasicUser(String username, String password, String authToken, RoleLevel level) {
        this.username = username;
        this.password = password;
        this.authToken = authToken;
        this.level = level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public RoleLevel getLevel() {
        return level;
    }

    public void setLevel(RoleLevel level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
