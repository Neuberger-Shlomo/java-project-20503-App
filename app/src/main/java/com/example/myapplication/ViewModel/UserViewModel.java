package com.example.myapplication.ViewModel;

import android.app.AlertDialog;
import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.Constants;
import com.example.myapplication.Model.BasicUser;
import com.example.myapplication.Model.RoleLevel;
import com.example.myapplication.api.Requests.AuthedJsonObjectRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UserViewModel extends AndroidViewModel {


    private final MutableLiveData<BasicUser> userState =
            new MutableLiveData<>(new BasicUser());
    private final RequestQueue               queue;

    final static private Gson gson = new Gson();

    public UserViewModel(@NotNull Application application) {
        super(application);
        queue = Volley.newRequestQueue(getApplication());
    }

    public LiveData<BasicUser> getUserState() {
        return userState;
    }

    public void login(String username, String password, Api.PreCall preCall,
                      Api.PostCall<JSONObject> postCall) {
        this.queue.add(loginRequest(username, password, preCall, (jsonObject, responseError,
                                                                  throwable) -> {
            if (responseError != null || throwable != null) {
                postCall.onPostCall(null, responseError, throwable);
                return;
            }
            BasicUser user = getUserState().getValue();
            if (user == null) {
                postCall.onPostCall(null, null, new Exception("No user error"));
                return;
            }
            String jwt   = null;
            String id    = null;
            int    level = -1;
            try {
                if (jsonObject != null) {
                    jwt   = jsonObject.getString("jwt");
                    level = jsonObject.getInt("role");
                    id    = jsonObject.getString("id");
                }
                if (jwt == null || level == -1) {
                    throw new Exception("Invalid values");
                }
            } catch (Exception e) {
                postCall.onPostCall(null, null, e);
            }
            BasicUser u = new BasicUser(username, password, jwt, RoleLevel.values()[level]);
            u.setId(id);
            userState.setValue(u);
        }));
    }

    public void register(Api.RegisterRequest registerRequest, Api.PreCall preCall,
                         Api.PostCall<Boolean> postCall) {

        preCall.onPreCall();
        queue.add(registerRequest(registerRequest, preCall, (jsonObject, responseError,
                                                             throwable) -> {
            if (responseError != null || throwable != null) {
                postCall.onPostCall(null, responseError, throwable);
                return;
            }
            BasicUser user = getUserState().getValue();
            if (user == null) {
                postCall.onPostCall(null, null, new Exception("No user error"));
                return;
            }
            user.setUsername(registerRequest.getUsername());
            userState.setValue(user);
            postCall.onPostCall(true, null, null);
        }));

    }

    public void logout(@Nullable Api.PreCall preCall, @Nullable Api.PostCall<Boolean> postCall) throws Exception {
        if(userState.getValue() == null) {
            if(postCall != null) postCall.onPostCall(null,null, new Exception("No user"));
            else throw new Exception("No user");
        }
        queue.add(logoutRequest(userState.getValue().getId(), userState.getValue().getAuthToken()
                , preCall, (aBoolean, responseError, throwable) -> {

                    if (Boolean.TRUE.equals(aBoolean) || (responseError != null && responseError.getStatus() != 200) || throwable != null) {
                        BasicUser u =
                                new BasicUser(Objects.requireNonNull(getUserState().getValue()));
                        u.setAuthToken(null);
                        userState.setValue(u);
                    }
                    if (postCall != null) {
                        postCall.onPostCall(aBoolean, responseError, throwable);
                    }
                }));

    }

    private JsonObjectRequest loginRequest(String username, String password, Api.PreCall preCall,
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
            if (err.networkResponse != null && err.networkResponse.data != null) {
                String resString = new String(err.networkResponse.data, StandardCharsets.UTF_8);
                postCall.onPostCall(null, gson.fromJson(resString, Api.ResponseError.class), null);
            } else {
                postCall.onPostCall(null, null, err);
            }
        });
    }

    private JsonObjectRequest registerRequest(Api.RegisterRequest registerRequest,
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

        return new JsonObjectRequest(Request.Method.POST, Constants.REGISTER_URL, jsonObj, res -> {
            try {
                postCall.onPostCall(res, null, null);
            } catch (Exception e) {
                postCall.onPostCall(null, null, e);
            }
        }, err -> {
            if (err.networkResponse != null && err.networkResponse.data != null) {
                String resString = new String(err.networkResponse.data, StandardCharsets.UTF_8);
                postCall.onPostCall(null, gson.fromJson(resString, Api.ResponseError.class), null);
            } else {
                postCall.onPostCall(null, null, err);
            }
        });
    }

    private AuthedJsonObjectRequest logoutRequest(String userId, String jwt,
                                                  @Nullable Api.PreCall preCall,
                                                  @Nullable Api.PostCall<Boolean> postCall) {
        if (preCall != null) {
            preCall.onPreCall();
        }

        JSONObject jsonObj;


        return new AuthedJsonObjectRequest(Constants.LOGOUT_URL, userId, jwt, response -> {
            if (postCall != null) {
                postCall.onPostCall(true, null, null);
            }
        }, err -> {
            if (err.networkResponse != null && err.networkResponse.data != null) {
                String resString = new String(err.networkResponse.data, StandardCharsets.UTF_8);
                if (postCall != null) {
                    postCall.onPostCall(null, gson.fromJson(resString, Api.ResponseError.class),
                                        null);
                }
            } else {
                if (postCall != null) {
                    postCall.onPostCall(null, null, err);
                }
            }
        });
    }
}
