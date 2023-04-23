package com.example.myapplication.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.Constants;
import com.example.myapplication.api.Requests.AuthedJsonArrayObjectRequest;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WorkersViewModel extends AndroidViewModel {

    private final RequestQueue queue;

    final static private Gson                                gson         = new Gson();
    private final        MutableLiveData<ArrayList<Profile>> workersState =
            new MutableLiveData<ArrayList<Profile>>(new ArrayList<Profile>());

    public LiveData<ArrayList<Profile>> getWorkersState() {
        return workersState;
    }

    public WorkersViewModel(@NonNull Application application) {
        super(application);
        queue = Volley.newRequestQueue(getApplication());
    }

    public void getData(String userId,
                        String token,
                        @NotNull Api.PostCall<ArrayList<Profile>> postCall) {

        queue.add(getProfiles(userId, token, () -> {
                              },
                              (jsonArray, responseError, throwable) -> {
                                  try {
                                      if (jsonArray != null) {
                                          ArrayList<Profile> arrayList = new ArrayList<>();
                                          for (int i = 0; i < jsonArray.length(); i++) {
                                              JSONObject jsonObject = jsonArray.getJSONObject(i);
                                              // This only work if the the class has the same
                                              // attribute as the DATABASE
                                              arrayList.add(Profile.fromJSON(jsonObject));

                                          }
                                          workersState.setValue(arrayList);
                                          postCall.onPostCall(arrayList, null, null);
                                      } else {
                                          postCall.onPostCall(null, responseError, throwable);
                                      }
                                  } catch (JSONException e) {
                                      postCall.onPostCall(null, null, e);
                                  }
                              }));
    }


    private static AuthedJsonArrayObjectRequest getProfiles(String userId, String token,
                                                            @NotNull Api.PreCall preCall,
                                                            @NotNull Api.PostCall<JSONArray> postCall) {
        preCall.onPreCall();
        return new AuthedJsonArrayObjectRequest(Constants.ALL_PROFILES_URL,
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

    public void getFreeWorkersData(String userId,
                                   String token, int shiftId,
                                   @NotNull Api.PostCall<ArrayList<Profile>> postCall) {

        queue.add(getfreeWorkers(userId, token, shiftId, () -> {
                                 },
                                 (jsonArray, responseError, throwable) -> {
                                     try {
                                         if (jsonArray != null) {
                                             ArrayList<Profile> arrayList = new ArrayList<>();
                                             for (int i = 0; i < jsonArray.length(); i++) {
                                                 JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                 // This only work if the the class has the same
                                                 // attribute as the DATABASE
                                                 arrayList.add(Profile.fromJSON(jsonObject));

                                             }
                                             workersState.setValue(arrayList);
                                             postCall.onPostCall(arrayList, null, null);
                                         } else {
                                             postCall.onPostCall(null, responseError, throwable);
                                         }
                                     } catch (JSONException e) {
                                         postCall.onPostCall(null, null, e);
                                     }
                                 }));
    }


    private static AuthedJsonArrayObjectRequest getfreeWorkers(String userId, String token, int shiftId,
                                                               @NotNull Api.PreCall preCall,
                                                               @NotNull Api.PostCall<JSONArray> postCall) {
        preCall.onPreCall();
        return new AuthedJsonArrayObjectRequest(Constants.SHIFT_URL + "/" + shiftId + Constants.GET_FREE_WORKERS,
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

    public void addWorkerToShift(int pId, int sId, Api.PreCall preCall,
                         Api.PostCall<Boolean> postCall) {

        preCall.onPreCall();
        queue.add(addWorkerRequest(pId,sId, preCall, (result, responseError,
                                                   throwable) -> {
            if (responseError != null || throwable != null) {
                postCall.onPostCall(null, responseError, throwable);
                return;
            }
            postCall.onPostCall(result, null, null);
        }));
    }


    private JsonObjectRequest addWorkerRequest(int pId, int sId,
                                              Api.PreCall preCall,
                                              Api.PostCall<Boolean> postCall) {
        preCall.onPreCall();

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("pId",pId);
            jsonObj.put("sId",sId);
        } catch (JSONException e) {
            postCall.onPostCall(null, null, e);
            return null;
        }

        return new JsonObjectRequest(Request.Method.POST, Constants.ADD_WORKER_TO_SHIFT_URL, jsonObj, res -> {
            try {
                postCall.onPostCall(res.getBoolean("result"), null, null);
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
