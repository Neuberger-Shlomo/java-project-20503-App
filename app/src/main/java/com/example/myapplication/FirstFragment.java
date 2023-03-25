package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentFirstBinding;
import com.example.myapplication.user.BasicUser;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Button btn;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicUser.getInstance().setUsername(binding.etUsername.getText().toString());
                BasicUser.getInstance().setPassword(binding.etPassword.getText().toString());
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        binding.button.setOnClickListener(this::butoonPressed);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void butoonPressed(View view) {
        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_thiredFregment);
    }
}