package com.example.myapplication.api;

import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * this class handle the api calls
 *
 * inner class ResponseError represent the errors the api return

 */
public class Api {
    /**
     * this class represent the errors the api return
     * with a timestamp of the error, the status code, the error description,
     * the message showed to the user, and the url path
     */
    public static class ResponseError {
        String  timestamp;
        Integer status;
        String  error;
        String  message;
        String  path;

        public ResponseError() {
        }

        public ResponseError(String timestamp, Integer status, String error, String message,
                             String path) {
            this.timestamp = timestamp;
            this.status    = status;
            this.error     = error;
            this.message   = message;
            this.path      = path;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    // before the error is called
    public interface PreCall {
        void onPreCall();
    }
    //
    public interface PostCall<T> {
        void onPostCall(@Nullable T t, @Nullable ResponseError responseError,
                        @Nullable Throwable throwable);


    }


}
