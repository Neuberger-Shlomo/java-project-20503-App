package com.example.myapplication.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class Profile {
    private   String first_Name;
    private   String last_Name;
    private   String email;
    private   String phone_Number;
    private int id;

    public Profile() {
        this.first_Name = "";
        this.last_Name = "";
        this.email = "";
        this.phone_Number = "";
    }


    public Profile(String first_Name, String last_Name, String email, String phone_Number, int id) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.email = email;
        this.phone_Number = phone_Number;
        this.id = id;
    }



    public String getFirst_Name() {
        return first_Name;
    }

    public void setFirst_Name(String first_Name) {
        this.first_Name = first_Name;
    }

    public String getLast_Name() {
        return last_Name;
    }

    public void setLast_Name(String last_Name) {
        this.last_Name = last_Name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        this.phone_Number = phone_Number;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Profile profile) {
        return this.first_Name.equals(profile.first_Name) &&
                this.last_Name.equals(profile.last_Name)&&
                this.phone_Number.equals(profile.phone_Number) &&
                this.email.equals(profile.email);
    }
}
