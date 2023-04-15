package com.example.myapplication.WorkersConstrains;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.Model.Constraints;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.ViewModel.WorkersConstrainsViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentDatePickingBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class WorkersConstrainsFragment extends Fragment {

    private FragmentDatePickingBinding binding;

    private final OneLinerAdapter<Constraints> adapter = new OneLinerAdapter<>();



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {

        binding = FragmentDatePickingBinding.inflate(inflater, container, false);
        binding.headerDatePicker.setText("Workers Constrains");

        WorkersConstrainsViewModel workersConstrainsViewModel =
                new ViewModelProvider(requireActivity()).get(WorkersConstrainsViewModel.class);
        UserViewModel userViewModel =
                new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding.btnDatePicker.setOnClickListener(this::onPickClicked);
        if (userViewModel.getUserState().getValue() == null)
            return binding.getRoot();
        workersConstrainsViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                this::onDataArrived);

        binding.rvDatePicker.setAdapter(adapter);
        adapter.setBindViewHolderListener(this::onViewBind);
        binding.rvDatePicker.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Handles the binding of a shift to the holder
     *
     * @param constraints of the current view
     * @param holder      of the current view
     * @param position    of the shift in the list
     */
    private void onViewBind(Constraints constraints, OneLineViewHolder<Constraints> holder,
                            int position) {
        holder.setItem(constraints);
        holder.setText("Full Name: " + constraints.getFirstName() + "\t\t" +
                       constraints.getLastName()
                       + "\nPhone Number: " + constraints.getPhoneNumber()
                       + "\nConstrain start Date: " + constraints.getStartDate());
        holder.setOnClickListener(this::onItemClicked);
    }

    /**
     * Handles the holder item clicked
     *
     * @param item of the calling view holder
     * @param view of the caller
     */
    private void onItemClicked(Constraints item, View view) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Worker details:")
                .setMessage(item.toPrettyString())
                .setPositiveButton("Ok", null)
                .create().show();
    }

    /**
     * Handles the pick date action
     *
     * @param view of the clicked the button
     */
    private void onPickClicked(View view) {
        String pickedDate = binding.dpDatePicker.getYear() + "-" +
                            +0 + (binding.dpDatePicker.getMonth() + 1) + "-" +
                            binding.dpDatePicker.getDayOfMonth();
        adapter.setFilter(pickedDate, (item, s) -> item.getStartDate().equals(s));

    }

    /**
     * Handles the getData call
     *
     * @param constraints received form the server
     * @param error       error received from the server
     * @param t           thrown by the Volley or parsing
     */
    void onDataArrived(@Nullable ArrayList<Constraints> constraints,
                       @Nullable Api.ResponseError error,
                       @Nullable Throwable t) {
        StringBuilder builder = new StringBuilder();
        if (constraints != null) {
            for (Constraints constraint : constraints) {
                adapter.addEntry(constraint, false);
            }
            return;
        } else if (error != null) {
            builder.append(error.getMessage());
        } else if (t != null) {
            builder.append(t.getMessage());
        } else {
            builder.append("Unknown Error");
        }
        Snackbar.make(requireView(), builder.toString(), BaseTransientBottomBar.LENGTH_SHORT).show();
    }


}
