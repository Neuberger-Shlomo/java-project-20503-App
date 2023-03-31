package com.example.myapplication.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.volley.RequestQueue;
import com.example.myapplication.user.BasicUser;

public class UserViewModel extends ViewModel {

    private RequestQueue restQueue;
    private final MutableLiveData<BasicUser> uiState =
            new MutableLiveData<BasicUser>();
    public LiveData<BasicUser> getUiState() {
        return uiState;
    }



}
