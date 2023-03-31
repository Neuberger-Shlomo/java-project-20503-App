package com.example.myapplication.Model;

public class User {
    /**
     *  "id": 1,
     *             "pid": 1,
     *             "roleId": 1,
     *             "password": "1234567890",
     *             "username": "shlomo",
     *             "profile": {
     *         "id": 1,
     *                 "firstName": "test",
     *                 "lastName": "test",
     *                 "email": "test@test.com",
     *                 "phoneNumber": "0123456"
     *     },
     */
    Integer id;
    Integer pid;
    Integer roleId;
    String password;
    String username;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
