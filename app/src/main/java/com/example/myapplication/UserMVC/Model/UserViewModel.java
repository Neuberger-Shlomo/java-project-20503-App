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

/**
 * ShiftRequestViewModel - current representation of the user
 * act as bridge between the user in the db and the controllers that handle user
 */
public class UserViewModel extends AndroidViewModel {

    // to later convert data to json
    final static private Gson         gson = new Gson();
    //queue of requests to the server
    // shiftRequestState= arraylist that represents the current state of user
    private final MutableLiveData<User> userState =
            new MutableLiveData<>(new User());
    private final        RequestQueue queue;

    public UserViewModel(@NotNull Application application) {
        super(application);
        //initializes the queue instance variable
        queue = Volley.newRequestQueue(getApplication());
    }

    /**
     * @return LiveData = that represent the userState
     */
    public LiveData<User> getUserState() {
        return userState;
    }

    /**
     * Sends a login request to the server.
     *
     * @param username the user name
     * @param password callback function before sending the request
     * @param preCall  callback function after sending the request
     */
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

    /**
     * send register request to server.
     *
     * @param registerRequest register request we want to send
     * @param preCall         callback function  before sending request
     * @param postCall        callback function after sending the request
     */

    public void register(UsersApi.RegisterRequest registerRequest, Api.PreCall preCall,
                         Api.PostCall<Boolean> postCall) {
        // preCall = callback before the request sent
        preCall.onPreCall();
        //   add user request to the server
        queue.add(UsersApi.registerRequest(registerRequest, preCall, (jsonObject, responseError,
                                                                      throwable) -> {
            // postCall with exception (if null)
            if (jsonObject != null)
                this.onRegisterResponse(registerRequest);
            postCall.onPostCall(jsonObject != null, responseError, throwable);
        }));

    }

    /**
     * Sends a logout request to the server.
     *
     * @param preCall  callback function  before sending request
     * @param postCall callback function after sending the request
     */
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

    /**
     * Sets the user's registration information in the userState.
     *
     * @param registerRequest A RegisterRequest object containing the user's registration
     *                        information
     */

    private void onRegisterResponse(UsersApi.RegisterRequest registerRequest) {
        User user = getUserState().getValue();
        if (user == null) {
            user = new User();
        }
        user.setUsername(registerRequest.getUsername());
        userState.setValue(user);
    }

    /**
     * Sets the userState to a new User object with an authToken of null if the logout request
     * was successful.
     *
     * @param result    A boolean value representing whether the logout was successful
     * @param error     A ResponseError object containing information about the error (if there
     *                  was one)
     * @param throwable An exception that was thrown (if there was one)
     */
    private void onLogoutResponse(boolean result, Api.ResponseError error, Throwable throwable) {
        if (Boolean.TRUE.equals(result) || (error != null && error.getStatus() != 200) || throwable != null) {
            User u =
                    new User(Objects.requireNonNull(getUserState().getValue()));
            u.setAuthToken(null);
            userState.setValue(u);
        }

    }

    /**
     * Sets the userState to a new User object with the login information if the login request
     * was successful.
     *
     * @param jsonObject JSONObject with the user login date
     * @param error      ResponseError with error data
     * @param throwable  the exception that was thrown
     * @throws JSONException exception if json not valid
     * @throws Exception     exception if login data not valid
     */
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
