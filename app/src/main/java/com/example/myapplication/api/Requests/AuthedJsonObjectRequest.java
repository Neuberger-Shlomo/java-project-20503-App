package com.example.myapplication.api.Requests;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

public class AuthedJsonObjectRequest extends JsonObjectRequest {

    private String token;
    private String userId;

    public AuthedJsonObjectRequest(String url, String userId,
                                   String token, Response.Listener<JSONObject> listener,
                                   @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.token = token;
        this.userId = userId;
    }

    public AuthedJsonObjectRequest(int method, String url, String userId,
                                   String token, @Nullable JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.token = token;
        this.userId = userId;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> hashMap =  super.getHeaders();
        hashMap.put("Auth",String.format("%s:%s",this.userId,this.token));
        return hashMap;
    }
}
