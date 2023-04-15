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

import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.Model.Constraints;
import com.example.myapplication.Model.ShiftRequest;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.ViewModel.WorkersConstrainsViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentDatePickingBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class WorkersConstrainsFragment extends DateListFragment<Constraints> {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding.headerDatePicker.setText("Workers Constrains");

        WorkersConstrainsViewModel workersConstrainsViewModel =
                new ViewModelProvider(this).get(WorkersConstrainsViewModel.class);
        UserViewModel userViewModel =
                new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        if (userViewModel.getUserState().getValue() == null)
            return view;

        workersConstrainsViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                this::onDataArrived);

        return view;
    }

    @Override
    protected void onPickClicked(View view, String pickerValue) {
        adapter.setFilter(pickerValue, (item, s) -> item.getStartDate().equals(s));
    }

    @Override
    protected void onItemClicked(Constraints model, View view) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Worker details:")
                .setMessage(
                        "Type: " + model.getConstType() +
                        "\nConstrain: " + model.getData() +
                        "\nis permanent: " + model.isPermanent() +
                        "\nEnd Date: " + model.getEndDate())
                .setPositiveButton("Ok", null)
                .create().show();
    }

}
