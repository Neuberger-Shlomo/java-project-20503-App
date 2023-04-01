package com.example.myapplication;

import android.app.AlertDialog;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.widget.Button;

import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentLoginBinding;
import com.example.myapplication.user.BasicUser;

import org.json.JSONObject;

import java.util.Objects;

public class LoginFragment extends Fragment implements TextWatcher, Api.PreCall {

    private FragmentLoginBinding binding;
    private Button btn;
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.btnLogin.setEnabled(false);
        binding.etPassword.addTextChangedListener(this);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding.btnLogin.setOnClickListener(this::onLogin);
        userViewModel.getUiState().observe(getViewLifecycleOwner(), (basicUser -> {
            if (!Objects.equals(basicUser.getAuthToken(), ""))
                NavHostFragment.findNavController(LoginFragment.this).popBackStack();
        }));
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void onLogin(View v) {
        String username = binding.etUsername.getText().toString(), password =
                binding.etPassword.getText().toString();
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);
        userViewModel.login(username, password, () -> {
        }, (jsonObject, responseError, throwable) -> {
            binding.progressBar.setVisibility(View.INVISIBLE);
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

    @Override
    public void onPreCall() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }


}