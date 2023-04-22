package com.example.myapplication.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.Constraints;
import com.example.myapplication.Model.ShiftRequest;
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

public class ShiftRequestViewModel extends AndroidViewModel {

    private final MutableLiveData<ArrayList<ShiftRequest>> shiftRequestState =
            new MutableLiveData<ArrayList<ShiftRequest>>(new ArrayList<ShiftRequest>());
    private final RequestQueue queue;

    final static private Gson gson = new Gson();

    public ShiftRequestViewModel(@NonNull Application application) {
        super(application);
        queue = Volley.newRequestQueue(getApplication());
    }
    public LiveData<ArrayList<ShiftRequest>> getShiftRequestState() {
        return shiftRequestState;
    }

    public void getData(String userId,
                        String token,
                        @NotNull Api.PostCall<ArrayList<ShiftRequest>> postCall) {

        queue.add(getShiftRequest(userId, token, () -> {
                },
                (jsonArray, responseError, throwable) -> {
                    try {
                        if (jsonArray != null) {
                            ArrayList<ShiftRequest> arrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                // This only work if the the class has the same
                                // attribute as the DATABASE
                                arrayList.add(ShiftRequest.fromJSON(jsonObject));

                            }
                            shiftRequestState.setValue(arrayList);
                            postCall.onPostCall(arrayList, null, null);
                        } else {
                            postCall.onPostCall(null, responseError, throwable);
                        }
                    } catch (JSONException e) {
                        postCall.onPostCall(null, null, e);
                    }
                }));
    }


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
                        String resString =
                                new String(err.networkResponse.data, StandardCharsets.UTF_8);
                        postCall.onPostCall(null,
                                gson.fromJson(resString, Api.ResponseError.class), null);
                    } else {
                        postCall.onPostCall(null, null, err);
                    }
                });
    }



}
