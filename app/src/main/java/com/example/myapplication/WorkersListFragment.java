package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.ViewModel.WorkersViewModel;
import com.example.myapplication.api.Api;

import java.util.ArrayList;

public class WorkersListFragment extends DateListFragment<Profile> implements SearchView.OnQueryTextListener {


    WorkersViewModel workersViewModel;
    UserViewModel    userViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding.headerDatePicker.setText("Workers List");
        workersViewModel = new ViewModelProvider(this).get(WorkersViewModel.class);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        workersViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                this::onDataArrived);
        binding.dpDatePicker.setVisibility(View.GONE);
        binding.btnDatePicker.setVisibility(View.GONE);
        setOnQueryListener(this);
        return view;

    }

    @Override
    protected void onPickClicked(View view, String pickerValue) {
    }

    @Override
    public void onDataArrived(@Nullable ArrayList<Profile> profiles,
                              @Nullable Api.ResponseError error, @Nullable Throwable t) {
        super.onDataArrived(profiles, error, t);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onItemClicked(Profile model, View view) {
        if (!userViewModel.getUserState().getValue().isSuperAdmin())
            new AlertDialog.Builder(requireContext())
                    .setTitle("More Details:")
                    .setMessage("Phone Number: " + model.getPhoneNumber() + "\n" +
                                "E-Mail: " + model.getEmail())
                    .setPositiveButton("Ok", null)
                    .create().show();
        else {
            Bundle b = new Bundle();
            b.putString(WorkerFragment.WORKER_ID_KEY, String.valueOf(model.getId()));
            NavHostFragment
                    .findNavController(WorkersListFragment.this)
                    .navigate(R.id.workerFragment, b);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.setFilter(query, ((item, s) -> item.toString().contains(s)));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.setFilter(newText, ((item, s) -> !item.toString().contains(s)));
        return false;
    }
}
