package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.databinding.FragmentEntryBinding;

public class EntryFragment extends Fragment {

    private FragmentEntryBinding binding;
    private UserViewModel        userViewModel;

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
        userViewModel.getUserState().observe(
                getViewLifecycleOwner(),
                (user) -> {
                    if (user.getAuthToken() != null && !user.getAuthToken().isEmpty()) {
                        binding.buttonSecond.setText(R.string.logout);
                        binding.buttonSecond.setOnClickListener(this::onLogout);
                    } else {
                        binding.buttonSecond.setText(R.string.login);
                        binding.buttonSecond.setOnClickListener(this::onLogin);
                    }

                });
        binding.buttonSecond.setOnClickListener(this::onLogin);
    }

    void onLogin(View v) {
        NavHostFragment.findNavController(EntryFragment.this)
                .navigate(R.id.action_to_login);
    }

    void onLogout(View v) {
        userViewModel.logout(
                () -> {},(aBoolean, responseError, throwable) -> {});
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}