package com.example.myapplication.api;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.example.myapplication.Model.ScheduleJob;
import com.example.myapplication.api.Requests.AuthedJsonObjectRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

final public class JobApi {
    private static final Gson gson = new Gson();

    public static AuthedJsonObjectRequest requestSchedule(
            String userId,
            String jwt,
            ScheduleJob job,
            @Nullable Api.PreCall preListener,
            @NotNull Api.PostCall<JSONObject> postListener) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(gson.toJson(job,ScheduleJob.class));
            jsonObject.put("uid",job.getId());
        } catch (JSONException e) {
            postListener.onPostCall(null, null, e);
            return null;
        }

        return new AuthedJsonObjectRequest(
                Request.Method.POST,
                Constants.CREATE_JOB_URL,
                userId, jwt, jsonObject,
                res -> UsersApi.responseHandler(res, null, postListener),
                err -> UsersApi.responseHandler(null, err, postListener)
        );
    }


}
