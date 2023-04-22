package com.example.myapplication.ScheduleWorkerIntoShifts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;

public class WorkerFragment extends DateListFragment<Profile> {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);
        binding.headerDatePicker.setText("Free Workers List");
        binding.dpDatePicker.setVisibility(View.GONE);
        binding.btnDatePicker.setText("Save Changes");


        return v;
    }

    @Override
    protected void onPickClicked(View view, String pickerValue) {

    }

    @Override
    protected void onItemClicked(Profile model, View view) {

    }
}
