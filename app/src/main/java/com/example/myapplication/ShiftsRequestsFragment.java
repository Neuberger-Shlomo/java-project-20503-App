package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;


import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Model.ShiftRequest;
import com.example.myapplication.ViewModel.ShiftRequestViewModel;
import com.example.myapplication.UserMVC.Model.UserViewModel;


public class ShiftsRequestsFragment extends DateListFragment<ShiftRequest> {

    private ShiftRequestViewModel shiftRequestViewModel;
    private UserViewModel         userViewModel;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        binding.headerDatePicker.setText("Shifts Requests");

        shiftRequestViewModel = new ViewModelProvider(this).get(ShiftRequestViewModel.class);
        userViewModel         = new ViewModelProvider(requireActivity()).get(UserViewModel.class);


        shiftRequestViewModel.getData(userViewModel.getUserState().getValue().getId(),
                                      userViewModel.getUserState().getValue().getAuthToken(),
                                      this::onDataArrived);


        return view;
    }

    @Override
    protected void onPickClicked(View view, String pickerValue) {
        String pickedDate =
                new StringBuilder()
                        .append(binding.dpDatePicker.getDayOfMonth())
                        .append("-")
                        .append(binding.dpDatePicker.getMonth() + 1)
                        .append("-")
                        .append(binding.dpDatePicker.getYear())
                        .toString();
        adapter.setFilter(pickedDate, ((item, s) -> !item.getShiftDate().equals(s)));
    }

    @Override
    protected void onItemClicked(ShiftRequest model, View view) {
        String requestMessage =
                "Request Timestamp: " + model.getTimestamp() + "\nShift Start Hour: " +
                model.getStartHour() + "\nShift Duration: " +
                model.getDuration();

        new AlertDialog.Builder(requireContext())
                .setTitle("More Info:")
                .setMessage(requestMessage)
                .setPositiveButton("Ok", null)
                .create().show();
    }



}
