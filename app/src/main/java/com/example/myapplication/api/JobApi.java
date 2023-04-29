package com.example.myapplication.api;


import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.example.myapplication.Model.ScheduleJob;
import com.example.myapplication.api.Requests.AuthedJsonArrayObjectRequest;
import com.example.myapplication.api.Requests.AuthedJsonObjectRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JobApi handle api requests related to jobs.
 */
final public class JobApi {
    public static final  String BASE_URL       = String.format("%s/%s", Constants.BASE_URL, "jobs");
    public static final  String CREATE_JOB_URL = String.format("%s/%s", BASE_URL, "");
    public static final  String GET_JOB_URL    = String.format("%s/%s", BASE_URL, "user/");
    private static final Gson   gson           = new Gson();

    /**
     * requset from user to schedule a shift.
     *
     * @param userId       the user id
     * @param jwt          The user json web token
     * @param job          The shift the user want to schedule
     * @param preListener  callback before the request
     * @param postListener callback after the request
     * @return AuthedJsonObjectRequest (request to get json object of sceduled shift)
     */
    public static AuthedJsonObjectRequest requestSchedule(
            String userId,
            String jwt,
            ScheduleJob job,
            @Nullable Api.PreCall preListener,
            @NotNull Api.PostCall<JSONObject> postListener) {

        // Call the pre-call listener, if it's not null
        if (preListener != null) {
            preListener.onPreCall();
        }

        JSONObject jsonObject;

        try {
            //convert job to JSON
            jsonObject = new JSONObject(gson.toJson(job, ScheduleJob.class));
            jsonObject.put("uid", job.getId());
        } catch (JSONException e) {
            postListener.onPostCall(null, null, e);
            return null;
        }

        // create AuthedJsonObjectRequest
        return new AuthedJsonObjectRequest(
                Request.Method.POST,
                CREATE_JOB_URL,
                userId, jwt, jsonObject,
                res -> UsersApi.responseHandler(res, null, postListener),
                err -> UsersApi.responseHandler(null, err, postListener)
        );
    }

    /**
     * Request to get all jobs for a user.
     *
     * @param userId       The user's ID
     * @param jwt          The user's JSON Web Token for authentication
     * @param preListener  Callback to be called before the request
     * @param postListener Callback to be called after the request
     * @return AuthedJsonArrayObjectRequest to be added to the request queue
     */
    public static AuthedJsonArrayObjectRequest getAllJobs(
            String userId,
            String jwt,
            @Nullable Api.PreCall preListener,
            @NotNull Api.PostCall<JSONArray> postListener) {

        // call pre-call listener
        if (preListener != null) {
            preListener.onPreCall();
        }

        // create  AuthedJsonArrayObjectRequest
        return new AuthedJsonArrayObjectRequest(
                GET_JOB_URL,
                userId, jwt,

                //  handle the response from the server
                res -> UsersApi.responseHandler(res, null, postListener),
                // handle errors
                err -> UsersApi.responseHandler(null, err, postListener)
        );
    }
}
