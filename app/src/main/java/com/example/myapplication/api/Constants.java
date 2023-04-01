package com.example.myapplication.api;

import com.google.gson.Gson;

final public  class Constants {
    // In android emulator the route 10.0.2.2 is redirected to host 127.0.0.1 (aka localhost)
    public final static String BASE_URL = "http://10.0.2.2:8080";
    public final static String USER_ROUTE = "/users/";
    public final static String LOGIN_ROUTE = "/login/";
    public final static String LOGIN_URL = BASE_URL + Constants.LOGIN_ROUTE;



}
