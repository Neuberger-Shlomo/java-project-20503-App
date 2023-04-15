package com.example.myapplication.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Shift {
    private String shiftDate;
    private int    numOfRequiredWorkers;
    private int    numOfScheduledWorkers;
    private int    id;
    private int    startHour;
    private int    duration;


    private ArrayList<Profile> scheduledWorkers;
    private int weekNumber, year = 123, dayNumber;




    public Shift(String shiftDate, int numOfRequiredWorkers, int id,
                 int startHour, int duration, int weekNumber, int dayNumber, int numOfScheduledWorkers) {
        this.shiftDate             = shiftDate;
        this.numOfRequiredWorkers  = numOfRequiredWorkers;
        this.id                    = id;
        this.startHour             = startHour;
        this.duration              = duration;
        this.numOfScheduledWorkers = numOfScheduledWorkers;
        this.scheduledWorkers      = new ArrayList<>(numOfRequiredWorkers);
        this.weekNumber = weekNumber;
        this.dayNumber = dayNumber;
    }


    public String getDate(){
        return shiftDate;
    }

    public static Shift fromJSON(JSONObject object) throws JSONException {

        int      numOfRequiredWorkers = object.getInt("employeeCount");
        int      numOfScheduledWorkers = object.getInt("numOfScheduledWorkers");
        int      id                   = object.getInt("id");
        int      weekNumber           = object.getInt("weekNumber");
        int      dayNumber            = object.getInt("dayNumber");
        int      startHour            = object.getInt("startHour");
        int      duration             = object.getInt("duration");
        ArrayList<Profile> scheduledWorkers = new ArrayList<>();
        Calendar calendar             = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, dayNumber);

        // Get the date from the calendar object
        Date   date      = calendar.getTime();
        String shiftDate = String.format("%d-%d-%d",date.getDate(),date.getMonth()+1,
                                         date.getYear()+1900 );

        Log.d("mymsg", "fromJSON: "+shiftDate);

        return new Shift(shiftDate, numOfRequiredWorkers,id, startHour,
                duration, weekNumber, dayNumber, numOfScheduledWorkers);
    }


    /*
     * this second constructor is for testing use only*/
    public Shift(String shiftDate, int numOfRequiredWorkers, ArrayList<Profile> workersList,
                 int id, int startHour, int duration) {
        this.shiftDate             = shiftDate;
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

    public String getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
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
    public String getScheduledWorkersStr(){
        String output = "";
        for(Profile p:scheduledWorkers){
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

    public int getDuration() {
        return duration;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
