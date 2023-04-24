package com.example.myapplication.ScheduleWorkerIntoShifts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.UserMVC.Model.User;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.ViewModel.WorkersViewModel;
import com.example.myapplication.api.Api;

import java.util.ArrayList;

public class WorkerFragment extends DateListFragment<Profile> {
    public final static String SHIFT_ID_KEY = "shiftIdKey";
    private int shiftId = -1;
    UserViewModel userViewModel;
    WorkersViewModel workersViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);
        binding.headerDatePicker.setText("Free Workers List");
        binding.dpDatePicker.setVisibility(View.GONE);
        binding.btnDatePicker.setText("Save Changes");
        binding.btn2DatePicker.setVisibility(View.VISIBLE);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        workersViewModel = new ViewModelProvider(this).get(WorkersViewModel.class);
        if(getArguments() != null){
            shiftId = getArguments().getInt(SHIFT_ID_KEY);
            User user = userViewModel.getUserState().getValue();
            workersViewModel.getFreeWorkersData(
                    user.getId(),
                    user.getAuthToken(),
                    shiftId,this::onDataArrived);
        }else{
            NavHostFragment.findNavController(WorkerFragment.this).popBackStack();

        }

        binding.btn2DatePicker.setOnClickListener(view -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Reset Shift")
                    .setMessage("Are you sure you want\n to reset this shift?")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        String uid = userViewModel.getUserState().getValue().getId();
                        String token = userViewModel.getUserState().getValue().getAuthToken();
                        workersViewModel.resetShift(uid, token, shiftId, () -> {
                        }, (valid, responseError, throwable) -> {
                            adapter.clearList();
                            workersViewModel.getFreeWorkersData(
                                    userViewModel.getUserState().getValue().getId(),
                                    userViewModel.getUserState().getValue().getAuthToken(),
                                    shiftId,this::onDataArrived);
                        });
                    })
                    .setNegativeButton("Discard", null)
                    .create().show();
        });

        return v;
    }


    @Override
    public void onDataArrived(@Nullable ArrayList<Profile> profiles, @Nullable Api.ResponseError error, @Nullable Throwable t) {
        super.onDataArrived(profiles, error, t);
        adapter.notifyItemRangeInserted(0,adapter.getItemCount());
    }

    @Override
    protected void onPickClicked(View view, String pickerValue) {

    }


    @Override
    protected void onItemClicked(Profile model, View view) {
        workersViewModel.addWorkerToShift(model.getId(), shiftId, () -> {
        }, (valid, responseError, throwable) -> {
            if(responseError != null || throwable != null){
                new AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage(responseError != null ? responseError.getMessage():"Unknown reason")
                        .setPositiveButton("Ok", null)
                        .create().show();
            }
            else if (Boolean.TRUE.equals(valid)) {
                adapter.removeEntry(model);
                adapter.clearList();
                workersViewModel.getFreeWorkersData(
                        userViewModel.getUserState().getValue().getId(),
                        userViewModel.getUserState().getValue().getAuthToken(),
                        shiftId, this::onDataArrived);
            }
        });
    }
}
