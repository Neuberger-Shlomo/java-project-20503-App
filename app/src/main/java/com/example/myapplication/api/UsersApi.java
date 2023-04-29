package com.example.myapplication.api;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.api.Requests.AuthedJsonArrayObjectRequest;
import com.example.myapplication.api.Requests.AuthedJsonObjectRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * JobApi handle API requests related to users
 * enabling user login, registration, logout,
 * promotion, demotion, and retrieval of user information by ID.
 */
final public class UsersApi {

    public final static  String BASE_URL          = String.format("%s/%s", Constants.BASE_URL,
                                                                  "users");
    public final static  String GET_BY_ID_URL     = String.format("%s/%s", BASE_URL, "users-id");
    public final static  String PROMOTE_BY_ID_URL = String.format("%s/%s", BASE_URL, "add-admin");
    public final static  String DEMOTE_BY_ID_URL  = String.format("%s/%s", BASE_URL, "remove" +
                                                                                     "-admin");
    public final static  String LOGIN_URL         = String.format("%s/%s", BASE_URL, "login");
    public final static  String REGISTER_URL      = String.format("%s/%s", BASE_URL, "signup");
    public final static String LOGOUT_URL = String.format("%s/%s", BASE_URL, "logout");
    private static final Gson   gson              = new Gson(); // Gson object for JSON manipulation

    /**
     * create a logout request
     *
     * @param userId       user id
     * @param jwt          user token
     * @param preListener  callback before the request
     * @param postListener callback after the request
     * @return AuthedJsonObjectRequest (request to get json object of user)
     */
    public static AuthedJsonObjectRequest logoutRequest(String userId,
                                                        String jwt,
                                                        @Nullable Api.PreCall preListener,
                                                        @NotNull Api.PostCall<Boolean> postListener) {
        if (preListener != null) {
            preListener.onPreCall();
        }

        return new AuthedJsonObjectRequest(
                LOGOUT_URL,
                userId,
                jwt,
                response -> responseHandler(true, null, postListener),
                err -> responseHandler(null, err, postListener));
    }

    /**
     * This method creates a login request
     *
     * @param username username
     * @param password user password
     * @param preCall  callback before request
     * @param postCall callback after request
     * @return AuthedJsonObjectRequest (request to get json object of user)
     */
    public static JsonObjectRequest loginRequest(
            @NotNull String username,
            @NotNull String password,
            @Nullable Api.PreCall preCall,
            @NotNull Api.PostCall<JSONObject> postCall) {
        if (preCall != null)
            preCall.onPreCall();

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("username", username);
            jsonObj.put("password", password);
        } catch (JSONException e) {
            postCall.onPostCall(null, null, e);
            return null;
        }

        return new JsonObjectRequest(
                Request.Method.POST,
                LOGIN_URL,
                jsonObj,
                res -> responseHandler(res, null, postCall),
                err -> responseHandler(null, err, postCall)
        );
    }

    /**
     * creates a register request
     *
     * @param registerRequest the RegisterRequest with the user information
     * @param preCall         callback before request
     * @param postCall        callback after request
     * @return AuthedJsonObjectRequest (request to get json object of user)
     */
    public static JsonObjectRequest registerRequest(RegisterRequest registerRequest,
                                                    Api.PreCall preCall,
                                                    Api.PostCall<JSONObject> postCall) {
        preCall.onPreCall();

        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(gson.toJson(registerRequest));
        } catch (JSONException e) {
            postCall.onPostCall(null, null, e);
            return null;
        }

        return new JsonObjectRequest(Request.Method.POST, REGISTER_URL, jsonObj,
                                     res -> responseHandler(res, null, postCall),
                                     err -> responseHandler(null, err, postCall));
    }

    /**
     * This method handles the response from the API request
     *
     * @param res      the result of the api request
     * @param err      the error of the api request
     * @param postCall callback after request
     */
    public static <T> void responseHandler(
            @Nullable T res,
            @Nullable VolleyError err,
            @NotNull Api.PostCall<T> postCall) {
        if (res != null) {
            try {
                postCall.onPostCall(res, null, null);
            } catch (Exception e) {
                postCall.onPostCall(null, null, e);
            }
        } else if (err != null) {
            if (err.networkResponse != null && err.networkResponse.data != null) {
                String resString = new String(err.networkResponse.data, StandardCharsets.UTF_8);
                postCall.onPostCall(null, gson.fromJson(resString, Api.ResponseError.class), null);
            } else {
                postCall.onPostCall(null, null, err);
            }
        } else {
            postCall.onPostCall(null, null, new Exception("No response"));
        }


    }

    /**
     * This method retrieves a user by their ID
     *
     * @param userId       user id
     * @param jwt          user token
     * @param targetID     user id to get
     * @param preListener  callback before request
     * @param postListener callback after request
     * @return AuthedJsonObjectRequest (request to get json object of user)
     */
    public static AuthedJsonArrayObjectRequest getUserById(
            String userId,
            String jwt,
            String targetID,
            @Nullable Api.PreCall preListener,
            @NotNull Api.PostCall<JSONArray> postListener) {
        if (preListener != null) {
            preListener.onPreCall();
        }
        return new AuthedJsonArrayObjectRequest(
                String.format("%s/%s", GET_BY_ID_URL, targetID),
                userId, jwt,
                res -> UsersApi.responseHandler(res, null, postListener),
                err -> UsersApi.responseHandler(null, err, postListener)
        );
    }

    /**
     * promotes user to manager
     *
     * @param userId       user id
     * @param jwt          user token
     * @param targetID     user id to get
     * @param preListener  callback before request
     * @param postListener callback after request
     * @return AuthedJsonObjectRequest (request to get json object of user)
     */
    public static AuthedJsonObjectRequest promoteUserByID(
            String userId,
            String jwt,
            String targetID,
            @Nullable Api.PreCall preListener,
            @NotNull Api.PostCall<JSONObject> postListener) {
        if (preListener != null) {
            preListener.onPreCall();
        }
        return new AuthedJsonObjectRequest(
                Request.Method.POST,
                String.format("%s/%s", PROMOTE_BY_ID_URL, targetID),
                userId, jwt,
                null,
                res -> UsersApi.responseHandler(res, null, postListener),
                err -> UsersApi.responseHandler(null, err, postListener)
        );
    }

    /**
     * demotes a manager to user
     *
     * @param userId       user id
     * @param jwt          user token
     * @param targetID     user id to get
     * @param preListener  callback before request
     * @param postListener callback after request
     * @return AuthedJsonObjectRequest (request to get json object of user)
     */
    public static AuthedJsonObjectRequest demoteUserByID(
            String userId,
            String jwt,
            String targetID,
            @Nullable Api.PreCall preListener,
            @NotNull Api.PostCall<JSONObject> postListener) {
        if (preListener != null) {
            preListener.onPreCall();
        }
        return new AuthedJsonObjectRequest(
                Request.Method.DELETE,
                String.format("%s/%s", DEMOTE_BY_ID_URL, targetID),
                userId, jwt,
                null,
                res -> UsersApi.responseHandler(res, null, postListener),
                err -> UsersApi.responseHandler(null, err, postListener)
        );
    }

    /**
     * register request with all the user data
     */
    public static class RegisterRequest {
        String firstName;
        String lastName;
        String email;
        String phoneNumber;
        String username;
        String password;

        public RegisterRequest() {
        }

        public RegisterRequest(String firstName, String lastName, String email,
                               String phoneNumber, String username, String password) {
            this.firstName   = firstName;
            this.lastName    = lastName;
            this.email       = email;
            this.phoneNumber = phoneNumber;
            this.username    = username;
            this.password    = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
