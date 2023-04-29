package com.example.myapplication.UserMVC.Model;


import androidx.annotation.Nullable;

import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.RoleLevel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * represents a basic user  login and permissions data - that is required for user login
 * <p>
 * veriables:
 * <p>
 * username = the user name fo login
 * id = the user id
 * password = password for login
 * authToken = authentication token of the user
 * RoleLevel = the user permission level (basic or manager)
 */
public class User {
    private String    username;
    private String    id;
    private String    password;
    private String    authToken;
    private RoleLevel level = RoleLevel.BASIC;

    @Nullable
    private Profile profile;

    public User() {
        this.id        = "";
        this.username  = "";
        this.password  = "";
        this.authToken = "";
        this.level     = null;
    }

    public User(String username, String password, String authToken, RoleLevel level) {
        this.username  = username;
        this.password  = password;
        this.authToken = authToken;
        this.level     = level;
    }

    public User(User u) {
        this.id        = u.id;
        this.username  = u.getUsername();
        this.password  = u.getPassword();
        this.authToken = u.getAuthToken();
        this.level     = u.getLevel();
    }

    public static User fromJSON(JSONObject object) throws JSONException {
        User user = new User();
        user.setId(object.getString("id"));
        user.setLevel(RoleLevel.values()[object.getJSONObject("role").getInt("roleLevel")]);
        user.setUsername(object.getString("username"));

        return user;
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

    public boolean isAdmin() {
        return level.ordinal() > RoleLevel.BASIC.ordinal();
    }

    public boolean isSuperAdmin() {
        return level.ordinal() > RoleLevel.MANAGER.ordinal();
    }

}
