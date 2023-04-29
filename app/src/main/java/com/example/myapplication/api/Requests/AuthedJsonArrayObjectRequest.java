package com.example.myapplication.api.Requests;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * add authentication headers to the request
 */
public class AuthedJsonArrayObjectRequest extends JsonArrayRequest {
    private final String token;
    private final String userId;

    /**
     * constructor that adds authentication headers to the request.
     *
     * @param url           the url to send the request
     * @param userId        the user id
     * @param token         the user token
     * @param listener      listener handle successful responses
     * @param errorListener listener that handle errors
     */
    public AuthedJsonArrayObjectRequest(String url, String userId,
                                        String token, Response.Listener<JSONArray> listener,
                                        @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.token  = token;
        this.userId = userId;
    }

    /**
     * constructor that adds authentication headers to the request.
     *
     * @param method        the http method for the request (for example GET, Post..)
     * @param url           the url to send the request
     * @param userId        the user id
     * @param token         the user token
     * @param jsonRequest   The jaon request body
     * @param listener      listener = handle successful responses
     * @param errorListener listener = handle errors
     */

    public AuthedJsonArrayObjectRequest(int method, String url, String userId,
                                        String token, @Nullable JSONArray jsonRequest,
                                        Response.Listener<JSONArray> listener,
                                        @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.token  = token;
        this.userId = userId;
    }

    /**
     * add authentication headers to request
     *
     * @return map of headers that inclde in the request
     * @throws AuthFailureError if error
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> hashMap = new HashMap<>(super.getHeaders());
        hashMap.put("Auth", String.format("%s:%s", this.userId, this.token));
        return hashMap;
    }
}
