package com.example.myapplication.api;

import com.google.gson.Gson;

final public class Constants {
    // In android emulator the route 10.0.2.2 is redirected to host 127.0.0.1 (aka localhost)
    public final static String BASE_URL       = "http://10.0.2.2:8080";
    public final static String USER_ROUTE     = "users";
    public final static String LOGIN_ROUTE    = USER_ROUTE + "/login";
    public final static String REGISTER_ROUTE = USER_ROUTE + "/signup";

    public final static String LOGOUT_ROUTE = USER_ROUTE + "/logout";
    public final static String LOGIN_URL      = String.format("%s/%s", BASE_URL, LOGIN_ROUTE);
    public final static String REGISTER_URL   = String.format("%s/%s", BASE_URL, REGISTER_ROUTE);
    public final static String LOGOUT_URL   = String.format("%s/%s", BASE_URL, LOGOUT_ROUTE);


}
