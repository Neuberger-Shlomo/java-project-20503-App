/**
 * ShiftRequestViewModel - current representation of the shift request
 * act as bridge between the ShiftRequest in the db and the controllers that handle shift requests
 */


package com.example.myapplication.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.Constraints;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.Model.ShiftRequest;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.Constants;
import com.example.myapplication.api.Requests.AuthedJsonArrayObjectRequest;
import com.example.myapplication.api.Requests.AuthedJsonObjectRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ShiftRequestViewModel extends AndroidViewModel {


    // shiftRequestState= arraylist that represents the current state of shift requests
    private final MutableLiveData<ArrayList<ShiftRequest>> shiftRequestState =
            new MutableLiveData<ArrayList<ShiftRequest>>(new ArrayList<ShiftRequest>());
    //queue of requests to the server
    private final RequestQueue queue;

    // to later convert data to json
    final static private Gson gson = new Gson();


    public ShiftRequestViewModel(@NonNull Application application) {
        super(application);
        //initializes the queue instance variable
        queue = Volley.newRequestQueue(getApplication());
    }

    public LiveData<ArrayList<ShiftRequest>> getShiftRequestState() {
        return shiftRequestState;
    }


    /**
     * get the shift requests from server
     * @param userId  the user id
     * @param token  token of the user
     * @param postCall for after the server response
     */
    public void getData(String userId,
                        String token,
                        @NotNull Api.PostCall<ArrayList<ShiftRequest>> postCall) {

        //  get shift requests from the server
        queue.add(getShiftRequest(userId, token, () -> {
                },
                (jsonArray, responseError, throwable) -> {
                    try {
                        if (jsonArray != null) {
                            // convert from JSONArray to ArrayList of ShiftRequests
                            ArrayList<ShiftRequest> arrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                arrayList.add(ShiftRequest.fromJSON(jsonObject));
                            }

                            // set shiftRequestState to  ArrayList (of ShiftRequests)
                            shiftRequestState.setValue(arrayList);
                            //  callback with the ArrayList
                            postCall.onPostCall(arrayList, null, null);
                        } else {
                            // callback with responseError and throwable
                            postCall.onPostCall(null, responseError, throwable);
                        }
                    } catch (JSONException e) {
                        // callback with the JSONException
                        postCall.onPostCall(null, null, e);
                    }
                }));
    }


    /**
     *  get shift requests from the server
     * @param userId  ID of the user
     * @param token   token of the user
     * @param preCall callback before the request sent
     * @param postCall callback  after server response
     */
    private static AuthedJsonArrayObjectRequest getShiftRequest(String userId, String token,
                                                                @NotNull Api.PreCall preCall,
                                                                @NotNull Api.PostCall<JSONArray> postCall) {
        preCall.onPreCall();
        return new AuthedJsonArrayObjectRequest(Constants.ALL_SHIFT_REQUESTS_URL,
                userId,
                token,
                res -> {
                    try {
                        postCall.onPostCall(res, null, null);
                    } catch (Exception e) {
                        postCall.onPostCall(null, null, e);
                    }
                },
                err -> {
                    if (err.networkResponse != null && err.networkResponse.data != null) {
                        String resString = new String(err.networkResponse.data, StandardCharsets.UTF_8);
                        postCall.onPostCall(null, gson.fromJson(resString, Api.ResponseError

                                .class), null);
                    } else {
                        postCall.onPostCall(null, null, err);
                    }
                });
    }

    /**
     * add shift request to the server as json object
     * @param shiftRequest  ShiftRequest we want to add
     * @param userId  ID of the user
     * @param token   token of the user
     * @param preCall callback before the request sent
     * @param postCall callback  after server response
     */
    public void addShiftRequest(ShiftRequest shiftRequest, String userId, String token, Api.PreCall preCall,
                                Api.PostCall<Boolean> postCall) {

        // preCall = callback before the request sent
        preCall.onPreCall();
        //   add shift request to the server
        queue.add(addUserShiftRequest(shiftRequest, userId, token, preCall, (jsonObject, responseError,
                                                                             throwable) -> {
            if (responseError != null || throwable != null) {
                postCall.onPostCall(null, responseError, throwable);
                return;
            }
            // get the current state of shift requests
            ArrayList<ShiftRequest> s = getShiftRequestState().getValue();
            if (s == null) {
                // postCall with exception (if null)
                postCall.onPostCall(null, null, new Exception("No user error"));
                return;
            }
            // add new shift request to the state
            // also set the LiveData to the new state
            s.add(shiftRequest);
            shiftRequestState.setValue(s);
            //  postCall callback to indicate sucsessful
            postCall.onPostCall(true, null, null);
        }));
    }


    /**
     * request to add shift request to the server as json object
     * @param shiftRequest  ShiftRequest we want to add
     * @param userId  ID of the user
     * @param token   token of the user
     * @param preCall callback before the request sent
     * @param postCall callback  after server response
     * @return JsonObjectRequest=  json represents the shift request
     */
    private JsonObjectRequest addUserShiftRequest(ShiftRequest shiftRequest, String userId, String token,
                                                  Api.PreCall preCall,
                                                  Api.PostCall<JSONObject> postCall) {
        //  preCall = callback before sending request
        preCall.onPreCall();

        JSONObject jsonObj;
        try {
            // convert ShiftRequest to JSON object
            jsonObj = new JSONObject(gson.toJson(shiftRequest));
        } catch (JSONException e) {
            postCall.onPostCall(null, null, e);
            return null;
        }

        // add shift request to server
        return new AuthedJsonObjectRequest(Request.Method.POST, Constants.ADD_SHIFT_REQUEST_URL, userId, token, jsonObj, res -> {
            try {
                //  postCall  with JSONObject
                postCall.onPostCall(res, null, null);
            } catch (Exception e) {
                //   callback with Exception
                postCall.onPostCall(null, null, e);
            }
        }, err -> {
            if (err.networkResponse != null && err.networkResponse.data != null) {
                String resString = new String(err.networkResponse.data, StandardCharsets.UTF_8);
                //   callback with error
                postCall.onPostCall(null, gson.fromJson(resString, Api.ResponseError.class), null);
            } else {
                //  callback with the Throwable
                postCall.onPostCall(null, null, err);
            }
        });
    }
}