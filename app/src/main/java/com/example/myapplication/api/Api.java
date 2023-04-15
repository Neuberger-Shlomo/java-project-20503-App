package com.example.myapplication.api;

import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class Api {

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

    public static class RegisterRequest {
        String firstName;
        String lastName;
        String email;
        String phoneNumber;
        String username;
        String password;

        public RegisterRequest() {
        }

        public RegisterRequest(String firstName, String lastName, String email,
                               String phoneNumber, String username, String password) {
            this.firstName   = firstName;
            this.lastName    = lastName;
            this.email       = email;
            this.phoneNumber = phoneNumber;
            this.username    = username;
            this.password    = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public interface PreCall {
        void onPreCall();
    }

    public interface PostCall<T> {
        void onPostCall(@Nullable T t, @Nullable ResponseError responseError,
                        @Nullable Throwable throwable);


    }


}
