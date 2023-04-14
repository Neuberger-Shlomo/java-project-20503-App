package com.example.myapplication.Model;

public class Constrains {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String constrain;
    private String constrainDate;

    public Constrains(String firstName, String lastName, String phoneNumber, String constrain, String constrainDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.constrain = constrain;
        this.constrainDate = constrainDate;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getConstrain() {
        return constrain;
    }

    public void setConstrain(String constrain) {
        this.constrain = constrain;
    }

    public String getConstrainDate() {
        return constrainDate;
    }

    public void setConstrainDate(String constrainDate) {
        this.constrainDate = constrainDate;
    }
}
