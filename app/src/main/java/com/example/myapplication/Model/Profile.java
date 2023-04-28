package com.example.myapplication.Model;

import com.example.myapplication.Common.Views.Fragments.IModel;
import com.example.myapplication.UserMVC.Model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Profile implements IModel {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int    id;
    private RoleLevel maxRole = RoleLevel.BASIC;

    private ArrayList<User> users;


    public Profile() {
        this.firstName   = "";
        this.lastName    = "";
        this.email       = "";
        this.phoneNumber = "";
        users= new ArrayList<>();
    }

    public Profile(String first_Name, String last_Name, String email, String phone_Number, int id) {
        this.firstName   = first_Name;
        this.lastName    = last_Name;
        this.email       = email;
        this.phoneNumber = phone_Number;
        this.id          = id;
        users= new ArrayList<>();
    }
    @Override
    public String toString() {
        return "Profile{" +
               "firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", id=" + id +
               '}';
    }



    public static Profile fromJSON(JSONObject object) throws JSONException {
        Gson g = new Gson();
        return g.fromJson(object.toString(), Profile.class);
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Profile profile) {
        return this.firstName.equals(profile.firstName) &&
               this.lastName.equals(profile.lastName) &&
               this.phoneNumber.equals(profile.phoneNumber) &&
               this.email.equals(profile.email);
    }

    public String toPrettyString() {
        return this.firstName + " " + this.lastName + " (" + this.email + ")";
    }

    public RoleLevel getMaxRole() {
        AtomicInteger maxValue = new AtomicInteger();
        maxValue.set(maxRole.ordinal());
        users.forEach(user-> maxValue.set(Math.max(maxValue.get(), user.getLevel().ordinal())));
        maxRole = RoleLevel.values()[maxValue.get()%RoleLevel.values().length];
        return maxRole;
    }

    public void setMaxRole(RoleLevel maxRole) {

        this.maxRole = maxRole;
    }

    public ArrayList<User> getUsers() {
        if(users == null) users = new ArrayList<>();
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
