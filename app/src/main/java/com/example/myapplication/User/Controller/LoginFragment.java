package com.example.myapplication.User.Controller;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.User.Model.UserViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentLoginBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class LoginFragment extends Fragment implements TextWatcher {

    private final String TAG = "LoginFragment";

    private FragmentLoginBinding binding;
    private UserViewModel        userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Login view loaded");
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setEnabled(false);
        binding.etPassword.addTextChangedListener(this);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding.btnLogin.setOnClickListener(this::onLoginPressed);
        binding.btnRegister.setOnClickListener(
                v -> NavHostFragment
                        .findNavController(LoginFragment.this)
                        .navigate(R.id.action_Login_to_RegisterFragment)
                                              );
        userViewModel.getUserState().observe(getViewLifecycleOwner(), (basicUser -> {
            if (!Objects.equals(basicUser.getAuthToken(), null) && !Objects.equals(basicUser.getAuthToken(), "")) {
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.EntryFragment);
            }
            if (!basicUser.getUsername().isEmpty())
                binding.etUsername.setText(basicUser.getUsername());

        }));
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void onLoginFiled(@Nullable Api.ResponseError responseError,
                              @Nullable Throwable throwable) {

        String message = responseError != null ? responseError.getMessage() : throwable != null ?
                throwable.getMessage() : "Unknown error";
        Log.d(TAG, "onLogin: Error occurred " + message);
        binding.etUsername.setError("Error");
        binding.etPassword.setError("Error");
        Snackbar.make(requireView(), responseError != null ? message : "Unknown error",
                      Snackbar.LENGTH_LONG).show();


    }

    private void onLoginPressed(View v) {
        String username = binding.etUsername.getText().toString(), password =
                binding.etPassword.getText().toString();
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);
        userViewModel.login(username, password, () -> {
        }, (jsonObject, responseError, throwable) -> {
            if (jsonObject == null && (responseError != null || throwable != null))
                this.onLoginFiled(responseError, throwable);
            binding.progressBar.setVisibility(View.GONE);
            binding.btnLogin.setEnabled(true);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean noUsername = binding.etUsername.getText().toString().isEmpty();
        boolean noPassword = binding.etPassword.getText().toString().isEmpty();
        binding.btnLogin.setEnabled(!(noUsername || noPassword));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}