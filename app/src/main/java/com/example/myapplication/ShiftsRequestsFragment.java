package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Model.ShiftRequest;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.ViewModel.ShiftRequestViewModel;

/**
 * disply list of of shifts requests
 */

public class ShiftsRequestsFragment extends DateListFragment<ShiftRequest> {

    private ShiftRequestViewModel shiftRequestViewModel;
    private UserViewModel         userViewModel;

    /**
     * inflate the view  and set the listeners
     *
     * @param inflater
     * @param container          the parent view
     * @param savedInstanceState saved previous state. so we can restore it.
     * @return the view for the fregment
     */
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

    /**
     * filter shifts via the adapter (to show only shifts for the selected date)
     *
     * @param view        the view clicked
     * @param pickerValue the selected date
     */

    @Override
    protected void onPickClicked(View view, String pickerValue) {
        String pickedDate =
                binding.dpDatePicker.getDayOfMonth() +
                "-" +
                (binding.dpDatePicker.getMonth() + 1) +
                "-" +
                binding.dpDatePicker.getYear();
        adapter.setFilter(pickedDate, ((item, s) -> !item.getShiftDate().equals(s)));
    }

    /**
     * shift request is clicked -> show data about the request
     *
     * @param model the shift request model
     * @param view  the view
     */

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
