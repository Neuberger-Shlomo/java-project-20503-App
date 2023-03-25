package com.example.myapplication.api;

import java.net.HttpURLConnection;
import java.net.URL;

public class UserApiCalls {
    final static private String BASE_URL =Constants.BASE_URL+ Constants.USER_ROUTE;
    String CreateUser(){
        /* Call TO BASE_URL+"/users" with POST */

        try{
            URL url = new URL(BASE_URL);
            HttpURLConnection client;
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("Name","Shlomo");
            client.setRequestProperty("username","Simoin Peres");
            client.setDoOutput(true);
        }catch (Exception exception) {
        }

        return "";
    }


    String Login(){
        return  "";
    }


}
