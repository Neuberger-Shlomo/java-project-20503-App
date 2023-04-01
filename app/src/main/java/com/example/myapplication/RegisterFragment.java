package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding       = FragmentRegisterBinding.inflate(inflater, container, false);
        binding.userCard.setScaleY(0f);
        binding.userCard.setVisibility(View.GONE);
        int shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);
        binding.btnProfileNext.setOnClickListener(
                v -> {
                    if (this.isValidProfile())
                        replace(binding.profileCard, binding.userCard);
                });
        binding.btnPrev.setOnClickListener(
                v -> replace(binding.userCard, binding.profileCard));
        binding.btnRegister.setOnClickListener(this::onRegisterClicked);
        return binding.getRoot();
    }

    boolean isValidProfile() {
        String firstName   = binding.etFirstName.getText().toString();
        String lastName    = binding.etLastName.getText().toString();
        String email       = binding.etEmail.getText().toString();
        String phoneNumber = binding.etPhoneNumber.getText().toString();
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            if (binding.profileCard.getVisibility() != View.VISIBLE)
                replace(binding.userCard,
                        binding.profileCard);
            if (firstName.isEmpty())
                binding.etFirstName.setBackgroundColor(Color.parseColor("#80FF0000"));
            else
                binding.etFirstName.setBackgroundColor(Color.parseColor("#FFFFFF"));
            if (lastName.isEmpty())
                binding.etLastName.setBackgroundColor(Color.parseColor("#80FF0000"));
            else
                binding.etLastName.setBackgroundColor(Color.parseColor("#FFFFFF"));
            if (email.isEmpty())
                binding.etEmail.setBackgroundColor(Color.parseColor("#80FF0000"));
            else
                binding.etEmail.setBackgroundColor(Color.parseColor("#FFFFFF"));
            if (phoneNumber.isEmpty())
                binding.etPhoneNumber.setBackgroundColor(Color.parseColor("#80FF0000"));
            else
                binding.etPhoneNumber.setBackgroundColor(Color.parseColor("#FFFFFF"));
            return false;
        }
        return true;
    }

    private void onRegisterClicked(View view) {

        String username    = binding.etUsername.getText().toString();
        String password    = binding.etPassword.getText().toString();
        String firstName   = binding.etFirstName.getText().toString();
        String lastName    = binding.etLastName.getText().toString();
        String email       = binding.etEmail.getText().toString();
        String phoneNumber = binding.etPhoneNumber.getText().toString();

        userViewModel.register(new Api.RegisterRequest(firstName, lastName, email, phoneNumber,
                                                       username, password), () -> {
        }, (valid, responseError, throwable) -> {
            if (Boolean.TRUE.equals(valid))
                NavHostFragment.findNavController(RegisterFragment.this).popBackStack();
        });


    }

    void replace(View v1, View v2) {
        v2.setScaleY(0f);
        v2.setVisibility(View.GONE);
        int shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        v1.animate()
                .scaleY(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                        v2.setVisibility(View.VISIBLE);
                        v1.setVisibility(View.GONE);
                        v2.animate()
                                .scaleY(1f)
                                .setDuration(shortAnimationDuration)
                                .setListener(null);
                    }
                });

    }
}