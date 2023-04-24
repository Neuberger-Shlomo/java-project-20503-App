package com.example.myapplication.ScheduleWorkerIntoShifts;

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
        adapter.removeEntry(model);
        workersViewModel.addWorkerToShift(model.getId(), shiftId, () -> {
        }, (valid, responseError, throwable) -> {
            if (Boolean.TRUE.equals(valid))
                NavHostFragment.findNavController(WorkerFragment.this).popBackStack();
        });
    }
}
