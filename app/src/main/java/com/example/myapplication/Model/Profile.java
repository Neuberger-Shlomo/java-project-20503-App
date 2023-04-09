package com.example.myapplication.Model;

import androidx.annotation.Nullable;

public class Profile {
    private   String firstName;
    private   String lastName;
    private   String email;
    private   String phoneNumber;
    private int id;

    public Profile() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.phoneNumber = "";
    }


    public Profile(String firstName, String lastName, String email, String phoneNumber, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.id = id;
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
                this.lastName.equals(profile.lastName)&&
                this.phoneNumber.equals(profile.phoneNumber) &&
                this.email.equals(profile.email);
    }
}
