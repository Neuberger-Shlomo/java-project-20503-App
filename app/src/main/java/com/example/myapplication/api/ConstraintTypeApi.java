package com.example.myapplication.api;

import com.example.myapplication.api.Requests.AuthedJsonArrayObjectRequest;

import org.json.JSONArray;

/**
 * this class handle the api calls to manage user constraintsTypes
 * include call to get all constraintTypes
 */
public class ConstraintTypeApi {

    public final static String BASE_URL = String.format("%s/%s", Constants.BASE_URL,
            "constraint-types");
    public final static String GET_ALL_URL = String.format("%s/%s", BASE_URL, "");

    /**
     * get all constraints types.
     *
     * @param user     user id
     * @param token    the user token
     * @param postCall callback to handle response from the server
     * @return AuthedJsonArrayObjectRequest (request to get json array of constraintstypes)
     */
    public static AuthedJsonArrayObjectRequest getConstraintsTypes(String user, String token,
                                                                   Api.PostCall<JSONArray> postCall) {

        return new AuthedJsonArrayObjectRequest(
                GET_ALL_URL, user, token,
                response -> UsersApi.responseHandler(response, null, postCall),
                error -> UsersApi.responseHandler(null, error, postCall));
    }


}
