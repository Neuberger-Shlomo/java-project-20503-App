package com.example.myapplication.api;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.api.Requests.AuthedJsonObjectRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

final public class UsersApi {

    private static final Gson gson = new Gson();


    public static AuthedJsonObjectRequest logoutRequest(String userId,
                                                        String jwt,
                                                        @Nullable Api.PreCall preListener,
                                                        @NotNull Api.PostCall<Boolean> postListener) {
        if (preListener != null) {
            preListener.onPreCall();
        }

        return new AuthedJsonObjectRequest(
                Constants.LOGOUT_URL,
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
                Constants.LOGIN_URL,
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


    public static JsonObjectRequest registerRequest(Api.RegisterRequest registerRequest,
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

        return new JsonObjectRequest(Request.Method.POST, Constants.REGISTER_URL, jsonObj,
                                     res -> responseHandler(res, null, postCall),
                                     err -> responseHandler(null, err, postCall));
    }


    private static <T> void responseHandler(
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
