package com.example.myapplication.api;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.Model.ConstraintType;
import com.example.myapplication.Model.Constraints;
import com.example.myapplication.api.Requests.AuthedJsonArrayObjectRequest;
import com.example.myapplication.api.Requests.AuthedJsonObjectRequest;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


final public class ConstraintApi {
    public final static String BASE_URL        = String.format("%s/%s", Constants.BASE_URL,
                                                               "constraints/user");
    public final static String GET_ALL_URL     = String.format("%s/%s", BASE_URL, "");
    public final static String GET_BY_WEEK_URL = String.format("%s/%s", BASE_URL, "range/");
    public final static String POST_NEW_URL    = String.format("%s/%s", BASE_URL, "");
    public final static String DELETE_URL      = String.format("%s/%s", BASE_URL, "");

//    public static JsonArrayRequest getConstraints(Api.PostCall<JSONArray> postCall) {
//        return new JsonArrayRequest(
//                GET_ALL_URL,
//                response -> UsersApi.responseHandler(response, null, postCall),
//                error -> UsersApi.responseHandler(null, error, postCall));
//    }

    public static JsonArrayRequest getConstraintsByDate(String userId, String token, int start,
                                                        int end,
                                                        Api.PostCall<JSONArray> postCall) {
        return new AuthedJsonArrayObjectRequest(
                GET_BY_WEEK_URL + start + "/" + end + "/date",
                userId, token,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null, error, postCall));
    }

    public static JsonArrayRequest getConstraintsByWeek(String userId, String token, int start,
                                                        int end,
                                                        Api.PostCall<JSONArray> postCall) {
        return new AuthedJsonArrayObjectRequest(
                GET_BY_WEEK_URL + start + "/" + end,
                userId, token,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null, error, postCall));
    }

    public static AuthedJsonObjectRequest deleteConstraint(String userId, String token, int id,
                                                           Api.PostCall<JSONObject> postCall) {
        return new AuthedJsonObjectRequest(
                Request.Method.DELETE,
                DELETE_URL + id,
                userId, token,
                null,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null, error, postCall));
    }

    public static AuthedJsonObjectRequest addConstraints(
            String uid,String token,
            @NotNull Constraints constraints,
            Api.PostCall<JSONObject> postCall) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(new Gson().toJson(constraints));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return new AuthedJsonObjectRequest(
                Request.Method.POST, POST_NEW_URL,uid,token, jsonObj,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null, error, postCall));
    }


}
