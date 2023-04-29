package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.ViewModel.ShiftsViewModel;
import com.example.myapplication.api.Api;

import java.util.ArrayList;

/**
 * this fragment that displays the user shifts schedule
 * user can click on a shift and see the list of workers in that shift
 */
public class ScheduleStatusFragment extends DateListFragment<Shift> {
    private ShiftsViewModel shiftViewModel;
    private UserViewModel   userViewModel;

    /**
     * inflate the view  and set the listeners
     *
     * @param inflater
     * @param container          the parent view
     * @param savedInstanceState saved previous state. so we can restore it.
     * @return the view for the fregment
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        binding.headerDatePicker.setText("Schedule Status");

        shiftViewModel = new ViewModelProvider(requireActivity()).get(ShiftsViewModel.class);
        userViewModel  = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        // if no no user state return the root view

        if (userViewModel.getUserState().getValue() == null)
            return binding.getRoot();

        shiftViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                this::onDataArrived);

        return root;
    }

    /**
     * filter shifts via the adapter (to show shifts only shifts for the selected date)
     *
     * @param view        the view clicked
     * @param pickerValue the selected date
     */
    @Override
    protected void onPickClicked(View view, String pickerValue) {
        adapter.setFilter(pickerValue, (item, s) -> !item.getDate().equals(s));
    }

    /**
     * get list of workers in this shift and show them in dialog
     *
     * @param model the selected Shift
     *              * @param view        the view clicked
     */
    @Override
    protected void onItemClicked(Shift model, View view) {
        if (userViewModel.getUserState().getValue() == null)
            return;
        shiftViewModel.getProfileFromSchedule(userViewModel.getUserState().getValue().getId(),
                                              userViewModel.getUserState().getValue().getAuthToken(),
                                              model.getId(),
                                              this::onPostProfileCall);

    }


    /**
     * show the list of workers in a dialog.
     *
     * @param profiles      list of workers in the shift
     * @param responseError if error
     * @param throwable     if exception
     */
    private void onPostProfileCall(ArrayList<Profile> profiles,
                                   Api.ResponseError responseError,
                                   Throwable throwable) {
        if (profiles == null)
            // TODO: Add error handling
            return;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < profiles.size(); i++) {
            Profile p = profiles.get(i);
            stringBuilder.append("").append(i + 1).append(". ").append(p.toPrettyString()).append(
                    "\n");
        }
        String msg = stringBuilder.toString();
        new AlertDialog.Builder(requireContext())
                .setTitle("Workers in Shift:")
                .setMessage(msg.isEmpty() ? "No employs" : msg)
                .setPositiveButton("Ok", null)
                .create().show();
    }


}
