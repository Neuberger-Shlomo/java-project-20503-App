package com.example.myapplication.Model;

import com.example.myapplication.Common.Views.Fragments.IModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class ShiftRequest implements IModel {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String shiftDate;
    private int    shiftId;
    private int    id;
    private int    uid;
    private String timestamp;
    private int    startHour;
    private int    duration;

    public ShiftRequest(String firstName, String lastName, String phoneNumber, String shiftDate,
                        int shiftId, int id, int uid, String timestamp, int startHour,
                        int duration) {
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.phoneNumber = phoneNumber;
        this.shiftDate   = shiftDate;
        this.shiftId     = shiftId;
        this.id          = id;
        this.uid         = uid;
        this.timestamp   = timestamp;
        this.startHour   = startHour;
        this.duration    = duration;
    }

    public ShiftRequest(int shiftId, int uid) {
        this.shiftId = shiftId;
        this.uid = uid;

    }

    public static ShiftRequest fromJSON(JSONObject object) throws JSONException {

        JSONObject users       = object.getJSONObject("user");
        JSONObject profile     = users.getJSONObject("profile");
        JSONObject shift       = object.getJSONObject("shift");
        String     firstName   = profile.getString("firstName");
        String     lastName    = profile.getString("lastName");
        String     phoneNumber = profile.getString("phoneNumber");
        int        id          = object.getInt("id");
        int        uid         = object.getInt("uid");
        int        shiftId     = object.getInt("shiftId");

        int weekNumber = shift.getInt("weekNumber");
        int dayNumber  = shift.getInt("dayNumber");
        int year       = shift.getInt("year");
        int startHour  = shift.getInt("startHour");
        int duration   = shift.getInt("duration");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, dayNumber);

        // Get the date from the calendar object
        Date date = calendar.getTime();
        String shiftDate = String.format("%d-%d-%d", date.getDate(), date.getMonth() + 1,
                                         year);

        String timestamp = object.getString("timestamp");

        return new ShiftRequest(firstName, lastName, phoneNumber, shiftDate, shiftId, id, uid,
                                timestamp, startHour, duration);
    }

    public int getStartHour() {
        return startHour;
    }

    public int getDuration() {
        return duration;
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

    public String getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String toPrettyString() {
        return "Full Name: " +
               this.getFirstName() + "\t\t" +
               this.getLastName() + "\nPhone Number: " +
               this.getPhoneNumber() + "\nShift Requested Date: " +
               this.getShiftDate();
    }
}
