package com.example.myapplication.api;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.api.Requests.AuthedJsonObjectRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

final public class UsersApi {

    private static final Gson gson = new Gson();
    public final static String BASE_URL = String.format("%s/%s", Constants.BASE_URL,"users");
    public final static String LOGIN_URL    = String.format("%s/%s", BASE_URL,"login");
    public final static String REGISTER_URL = String.format("%s/%s", BASE_URL, "register");

    public final static String LOGOUT_URL   = String.format("%s/%s", BASE_URL, "logout");


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

    public static AuthedJsonObjectRequest logoutRequest(String userId,
                                                        String jwt,
                                                        @NotNull Api.PostCall<Boolean> postListener
                                                       ) {
        return logoutRequest(userId, jwt, null, postListener);
    }

    public static AuthedJsonObjectRequest logoutRequest(String userId,
                                                        String jwt,
                                                        @NotNull Api.PostCall<Boolean> postListener,
                                                        @Nullable Api.PreCall preListener
                                                       ) {
        return logoutRequest(userId, jwt, preListener, postListener);
    }


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


    public static JsonObjectRequest loginRequest(
            @NotNull String username,
            @NotNull String password,
            @NotNull Api.PostCall<JSONObject> postCall,
            @Nullable Api.PreCall preCall) {
        return loginRequest(username, password, preCall, postCall);
    }

    public static JsonObjectRequest loginRequest(
            @NotNull String username,
            @NotNull String password,
            @NotNull Api.PostCall<JSONObject> postCall) {
        return loginRequest(username, password, null, postCall);
    }


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
}
