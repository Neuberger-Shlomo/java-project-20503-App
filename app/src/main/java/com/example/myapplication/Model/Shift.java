package com.example.myapplication.Model;

import java.util.ArrayList;

public class Shift {
    private String shiftDate;
    private int numOfRequiredWorkers;
    private int numOfScheduledWorkers;
    private int id;
    private int startHour;
    private int duration;



    private ArrayList<Profile> scheduledWorkers;

    public Shift(String shiftDate, int numOfRequiredWorkers, int id, int startHour, int duration) {
        this.shiftDate = shiftDate;
        this.numOfRequiredWorkers = numOfRequiredWorkers;
        this.id = id;
        this.startHour = startHour;
        this.duration = duration;
        this.numOfScheduledWorkers = 0;
        this.scheduledWorkers = new ArrayList<>(numOfRequiredWorkers);
    }


    /*
     * this second constructor is for testing use only*/
    public Shift(String shiftDate, int numOfRequiredWorkers, ArrayList<Profile> workersList, int id, int startHour, int duration) {
        this.shiftDate = shiftDate;
        this.numOfRequiredWorkers = numOfRequiredWorkers;
        this.id = id;
        this.startHour = startHour;
        this.duration = duration;
        this.numOfScheduledWorkers = 0;
        this.scheduledWorkers = new ArrayList<>(numOfRequiredWorkers);

        for(Profile worker:workersList){
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
        if(scheduledWorkers.size() < numOfRequiredWorkers) {
            this.scheduledWorkers.add(scheduledWorker);
        }
        setNumOfScheduledWorkers(scheduledWorkers.size());
    }

    public ArrayList<Profile> getScheduledWorkers(){
        return scheduledWorkers;
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
