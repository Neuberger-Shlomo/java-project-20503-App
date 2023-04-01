package com.example.myapplication.ViewModel;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.Constants;
import com.example.myapplication.user.BasicUser;
import com.example.myapplication.user.RoleLevel;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UserViewModel extends AndroidViewModel {

    private RequestQueue restQueue;
    private final MutableLiveData<BasicUser> uiState =
            new MutableLiveData<BasicUser>(new BasicUser());
    private RequestQueue queue;

    final static private Gson gson = new Gson();

    public UserViewModel(@NotNull Application application) {
        super(application);
        queue = Volley.newRequestQueue(getApplication());
    }

    public LiveData<BasicUser> getUiState() {
        return uiState;
    }

    public void login(String username, String password, Api.PreCall preCall,
                      Api.PostCall<JSONObject> postCall) {
        this.queue.add(loginRequest(username, password, preCall, (jsonObject, responseError,
                                                                  throwable) -> {
            BasicUser user = getUiState().getValue();
            if (user == null) {
                postCall.onPostCall(null, null, new Exception("No user error"));
                return;
            }
            String jwt = null;
            int level = -1;
            try {
                if (jsonObject != null) {
                    jwt = jsonObject.getString("jwt");
                    level = jsonObject.getInt("role");
                }
                if (jwt == null || level == -1) {
                    throw new Exception("Invalid values");
                }
            } catch (Exception e) {
                postCall.onPostCall(null, null, e);
            }
            uiState.setValue(new BasicUser(username, password, jwt, RoleLevel.values()[level]));

        }));
    }


    private static JsonObjectRequest loginRequest(String username, String password,
                                                  Api.PreCall preCall,
                                                  Api.PostCall<JSONObject> postCall) {
        preCall.onPreCall();

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("username", username);
            jsonObj.put("password", password);
        } catch (JSONException e) {
            postCall.onPostCall(null, null, e);
            return null;
        }

        return new JsonObjectRequest(Request.Method.POST, Constants.LOGIN_URL, jsonObj, res -> {
            try {
                postCall.onPostCall(res, null, null);
            } catch (Exception e) {
                postCall.onPostCall(null, null, e);
            }
        }, err -> {
            String resString = new String(err.networkResponse.data, StandardCharsets.UTF_8);
            postCall.onPostCall(null, gson.fromJson(resString, Api.ResponseError.class), null);
        });
    }
}
