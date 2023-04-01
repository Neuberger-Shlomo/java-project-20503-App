package com.example.myapplication.api;

import androidx.annotation.Nullable;

public class Api {

    public class ResponseError{
        String timestamp;
        Integer status;
        String error;
        String message;
        String path;

        public ResponseError() {
        }

        public ResponseError(String timestamp, Integer status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
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
    public interface PreCall{
        void onPreCall();
    }

    public interface PostCall<T>{
        void onPostCall(@Nullable T t,@Nullable ResponseError responseError,@Nullable Throwable throwable);
    }
}
