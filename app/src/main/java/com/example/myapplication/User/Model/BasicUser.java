package com.example.myapplication.User.Model;


import androidx.annotation.Nullable;

import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.RoleLevel;

import org.jetbrains.annotations.NotNull;

public class BasicUser {
    private String    username;
    private String    id;
    private String    password;
    private String    authToken;
    private RoleLevel level = RoleLevel.BASIC;

    @Nullable
    private Profile profile;

    public BasicUser() {
        this.id        = "";
        this.username  = "";
        this.password  = "";
        this.authToken = "";
        this.level     = null;
    }

    public BasicUser(String username, String password, String authToken, RoleLevel level) {
        this.username  = username;
        this.password  = password;
        this.authToken = authToken;
        this.level     = level;
    }

    public BasicUser(BasicUser u) {
        this.id        = u.id;
        this.username  = u.getUsername();
        this.password  = u.getPassword();
        this.authToken = u.getAuthToken();
        this.level     = u.getLevel();
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

    @Nullable
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(@NotNull Profile profile) {
        this.profile = profile;
    }

    public boolean isLoggedIn() {
        return getAuthToken() != null && !getAuthToken().isEmpty();
    }
}