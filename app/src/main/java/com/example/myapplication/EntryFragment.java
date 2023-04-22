package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.Model.RoleLevel;
import com.example.myapplication.User.Model.UserViewModel;
import com.example.myapplication.databinding.FragmentEntryBinding;

public class EntryFragment extends Fragment {

    private static final String               TAG = "EntryFragment";
    private              FragmentEntryBinding binding;
    private              UserViewModel        userViewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {
        binding       = FragmentEntryBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnMan.setVisibility(View.GONE);
        userViewModel.getUserState().observe(
                getViewLifecycleOwner(),
                (user) -> {
                    if (user.getAuthToken() != null && !user.getAuthToken().isEmpty()) {
                        binding.buttonSecond.setText(R.string.logout);
                        binding.buttonSecond.setOnClickListener(this::onLogout);
                        if(user.getLevel().equals(RoleLevel.MANAGER) || userViewModel.isManger()) {
                            binding.btnMan.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.buttonSecond.setText(R.string.login);
                        binding.buttonSecond.setOnClickListener(this::onLogin);
                        binding.btnMan.setVisibility(View.GONE);
                    }

                });
        binding.buttonSecond.setOnClickListener(this::onLogin);
        binding.btnMan.setOnClickListener(this::onMangerClicked);
    }

    void onLogin(View v) {
        NavHostFragment.findNavController(EntryFragment.this)
                .navigate(R.id.action_to_login);
    }

    void onLogout(View v) {
        try {

            userViewModel.logout(
                    () -> {
                    }, (aBoolean, responseError, throwable) -> {
                    });
        } catch (Exception e) {
            Log.e(TAG, "onLogout: Error occured", e);
        }
    }


    void onMangerClicked(View v) {
        NavHostFragment.findNavController(EntryFragment.this)
                .navigate(R.id.from_login_to_manger);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}