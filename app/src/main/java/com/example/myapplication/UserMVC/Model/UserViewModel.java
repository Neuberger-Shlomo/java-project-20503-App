package com.example.myapplication.UserMVC.Model;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.RoleLevel;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.UsersApi;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class UserViewModel extends AndroidViewModel {


    final static private Gson gson = new Gson();
    private final MutableLiveData<User> userState =
            new MutableLiveData<>(new User());
    private final RequestQueue          queue;

    public UserViewModel(@NotNull Application application) {
        super(application);
        queue = Volley.newRequestQueue(getApplication());
    }

    public LiveData<User> getUserState() {
        return userState;
    }

    public void login(
            @NotNull String username,
            @NotNull String password,
            @Nullable Api.PreCall preCall,
            @NotNull Api.PostCall<JSONObject> postCall) {
        this.queue.add(
                UsersApi.loginRequest(
                        username, password, preCall,
                        ((object, responseError, throwable) -> {
                            try {
                                this.onLoginResponse(object, responseError, throwable);
                            } catch (Exception e) {
                                postCall.onPostCall(null, null, e);
                            }
                            postCall.onPostCall(object, responseError, throwable);
                        })));

    }

    public void register(UsersApi.RegisterRequest registerRequest, Api.PreCall preCall,
                         Api.PostCall<Boolean> postCall) {

        preCall.onPreCall();
        queue.add(UsersApi.registerRequest(registerRequest, preCall, (jsonObject, responseError,
                                                                      throwable) -> {
            if (jsonObject != null)
                this.onRegisterResponse(registerRequest);
            postCall.onPostCall(jsonObject != null, responseError, throwable);
        }));

    }


    public void logout(@Nullable Api.PreCall preCall, @NotNull Api.PostCall<Boolean> postCall) {
        if (userState.getValue() == null) {
            postCall.onPostCall(null, null, new Exception("No user"));

        }
        queue.add(
                UsersApi.logoutRequest(
                        userState.getValue().getId(), userState.getValue().getAuthToken(), preCall,
                        (aBoolean, responseError, throwable) -> {
                            this.onLogoutResponse(aBoolean != null ? aBoolean : false,
                                                  responseError,
                                                  throwable);

                            postCall.onPostCall(aBoolean, responseError, throwable);

                        }));

    }


    private void onRegisterResponse(UsersApi.RegisterRequest registerRequest) {
        User user = getUserState().getValue();
        if (user == null) {
            user = new User();
        }
        user.setUsername(registerRequest.getUsername());
        userState.setValue(user);
    }

    private void onLogoutResponse(boolean result, Api.ResponseError error, Throwable throwable) {
        if (Boolean.TRUE.equals(result) || (error != null && error.getStatus() != 200) || throwable != null) {
            User u =
                    new User(Objects.requireNonNull(getUserState().getValue()));
            u.setAuthToken(null);
            userState.setValue(u);
        }

    }

    private void onLoginResponse(JSONObject jsonObject, Api.ResponseError error,
                                 Throwable throwable) throws Exception {
        User user = userState.getValue();
        if (user == null) {
            throw new Exception("No user data");
        }
        String jwt   = null;
        String id    = null;
        int    level = -1;
        if (jsonObject != null) {
            jwt   = jsonObject.getString("jwt");
            level = jsonObject.getInt("role");
            id    = jsonObject.getString("id");
        }
        if (jwt == null || level == -1) {
            throw new Exception("Invalid values");
        }

        User u = new User(user.getUsername(), user.getPassword(), jwt,
                          RoleLevel.values()[level]);
        u.setId(id);

        u.setProfile(Profile.fromJSON(jsonObject.getJSONObject("profile")));
        userState.setValue(u);
    }


}
