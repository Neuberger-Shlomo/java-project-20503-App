package com.example.myapplication.api;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myapplication.Model.Constraints;
import com.example.myapplication.api.Requests.AuthedJsonArrayObjectRequest;
import com.example.myapplication.api.Requests.AuthedJsonObjectRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * this class handle the api calls to manage user constraints
 * include calls to get constraints by date/by week, and add/delete constraints
 */
final public class ConstraintApi {
    //
    public final static String BASE_URL        = String.format("%s/%s", Constants.BASE_URL,
                                                               "constraints/user");
    public final static String GET_ALL_URL     = String.format("%s/%s", BASE_URL, "");
    public final static String GET_BY_WEEK_URL = String.format("%s/%s", BASE_URL, "range/");
    public final static String POST_NEW_URL    = String.format("%s/%s", BASE_URL, "");
    public final static String DELETE_URL      = String.format("%s/%s", BASE_URL, "");

    /**
     * getConstraintsByDate return constraints given date range.
     *
     * @param userId   the user id
     * @param token    the user login token
     * @param postCall callback to handle response from the server
     * @param start    the start day of date
     * @param end      the end day of date
     * @return AuthedJsonObjectRequest (request to get json object of constraints)
     */
    public static JsonArrayRequest getConstraintsByDate(String userId, String token, int start,
                                                        int end,
                                                        Api.PostCall<JSONArray> postCall) {
        return new AuthedJsonArrayObjectRequest(
                GET_BY_WEEK_URL + start + "/" + end + "/date",
                userId, token,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null, error, postCall));
    }

    /**
     * getConstraintsByWeek return constraints for a specific week.
     *
     * @param userId   the user id
     * @param token    the user login token
     * @param postCall callback to handle response from the server
     * @param start    the start day of the week
     * @param end      the end day of the week
     * @return AuthedJsonObjectRequest (request to get json object of constraints)
     */
    public static JsonArrayRequest getConstraintsByWeek(String userId, String token, int start,
                                                        int end,
                                                        Api.PostCall<JSONArray> postCall) {
        return new AuthedJsonArrayObjectRequest(
                GET_BY_WEEK_URL + start + "/" + end,
                userId, token,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null, error, postCall));
    }

    /**
     * deleteConstraint remove specific constraint.
     *
     * @param userId   the id of the user
     * @param token    the user's access token
     * @param id       the id of the constraint we to remove
     * @param postCall callback to handle response from the server
     * @return AuthedJsonObjectRequest (request to get json object of constraints)
     */
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

    /**
     * add new constraints.
     *
     * @param uid         the user id
     * @param token       the user login token
     * @param constraints the Constraints object we need to add
     * @param postCall    callback to handle response from the server
     * @return AuthedJsonObjectRequest (request to get json object request to add constraints)
     */
    public static AuthedJsonObjectRequest addConstraints(
            String uid, String token,
            @NotNull Constraints constraints,
            Api.PostCall<JSONObject> postCall) {
        // create json object from constraints
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(new Gson().toJson(constraints));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        // return authenticated json object request
        return new AuthedJsonObjectRequest(
                Request.Method.POST, POST_NEW_URL, uid, token, jsonObj,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null, error, postCall));
    }


}

