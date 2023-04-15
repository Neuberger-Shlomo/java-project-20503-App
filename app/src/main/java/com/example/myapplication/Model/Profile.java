package com.example.myapplication.Model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int    id;

    public Profile() {
        this.firstName   = "";
        this.lastName    = "";
        this.email       = "";
        this.phoneNumber = "";
    }


    @Override
    public String toString() {
        return "Profile{" +
               "first_Name='" + firstName + '\'' +
               ", last_Name='" + lastName + '\'' +
               '}';
    }

    public Profile(String first_Name, String last_Name, String email, String phone_Number, int id) {
        this.firstName   = first_Name;
        this.lastName    = last_Name;
        this.email       = email;
        this.phoneNumber = phone_Number;
        this.id          = id;
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
}
