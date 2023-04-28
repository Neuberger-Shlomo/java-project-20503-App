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

final public class JobApi {
    private static final Gson   gson           = new Gson();
    public static final  String BASE_URL       = String.format("%s/%s", Constants.BASE_URL, "jobs");
    public static final  String CREATE_JOB_URL = String.format("%s/%s", BASE_URL, "");
    public static final  String GET_JOB_URL    = String.format("%s/%s", BASE_URL, "user/");


    public static AuthedJsonObjectRequest requestSchedule(
            String userId,
            String jwt,
            ScheduleJob job,
            @Nullable Api.PreCall preListener,
            @NotNull Api.PostCall<JSONObject> postListener) {
        if (preListener != null) {
            preListener.onPreCall();
        }
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(gson.toJson(job, ScheduleJob.class));
            jsonObject.put("uid", job.getId());
        } catch (JSONException e) {
            postListener.onPostCall(null, null, e);
            return null;
        }

        return new AuthedJsonObjectRequest(
                Request.Method.POST,
                CREATE_JOB_URL,
                userId, jwt, jsonObject,
                res -> UsersApi.responseHandler(res, null, postListener),
                err -> UsersApi.responseHandler(null, err, postListener)
        );
    }


    public static AuthedJsonArrayObjectRequest getAllJobs(
            String userId,
            String jwt,
            @Nullable Api.PreCall preListener,
            @NotNull Api.PostCall<JSONArray> postListener) {
        if (preListener != null) {
            preListener.onPreCall();
        }

        return new AuthedJsonArrayObjectRequest(
                GET_JOB_URL,
                userId, jwt,
                res -> UsersApi.responseHandler(res, null, postListener),
                err -> UsersApi.responseHandler(null, err, postListener)
        );
    }


}
