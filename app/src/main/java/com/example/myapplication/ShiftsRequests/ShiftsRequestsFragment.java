package com.example.myapplication.ShiftsRequests;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.Model.ShiftRequest;
import com.example.myapplication.R;
import com.example.myapplication.ViewModel.ShiftRequestViewModel;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentDatePickingBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class ShiftsRequestsFragment extends Fragment {

    private FragmentDatePickingBinding binding;

    private ShiftRequestViewModel shiftRequestViewModel;
    private UserViewModel         userViewModel;

    private OneLinerAdapter<ShiftRequest> adapter = new OneLinerAdapter<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDatePickingBinding.inflate(inflater, container, false);

        binding.headerDatePicker.setText("Shifts Requests");

        shiftRequestViewModel =
                new ViewModelProvider(requireActivity()).get(ShiftRequestViewModel.class);
        userViewModel         = new ViewModelProvider(requireActivity()).get(UserViewModel.class);


        shiftRequestViewModel.getData(userViewModel.getUserState().getValue().getId(),
                                      userViewModel.getUserState().getValue().getAuthToken(),
                                      this::onDataArrived);

        adapter.setBindViewHolderListener(this::onViewBind);
        binding.rvDatePicker.setAdapter(adapter);
        binding.rvDatePicker.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.btnDatePicker.setOnClickListener(this::onPickerDate);

        return binding.getRoot();
    }

    private void onDataArrived(ArrayList<ShiftRequest> shiftRequests, Api.ResponseError error,
                               Throwable t) {
        StringBuilder builder = new StringBuilder();
        if (shiftRequests != null) {
            for (ShiftRequest request : shiftRequests) {
                adapter.addEntry(request, false);
            }
        } else if (error != null) {
            builder.append(error.getMessage());
        } else if (t != null) {
            builder.append(t.getMessage());
        } else {
            builder.append("Unknown Error");
        }
        Snackbar.make(requireView(), builder.toString(), BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    private void onPickerDate(View view) {
        String pickedDate =
                new StringBuilder()
                        .append(binding.dpDatePicker.getDayOfMonth())
                        .append("-")
                        .append(binding.dpDatePicker.getMonth() + 1)
                        .append("-")
                        .append(binding.dpDatePicker.getYear())
                        .toString();
        adapter.setFilter(pickedDate, ((item, s) -> item.getShiftDate().equals(s)));


    }

    private void onViewBind(ShiftRequest shiftRequest, OneLineViewHolder<ShiftRequest> holder,
                            int position) {
        holder.setItem(shiftRequest);
        holder.setText(shiftRequest.toPrettyString());
        holder.setOnClickListener(this::onItemClicked);
    }

    private void onItemClicked(ShiftRequest request, View view) {
        String requestMessage =
                "Request Timestamp: " + request.getTimestamp() + "\nShift Start Hour: " +
                request.getStartHour() + "\nShift Duration: " +
                request.getDuration();

        new AlertDialog.Builder(requireContext())
                .setTitle("More Info:")
                .setMessage(requestMessage)
                .setPositiveButton("Ok", null)
                .create().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
