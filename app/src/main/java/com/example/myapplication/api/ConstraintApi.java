package com.example.myapplication.api;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.Model.ConstraintType;
import com.example.myapplication.Model.Constraints;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

final public class ConstraintApi {
    public final static String BASE_URL     = String.format("%s/%s", Constants.BASE_URL,
                                                            "constraints");
    public final static String GET_ALL_URL  = String.format("%s/%s", BASE_URL, "");
    public final static String POST_NEW_URL = GET_ALL_URL;

    public static JsonArrayRequest getConstraints(Api.PostCall<JSONArray> postCall) {
        return new JsonArrayRequest(
                GET_ALL_URL,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null, error, postCall));
    }


    public static JsonObjectRequest addConstraints(
            @NotNull Constraints constraints,
            Api.PostCall<JSONObject> postCall) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(new Gson().toJson(constraints));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return new JsonObjectRequest(
                Request.Method.POST, POST_NEW_URL, jsonObj,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null,error,postCall));
    }


}
