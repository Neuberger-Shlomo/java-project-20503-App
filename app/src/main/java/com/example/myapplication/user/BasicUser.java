package com.example.myapplication.user;

/**
 * This is the basic user singletone
 */
public class BasicUser {
    static BasicUser user;
    String username;
    String password;
    String authToken;
    String refreshToken;
    RoleLevel level;

    private BasicUser() {
        
    }

    public static BasicUser getInstance() {
        if(user == null) user = new BasicUser();
        return user;
    }
    
    
    public static BasicUser getUser() {
        return user;
    }

    public static void setUser(BasicUser user) {
        BasicUser.user = user;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RoleLevel getLevel() {
        return level;
    }

    public void setLevel(RoleLevel level) {
        this.level = level;
    }
}
