/**
 * ShiftsViewModel - current representation of the shifts
 * act as bridge between the Shift in the db and the controllers that handle shifts
 */

package com.example.myapplication.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;
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

public class ShiftsViewModel extends AndroidViewModel {

    // shiftsState = arraylist that represents the current state of shifts
    private final MutableLiveData<ArrayList<Shift>> shiftsState =
            new MutableLiveData<ArrayList<Shift>>(new ArrayList<Shift>());

    //queue of requests to the server
    private final RequestQueue queue;

    // to later convert data to json
    final static private Gson gson = new Gson();

    public ShiftsViewModel(@NotNull Application application) {
        super(application);
        //initializes the queue instance variable
        queue = Volley.newRequestQueue(getApplication());
    }

    // get the LiveData object representing the state of shifts
    public LiveData<ArrayList<Shift>> getShiftstate() {
        return shiftsState;
    }

    //update existing shift entry
    public void updateEntry(int index, Shift shift) {
        shiftsState.getValue().set(index, shift);
        shiftsState.setValue(shiftsState.getValue());
    }

    // add new shift entry
    public void addEntry(Shift shift) {
        shiftsState.getValue().add(shift);
        shiftsState.setValue(shiftsState.getValue());
    }

    /**
     * get the shifts from server
     *
     * @param userId   the user id
     * @param token    token of the user
     * @param postCall for after the server response
     */
    public void getData(String userId,
                        String token,
                        @NotNull Api.PostCall<ArrayList<Shift>> postCall) {

        // get shifts from the server
        queue.add(getShifts(userId, token, () -> {
                },
                (jsonArray, responseError, throwable) -> {
                    try {
                        if (jsonArray != null) {
                            // convert from JSONArray to ArrayList of Shifts
                            ArrayList<Shift> arrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                arrayList.add(Shift.fromJSON(jsonObject));
                            }

                            // set shiftsState to ArrayList (of Shifts)
                            shiftsState.setValue(arrayList);
                            // callback with the ArrayList
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
     * get shifts from the server
     *
     * @param userId   ID of the user
     * @param token    token of the user
     * @param preCall  callback before the request sent
     * @param postCall callback after the server response
     * @return the request as AuthedJsonArrayObjectRequest
     */
    private static AuthedJsonArrayObjectRequest getShifts(String userId, String token,
                                                          @NotNull Api.PreCall preCall,
                                                          @NotNull Api.PostCall<JSONArray> postCall) {
        preCall.onPreCall();
        return new AuthedJsonArrayObjectRequest(Constants.ALL_SHIFTS_URL,
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
                        String resString =
                                new String(err.networkResponse.data, StandardCharsets.UTF_8);
                        postCall.onPostCall(null,
                                gson.fromJson(resString, Api.ResponseError.class), null);
                    } else {
                        postCall.onPostCall(null, null, err);
                    }
                });
    }

    /**
     * Get profiles from schedule
     *
     * @param userId     user id
     * @param token      user token
     * @param scheduleId schedule id
     * @param postCall   callback after server response
     */
    public void getProfileFromSchedule(String userId,
                                       String token, int scheduleId,
                                       @NotNull Api.PostCall<ArrayList<Profile>> postCall) {

        queue.add(getProfileFromSchedule(userId, token, scheduleId, () -> {
                },
                (jsonArray, responseError, throwable) -> {
                    try {
                        if (jsonArray != null) {
                    // convert from JSONArray to ArrayList of Profiles
                            ArrayList<Profile> arrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                // This only work if the the class has the same
                                // attribute as the DATABASE
                                arrayList.add(Profile.fromJSON(jsonObject));

                            }
                    // callback with the ArrayList
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
     * Get profiles from schedule
     *
     * @param userId     user id
     * @param token      user token
     * @param scheduleId schedule id
     * @param preCall    callback before the request sent
     * @param postCall   callback after server response
     * @return request as json array object
     */
    private static AuthedJsonArrayObjectRequest getProfileFromSchedule(String userId, String token, int scheduleId,
                                                                       @NotNull Api.PreCall preCall,
                                                                       @NotNull Api.PostCall<JSONArray> postCall) {
        preCall.onPreCall();
        return new AuthedJsonArrayObjectRequest(Constants.SHIFTS_FROM_SCHEDULE_URL + "?id=" + scheduleId,
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
                        String resString =
                                new String(err.networkResponse.data, StandardCharsets.UTF_8);
                        postCall.onPostCall(null,
                                gson.fromJson(resString, Api.ResponseError.class), null);
                    } else {
                        postCall.onPostCall(null, null, err);
                    }
                });
    }

    /**
     * Add a shift to the server
     *
     * @param shift    shift to be added
     * @param preCall  callback before request sent
     * @param postCall callback after server response
     */
    public void addShift(Shift shift,String userId,String token, Api.PreCall preCall,
                         Api.PostCall<Boolean> postCall) {

        preCall.onPreCall();
        queue.add(addShiftRequest(shift,userId,token, preCall, (jsonObject, responseError,
                                                             throwable) -> {
            if (responseError != null || throwable != null) {
            // callback with responseError and throwable
                postCall.onPostCall(null, responseError, throwable);
                return;
            }
            ArrayList<Shift> s = getShiftstate().getValue();
            if (s == null) {
                postCall.onPostCall(null, null, new Exception("No user error"));
                return;
            }
            // add shift to the local state
            s.add(shift);
            shiftsState.setValue(s);
            // callback with success status
            postCall.onPostCall(true, null, null);
        }));

    }

    /**
     * Create a request to add a shift
     *
     * @param shift    shift to be added
     * @param preCall  callback before request sent
     * @param postCall callback after server response
     * @return JsonObjectRequest to be added to the request queue
     */
    private AuthedJsonObjectRequest addShiftRequest(Shift shift,
                                              String userid,
                                              String token,
                                              Api.PreCall preCall,
                                              Api.PostCall<JSONObject> postCall) {
        preCall.onPreCall();

        // convert the Shift object to a JSON object
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(gson.toJson(shift));
        } catch (JSONException e) {
            // callback with the JSONException
            postCall.onPostCall(null, null, e);
            return null;
        }
        // create and return the JsonObjectRequest

        return new AuthedJsonObjectRequest(Request.Method.POST,userid,token,
                                           Constants.ADD_SHIFT_URL,
                                           jsonObj
                , res -> {
            try {
                postCall.onPostCall(res, null, null);
            } catch (Exception e) {
                // callback with the exception
                postCall.onPostCall(null, null, e);
            }
        }, err -> {
            if (err.networkResponse != null && err.networkResponse.data != null) {
                // convert the error response to a string and deserialize it
                String resString = new String(err.networkResponse.data, StandardCharsets.UTF_8);
                postCall.onPostCall(null, gson.fromJson(resString, Api.ResponseError.class), null);
            } else {
                // callback with the error
                postCall.onPostCall(null, null, err);
            }
        });
    }
}
