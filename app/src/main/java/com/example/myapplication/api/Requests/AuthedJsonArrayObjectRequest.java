package com.example.myapplication.api.Requests;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class AuthedJsonArrayObjectRequest extends JsonArrayRequest {
    private final String token;
    private final String userId;

    public AuthedJsonArrayObjectRequest(String url, String userId,
                                        String token, Response.Listener<JSONArray> listener,
                                        @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.token  = token;
        this.userId = userId;
    }

    public AuthedJsonArrayObjectRequest(int method, String url, String userId,
                                        String token, @Nullable JSONArray jsonRequest,
                                        Response.Listener<JSONArray> listener,
                                        @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.token  = token;
        this.userId = userId;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> hashMap = new HashMap<>(super.getHeaders());
        hashMap.put("Auth", String.format("%s:%s", this.userId, this.token));
        return hashMap;
    }
}
