package com.example.myapplication.api;

import static com.example.myapplication.api.UsersApi.responseHandler;

import android.annotation.SuppressLint;

import com.example.myapplication.api.Requests.AuthedJsonArrayObjectRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

final public class ScheduleAPI {
    private static final Gson   gson               = new Gson();
    public static final  String BASE_URL           = String.format("%s/%s", Constants.BASE_URL,
                                                                   "schedules");
    public static final  String GET_USER_SCHEDULES = String.format("%s/%s", BASE_URL, "user" +
                                                                                      "/shifts");

    @SuppressLint("DefaultLocale")
    static public AuthedJsonArrayObjectRequest getSchedulesByUser(String userId, String token,
                                                                  int week, int day,
                                                                  @NotNull Api.PostCall<JSONArray> postCall) {


        return new AuthedJsonArrayObjectRequest(
                String.format("%s/%d/%d", GET_USER_SCHEDULES, week,
                              day), userId, token,
                res -> responseHandler(res, null, postCall),
                err -> responseHandler(null, err, postCall));


    }
    @SuppressLint("DefaultLocale")
    static public AuthedJsonArrayObjectRequest getSchedulesByUser(String userId, String token,
                                                                  int week,
                                                                  @NotNull Api.PostCall<JSONArray> postCall) {


        return new AuthedJsonArrayObjectRequest(
                String.format("%s/%d/", GET_USER_SCHEDULES, week), userId, token,
                res -> responseHandler(res, null, postCall),
                err -> responseHandler(null, err, postCall));


    }


}
