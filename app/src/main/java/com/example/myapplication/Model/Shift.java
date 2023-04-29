package com.example.myapplication.Model;

import android.annotation.SuppressLint;

import com.example.myapplication.Common.Views.Fragments.IModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Shift class represents a shift filled by the manager.
 * veriables:
 * numOfRequiredWorkers
 * numOfScheduledWorkers
 * id
 * startHour
 * duration
 * startTime
 * endTime
 * startDate
 * scheduledWorkers
 * weekNumber=  week number of the year (1-52)
 * year
 * dayNumber=  day of the week
 */
public class Shift implements IModel {

    private int numOfRequiredWorkers;
    private int numOfScheduledWorkers;
    private int id;
    private int startHour;
    private int duration;

    private Timestamp endTime;
    private Timestamp startTime;

    private Date startDate;

    private ArrayList<Profile> scheduledWorkers;
    private int                weekNumber, year = 2023, dayNumber;
    //TODO: implement year getting according to database

    public Shift(String shiftDate, int numOfRequiredWorkers, int id,
                 int startHour, int duration, int weekNumber, int dayNumber,
                 int numOfScheduledWorkers) {
        this.numOfRequiredWorkers  = numOfRequiredWorkers;
        this.id                    = id;
        this.startHour             = startHour;
        this.duration              = duration;
        this.numOfScheduledWorkers = numOfScheduledWorkers;
        this.scheduledWorkers      = new ArrayList<>(numOfRequiredWorkers);
        this.weekNumber            = weekNumber;
        this.dayNumber             = dayNumber;
    }

    /*
     * this second constructor is for testing use only*/
    public Shift(String shiftDate, int numOfRequiredWorkers, ArrayList<Profile> workersList,
                 int id, int startHour, int duration) {
        this.numOfRequiredWorkers  = numOfRequiredWorkers;
        this.id                    = id;
        this.startHour             = startHour;
        this.duration              = duration;
        this.numOfScheduledWorkers = 0;
        this.scheduledWorkers      = new ArrayList<>(numOfRequiredWorkers);

        for (Profile worker : workersList) {
            setScheduledWorker(worker);
        }
    }

    public static Shift fromJSON(JSONObject object) throws JSONException {
        Shift shift = new Gson().fromJson(object.toString(), Shift.class);
        shift.setNumOfRequiredWorkers(object.optInt("employeeCount", -1));
        shift.setNumOfScheduledWorkers(object.optInt("numOfScheduledWorkers", -1));


        return shift;
    }

    public String getDate() {
        return getShiftDate();
    }

    @SuppressLint("DefaultLocale")
    public String getShiftDate() {
        return String.format("%d-%d-%d", startDate.getDate(), startDate.getMonth() + 1,
                             startDate.getYear() + 1900);
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getNumOfRequiredWorkers() {
        return numOfRequiredWorkers;
    }

    public void setNumOfRequiredWorkers(int numOfRequiredWorkers) {
        this.numOfRequiredWorkers = numOfRequiredWorkers;
    }

    public int getNumOfScheduledWorkers() {
        return numOfScheduledWorkers;
    }

    public void setNumOfScheduledWorkers(int numOfScheduledWorkers) {
        this.numOfScheduledWorkers = numOfScheduledWorkers;
    }

    public void setScheduledWorker(Profile scheduledWorker) {
        if (scheduledWorkers.size() < numOfRequiredWorkers) {
            this.scheduledWorkers.add(scheduledWorker);
        }
        setNumOfScheduledWorkers(scheduledWorkers.size());
    }

    public ArrayList<Profile> getScheduledWorkers() {
        return scheduledWorkers;
    }

    public String getScheduledWorkersStr() {
        String output = "";
        for (Profile p : scheduledWorkers) {
            output += p.toString();
        }
        return output;
    }

    public int getId() {
        return id;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toPrettyString() {
        return "Shift Date: " + this.getDate()
               + (this.getNumOfRequiredWorkers() != -1 ?
                ("\nNumber Of Required Workers: " + this.getNumOfRequiredWorkers()) : "")
               + (this.getNumOfScheduledWorkers() != -1 ?
                "\nNumber Of Scheduled Workers: " + this.getNumOfScheduledWorkers() : "");
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getStartTime(boolean shortVersion) {
        SimpleDateFormat sdf           = new SimpleDateFormat("HH:mm");
        String           formattedTime = sdf.format(startTime.getTime());
        return shortVersion ? formattedTime : getStartTime().toString();

    }

    public String getEndTime(boolean shortVersion) {
        SimpleDateFormat sdf           = new SimpleDateFormat("HH:mm");
        String           formattedTime = sdf.format(endTime.getTime());
        return shortVersion ? formattedTime : getEndTime().toString();
    }
}
