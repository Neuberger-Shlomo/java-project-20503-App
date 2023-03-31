package com.example.myapplication.api;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

final public class UserApiCalls {
    final static private String TAG = "UserApiCalls";
    final static private String BASE_URL = Constants.BASE_URL + Constants.USER_ROUTE;
    final static private String LOGIN_URL = Constants.BASE_URL + Constants.LOGIN_ROUTE;
    final static private Gson gson = new Gson();


    public static void Login(RequestQueue queue,
                             String username, String password,
                             Api.PreCall preCall,
                             Api.PostCall<JSONObject> postCall) {
        preCall.onPreCall();

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("username", username);
            jsonObj.put("password", password);
        } catch (JSONException e) {
            postCall.onException(e);
            return;
        }
        queue.add(new JsonObjectRequest(Request.Method.POST, LOGIN_URL, jsonObj, res -> {
            try {
                postCall.onPostCall(res, null);
            } catch (Exception e) {
                postCall.onException(e);
            }
        },
                err -> {
                    String resString = new String(err.networkResponse.data, StandardCharsets.UTF_8);
                    postCall.onPostCall(null, gson.fromJson(resString, Api.ResponseError.class));
                }
        ));

    }


}
