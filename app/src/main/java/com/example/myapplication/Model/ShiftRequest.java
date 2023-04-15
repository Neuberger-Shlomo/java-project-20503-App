package com.example.myapplication.Model;

public class ShiftRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String shiftDate;

    public ShiftRequest(String firstName, String lastName, String phoneNumber, String shiftDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.shiftDate = shiftDate;
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

    public String getshiftDate() {
        return shiftDate;
    }

    public void setshiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }
}
