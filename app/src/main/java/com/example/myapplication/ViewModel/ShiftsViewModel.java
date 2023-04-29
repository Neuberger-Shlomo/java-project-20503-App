package com.example.myapplication.ViewModel;

import android.app.Application;

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


    final static private Gson gson = new Gson();
    private final MutableLiveData<ArrayList<Shift>> shiftsState =
            new MutableLiveData<ArrayList<Shift>>(new ArrayList<Shift>());
    private final RequestQueue                      queue;

    public ShiftsViewModel(@NotNull Application application) {
        super(application);
        queue = Volley.newRequestQueue(getApplication());
    }

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

    private static AuthedJsonArrayObjectRequest getProfileFromSchedule(String userId,
                                                                       String token, int scheduleId,
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

    public LiveData<ArrayList<Shift>> getShiftstate() {
        return shiftsState;
    }

    public void updateEntry(int index, Shift shift) {
        shiftsState.getValue().set(index, shift);
        shiftsState.setValue(shiftsState.getValue());
    }

    public void addEntry(Shift shift) {
        shiftsState.getValue().add(shift);
        shiftsState.setValue(shiftsState.getValue());
    }

    public void getData(String userId,
                        String token,
                        @NotNull Api.PostCall<ArrayList<Shift>> postCall) {

        queue.add(getShifts(userId, token, () -> {
                            },
                            (jsonArray, responseError, throwable) -> {
                                try {
                                    if (jsonArray != null) {
                                        ArrayList<Shift> arrayList = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            // This only work if the the class has the same
                                            // attribute as the DATABASE
                                            arrayList.add(Shift.fromJSON(jsonObject));

                                        }
                                        shiftsState.setValue(arrayList);
                                        postCall.onPostCall(arrayList, null, null);
                                    } else {
                                        postCall.onPostCall(null, responseError, throwable);
                                    }
                                } catch (JSONException e) {
                                    postCall.onPostCall(null, null, e);
                                }
                            }));
    }

    public void getProfileFromSchedule(String userId,
                                       String token, int scheduleId,
                                       @NotNull Api.PostCall<ArrayList<Profile>> postCall) {

        queue.add(getProfileFromSchedule(userId, token, scheduleId, () -> {
                                         },
                                         (jsonArray, responseError, throwable) -> {
                                             try {
                                                 if (jsonArray != null) {
                                                     ArrayList<Profile> arrayList =
                                                             new ArrayList<>();
                                                     for (int i = 0; i < jsonArray.length(); i++) {
                                                         JSONObject jsonObject =
                                                                 jsonArray.getJSONObject(i);
                                                         // This only work if the the class has
                                                         // the same
                                                         // attribute as the DATABASE
                                                         arrayList.add(Profile.fromJSON(jsonObject));

                                                     }
                                                     postCall.onPostCall(arrayList, null, null);
                                                 } else {
                                                     postCall.onPostCall(null, responseError,
                                                                         throwable);
                                                 }
                                             } catch (JSONException e) {
                                                 postCall.onPostCall(null, null, e);
                                             }
                                         }));
    }

    public void addShift(Shift shift, String userId, String token, Api.PreCall preCall,
                         Api.PostCall<Boolean> postCall) {

        preCall.onPreCall();
        queue.add(addShiftRequest(shift, userId, token, preCall, (jsonObject, responseError,
                                                                  throwable) -> {
            if (responseError != null || throwable != null) {
                postCall.onPostCall(null, responseError, throwable);
                return;
            }
            ArrayList<Shift> s = getShiftstate().getValue();
            if (s == null) {
                postCall.onPostCall(null, null, new Exception("No user error"));
                return;
            }
            s.add(shift);
            shiftsState.setValue(s);
            postCall.onPostCall(true, null, null);
        }));

    }


    private AuthedJsonObjectRequest addShiftRequest(Shift shift,
                                                    String userid,
                                                    String token,
                                                    Api.PreCall preCall,
                                                    Api.PostCall<JSONObject> postCall) {
        preCall.onPreCall();

        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(gson.toJson(shift));
        } catch (JSONException e) {
            postCall.onPostCall(null, null, e);
            return null;
        }

        return new AuthedJsonObjectRequest(Request.Method.POST, Constants.ADD_SHIFT_URL, userid, token,
                                           jsonObj
                , res -> {
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


}
