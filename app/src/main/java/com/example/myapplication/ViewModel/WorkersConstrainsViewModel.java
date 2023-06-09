package com.example.myapplication.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.Constraints;
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

public class WorkersConstrainsViewModel extends AndroidViewModel {

    final static private Gson gson = new Gson();
    private final MutableLiveData<ArrayList<Constraints>> workersConstraintsState =
            new MutableLiveData<ArrayList<Constraints>>(new ArrayList<Constraints>());
    private final RequestQueue                            queue;

    public WorkersConstrainsViewModel(@NonNull Application application) {
        super(application);
        queue = Volley.newRequestQueue(getApplication());
    }

    private static AuthedJsonArrayObjectRequest getConstraints(String userId, String token,
                                                               @NotNull Api.PreCall preCall,
                                                               @NotNull Api.PostCall<JSONArray> postCall) {
        preCall.onPreCall();
        return new AuthedJsonArrayObjectRequest(Constants.ALL_CONSTRAINTS_URL,
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

    public LiveData<ArrayList<Constraints>> getWorkersConstraintsstate() {
        return workersConstraintsState;
    }

    public void getData(String userId,
                        String token,
                        @NotNull Api.PostCall<ArrayList<Constraints>> postCall) {

        queue.add(getConstraints(userId, token, () -> {
                                 },
                                 (jsonArray, responseError, throwable) -> {
                                     try {
                                         if (jsonArray != null) {
                                             ArrayList<Constraints> arrayList = new ArrayList<>();
                                             for (int i = 0; i < jsonArray.length(); i++) {
                                                 JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                 // This only work if the the class has the same
                                                 // attribute as the DATABASE
                                                 arrayList.add(Constraints.fromJSON(jsonObject));

                                             }
                                             workersConstraintsState.setValue(arrayList);
                                             postCall.onPostCall(arrayList, null, null);
                                         } else {
                                             postCall.onPostCall(null, responseError, throwable);
                                         }
                                     } catch (JSONException e) {
                                         postCall.onPostCall(null, null, e);
                                     }
                                 }));
    }


}
