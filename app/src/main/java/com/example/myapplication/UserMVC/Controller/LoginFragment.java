package com.example.myapplication.UserMVC.Controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.R;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentLoginBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

/**
 * controller for user login fragment
 */
public class LoginFragment extends Fragment implements TextWatcher {

    //  for logging from this api
    private final String TAG = "LoginFragment";

    private FragmentLoginBinding binding;
    private UserViewModel        userViewModel;

    /**
     * create fragment
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate the view for the fragment.
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Login view loaded");
        // Inflate the layout
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setEnabled(false);

        // add TextChangeListener to the password field
        binding.etPassword.addTextChangedListener(this);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // set the listeners for the buttons
        binding.btnLogin.setOnClickListener(this::onLoginPressed);
        binding.btnRegister.setOnClickListener(
                v -> NavHostFragment
                        .findNavController(LoginFragment.this)
                        .navigate(R.id.RegisterFragment)
                                              );
        userViewModel.getUserState().observe(getViewLifecycleOwner(), (basicUser -> {
            //  the user is authenticated go to entry fragment
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

    /**
     * handle login fails.
     */
    private void onLoginFiled(@Nullable Api.ResponseError responseError,
                              @Nullable Throwable throwable) {

        String message = responseError != null ? responseError.getMessage() : throwable != null ?
                throwable.getMessage() : "Unknown error";
        Log.d(TAG, "onLogin: Error occurred " + message);
        // show errors in the fields
        binding.etUsername.setError("Error");
        binding.etPassword.setError("Error");
        // show error massage
        Snackbar.make(requireView(), responseError != null ? message : "Unknown error",
                      Snackbar.LENGTH_LONG).show();


    }

    /**
     * handle login button pressed.
     */
    private void onLoginPressed(View v) {
        String username = binding.etUsername.getText().toString(), password =
                binding.etPassword.getText().toString();
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        //login try
        userViewModel.login(username, password, () -> {
        }, (jsonObject, responseError, throwable) -> {
            if (jsonObject == null && (responseError != null || throwable != null))
                this.onLoginFiled(responseError, throwable);
            // hide the progress bar and enable the login button again
            binding.progressBar.setVisibility(View.GONE);
            binding.btnLogin.setEnabled(true);
        });
    }

    /**
     * set binding to null when we close the fregment
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * handle text changed in the view.
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean noUsername = binding.etUsername.getText().toString().isEmpty();
        boolean noPassword = binding.etPassword.getText().toString().isEmpty();
        // only if fields not empty enable login
        binding.btnLogin.setEnabled(!(noUsername || noPassword));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}