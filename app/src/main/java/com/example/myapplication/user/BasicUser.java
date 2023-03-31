package com.example.myapplication.user;


public class BasicUser {
    private  final String username;
    private  final  String password;
    private  final String authToken;
    private  final  RoleLevel level;
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
}
