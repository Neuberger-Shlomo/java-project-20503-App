package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentEntryBinding;
import com.example.myapplication.user.BasicUser;
import com.example.myapplication.user.RoleLevel;

public class EntryFragment extends Fragment {

    private FragmentEntryBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentEntryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if(BasicUser.getInstance().getLevel() != RoleLevel.MANAGER){
//            // TODO: Alert of error and go to user view
//        }
//        binding.textviewSecond.setText(String.format("%s\n%s",
//                BasicUser.getInstance().getUsername(),
//                BasicUser.getInstance().getPassword()));
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EntryFragment.this)
                        .navigate(R.id.action_to_login);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}