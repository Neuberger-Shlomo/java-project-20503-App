package com.example.myapplication.api;

import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class ConstraintTypeApi {
    public final static String BASE_URL    = String.format("%s/%s", Constants.BASE_URL,
                                                           "constraint-types");
    public final static String GET_ALL_URL = String.format("%s/%s", BASE_URL, "");

    public static JsonArrayRequest getConstraintsTypes(Api.PostCall<JSONArray> postCall) {
        return new JsonArrayRequest(
                GET_ALL_URL,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null, error, postCall));
    }


}
