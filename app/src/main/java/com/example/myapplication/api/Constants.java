package com.example.myapplication.api;

import com.google.gson.Gson;

final public class Constants {
    // In android emulator the route 10.0.2.2 is redirected to host 127.0.0.1 (aka localhost)
    public final static String BASE_URL = "http://10.0.2.2:8080";

    public final static String SHIFT_ROUTE     = "available-shifts";
    public final static String PROFILE_ROUTE     = "profiles";
    public final static String CONSTRAINTS_ROUTE     = "constraints";
    public final static String SHIFT_REQUESTS_ROUTE     = "shiftsrequests";

    public final static String SCHEDULE_ROUTE = "schedules";
    public final static String ADD_SHIFT_ROUTE = SHIFT_ROUTE + "/addShift";
    public final static String ADD_WORKER_TO_SHIFT_ROUTE = SCHEDULE_ROUTE + "/addWorker";

    public final static String GET_ALL_SHIFTS = SHIFT_ROUTE + "/test";

    public final static String GET_ALL_PROFILES = PROFILE_ROUTE + "/";

    public final static String GET_ALL_CONSTRAINTS = CONSTRAINTS_ROUTE + "/";
    public final static String GET_ALL_SHIFT_REQUESTS = SHIFT_REQUESTS_ROUTE + "/";

    public static final String GET_SHIFTS_FROM_SCHEDULE = SCHEDULE_ROUTE + "/shifts_from_schedule";
    public static final String GET_FREE_WORKERS = "/free-workers";



    public final static String SHIFT_URL    = String.format("%s/%s", BASE_URL, SHIFT_ROUTE);

    public final static String ALL_SHIFTS_URL   = String.format("%s/%s", BASE_URL, GET_ALL_SHIFTS);
    public final static String ALL_PROFILES_URL   = String.format("%s/%s", BASE_URL, GET_ALL_PROFILES);
    public final static String SHIFTS_FROM_SCHEDULE_URL   = String.format("%s/%s", BASE_URL, GET_SHIFTS_FROM_SCHEDULE);
    public final static String ALL_CONSTRAINTS_URL   = String.format("%s/%s", BASE_URL, GET_ALL_CONSTRAINTS);
    public final static String ALL_SHIFT_REQUESTS_URL   = String.format("%s/%s", BASE_URL, GET_ALL_SHIFT_REQUESTS);
    public final static String ADD_SHIFT_URL = String.format("%s/%s", BASE_URL, ADD_SHIFT_ROUTE);
    public final static String ADD_WORKER_TO_SHIFT_URL = String.format("%s/%s", BASE_URL, ADD_WORKER_TO_SHIFT_ROUTE);


    public static final String CREATE_JOB_URL =  String.format("%s/%s", BASE_URL,"jobs/");
    public static final String GET_JOB_URL =  String.format("%s/%s", BASE_URL,"jobs/user/");
}
