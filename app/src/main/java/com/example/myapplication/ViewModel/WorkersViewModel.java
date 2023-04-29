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
import com.example.myapplication.Model.Profile;
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

/**
 * WorkersViewModel - A ViewModel representation of the workers in the system
 * Act as a bridge between the worker profiles in the db and the controllers that handle worker
 * profiles
 */
public class WorkersViewModel extends AndroidViewModel {

    // Gson instance for converting data to JSON
    final static private Gson gson = new Gson();
    // Queue for managing requests to the server
    private final RequestQueue queue;
    // LiveData object that represents the current state of the worker profiles
    private final MutableLiveData<ArrayList<Profile>> workersState =
            new MutableLiveData<ArrayList<Profile>>(new ArrayList<Profile>());

    /**
     * Initialize WorkersViewModel with application context
     *
     * @param application Application context
     */
    public WorkersViewModel(@NonNull Application application) {
        super(application);
        // Initialize request queue
        queue = Volley.newRequestQueue(getApplication());
    }

    /**
     * Request to get worker profiles from server
     *
     * @param userId   User ID
     * @param token    User token
     * @param preCall  Callback before request is sent
     * @param postCall Callback after server response
     * @return AuthedJsonArrayObjectRequest representing the request
     */
    private static AuthedJsonArrayObjectRequest getProfiles(String userId, String token,
                                                            @NotNull Api.PreCall preCall,
                                                            @NotNull Api.PostCall<JSONArray> postCall) {
        preCall.onPreCall();
        //ArrayObjectRequest to fetch profiles
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

    /**
     * Request to get worker profiles from server who are available to work in a given shift
     *
     * @param userId   User ID
     * @param token    User token
     * @param shiftId  ID of the shift
     * @param preCall  Callback before request is sent
     * @param postCall Callback after server response
     * @return AuthedJsonArrayObjectRequest representing the request
     */
    private static AuthedJsonArrayObjectRequest getfreeWorkers(String userId, String token,
                                                               int shiftId,
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

    /**
     * Return the current worker state
     *
     * @return LiveData object representing the current state of the worker profiles
     */
    public LiveData<ArrayList<Profile>> getWorkersState() {
        return workersState;
    }

    /**
     * Fetch worker profiles from server
     *
     * @param userId   User ID
     * @param token    User token
     * @param postCall Callback to handle server response
     */
    public void getData(String userId,
                        String token,
                        @NotNull Api.PostCall<ArrayList<Profile>> postCall) {

        // Add request to queue
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
                                          // Update worker state
                                          workersState.setValue(arrayList);
                                          // Callback with result
                                          postCall.onPostCall(arrayList, null, null);
                                      } else {
                                          // Callback with error
                                          postCall.onPostCall(null, responseError, throwable);
                                      }
                                  } catch (JSONException e) {
                                      // Callback with JSON exception
                                      postCall.onPostCall(null, null, e);
                                  }
                              }));
    }

    /**
     * Fetch worker profiles from server who are available to work in a given shift
     *
     * @param userId   User ID
     * @param token    User token
     * @param shiftId  ID of the shift
     * @param postCall Callback to handle server response
     */
    public void getFreeWorkersData(String userId,
                                   String token, int shiftId,
                                   @NotNull Api.PostCall<ArrayList<Profile>> postCall) {

        // Add request to queue
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
                                             // Update worker state
                                             workersState.setValue(arrayList);
                                             // Callback with result
                                             postCall.onPostCall(arrayList, null, null);
                                         } else {
                                             // Callback with error
                                             postCall.onPostCall(null, responseError, throwable);
                                         }
                                     } catch (JSONException e) {
                                         // Callback with JSON exception
                                         postCall.onPostCall(null, null, e);
                                     }
                                 }));
    }

    /**
     * Add a worker to a shift
     * /**
     * Add a worker to a shift
     *
     * @param pId      Worker profile ID
     * @param sId      Shift ID
     * @param preCall  Callback before request is sent
     * @param postCall Callback after server response
     */
    public void addWorkerToShift(int pId, int sId, String userId,
                                 String token, Api.PreCall preCall,
                                 Api.PostCall<Boolean> postCall) {

        // Call the preCall callback before making the request
        preCall.onPreCall();
        queue.add(addWorkerRequest(pId, sId, userId, token, preCall, (result, responseError,
                                                                      throwable) -> {
            if (responseError != null || throwable != null) {
                postCall.onPostCall(null, responseError, throwable);
                return;
            }
            // Callback with result if no error
            postCall.onPostCall(result, null, null);
        }));
    }

    /**
     * Request to add worker to a shift
     *
     * @param pId      Worker profile ID
     * @param sId      Shift ID
     * @param preCall  Callback before request is sent
     * @param postCall Callback after server response
     * @return JsonObjectRequest representing the request
     */

    private AuthedJsonObjectRequest addWorkerRequest(int pId, int sId,
                                                     String userId,
                                                     String token,
                                                     Api.PreCall preCall,
                                                     Api.PostCall<Boolean> postCall) {
        preCall.onPreCall();

        // Construct JSON Object to send with the request
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("pId", pId);
            jsonObj.put("sId", sId);
        } catch (JSONException e) {
            // Callback with JSON exception
            postCall.onPostCall(null, null, e);
            return null;
        }

        return new AuthedJsonObjectRequest(Request.Method.POST, Constants.ADD_WORKER_TO_SHIFT_URL
                , userId,
                                           token, jsonObj,
                                           res -> {
                                               try {
                                                   // Callback with result
                                                   postCall.onPostCall(res.getBoolean("result"),
                                                                       null, null);
                                               } catch (Exception e) {
                                                   // Callback with exception
                                                   postCall.onPostCall(null, null, e);
                                               }
                                           }, err -> {
            // Handle the error scenario
            if (err.networkResponse != null && err.networkResponse.data != null) {
                String resString = new String(err.networkResponse.data, StandardCharsets.UTF_8);
                postCall.onPostCall(null, gson.fromJson(resString, Api.ResponseError.class), null);
            } else {
                // Callback with error
                postCall.onPostCall(null, null, err);
            }
        });
    }

    public void resetShift(String uid, String token, int sId, Api.PreCall preCall,
                           Api.PostCall<Boolean> postCall) {

        preCall.onPreCall();
        queue.add(resetShiftRequest(uid, token, sId, preCall, (result, responseError,
                                                               throwable) -> {
            if (responseError != null || throwable != null) {
                postCall.onPostCall(null, responseError, throwable);
                return;
            }
            postCall.onPostCall(result, null, null);
        }));
    }


    private JsonObjectRequest resetShiftRequest(String uid, String token, int sId,
                                                Api.PreCall preCall,
                                                Api.PostCall<Boolean> postCall) {
        preCall.onPreCall();

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("sId", sId);
        } catch (JSONException e) {
            postCall.onPostCall(null, null, e);
            return null;
        }

        return new AuthedJsonObjectRequest(Request.Method.DELETE,
                                           Constants.DELETE_SHIFT_SCHEDULE_URL + "/" + sId, uid,
                                           token, jsonObj, res -> {
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
