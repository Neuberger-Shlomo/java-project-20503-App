package com.example.myapplication.api;

import static com.example.myapplication.api.UsersApi.responseHandler;

import android.annotation.SuppressLint;

import com.example.myapplication.api.Requests.AuthedJsonArrayObjectRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

/**
 * ScheduleAPI handle requests related to sceduling shifts.
 */
final public class ScheduleAPI {
    public static final String BASE_URL           = String.format("%s/%s", Constants.BASE_URL,
                                                                  "schedules");
    public static final String GET_USER_SCHEDULES = String.format("%s/%s", BASE_URL, "user" +
                                                                                     "/shifts");

    /**
     * getSchedulesByUser retrieves schedules by user for a specific day of the week.
     *
     * @param userId   id of the user
     * @param token    token of the user
     * @param week     the week number
     * @param day      the day of the week
     * @param postCall callback after the server response
     * @return AuthedJsonObjectRequest (request to get json object of sceduled shift
     */
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

    /**
     * getSchedulesByUser retrieves schedules by user for a specific week.
     *
     * @param userId   id of the user
     * @param token    token of the user
     * @param week     week number
     * @param postCall callback after the server response
     * @return the request as AuthedJsonArrayObjectRequest
     */
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
