package com.example.myapplication.Model;

import java.util.ArrayList;

public class Shift {
    private String shiftDate;
    private int numOfRequiredWorkers;
    private int numOfScheduledWorkers;

    private ArrayList<Profile> scheduledWorkers;

    public Shift(String shiftDate, int numOfRequiredWorkers) {
        this.shiftDate = shiftDate;
        this.numOfRequiredWorkers = numOfRequiredWorkers;
        this.numOfScheduledWorkers = 0;
        this.scheduledWorkers = new ArrayList<>(numOfRequiredWorkers);
    }

    /*
    * this second constructor is for testing use only*/
    public Shift(String shiftDate, int numOfRequiredWorkers, ArrayList<Profile> workersList) {
        this.shiftDate = shiftDate;
        this.numOfRequiredWorkers = numOfRequiredWorkers;
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
        this.scheduledWorkers.add(scheduledWorker);
        setNumOfScheduledWorkers(scheduledWorkers.size());
    }

    public ArrayList<Profile> getScheduledWorkers(){
        return scheduledWorkers;
    }
}
