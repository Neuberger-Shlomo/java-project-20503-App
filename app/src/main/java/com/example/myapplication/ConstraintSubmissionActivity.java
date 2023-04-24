package com.example.myapplication;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Common.Utils.DateUtils;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.Model.ConstraintType;
import com.example.myapplication.Model.Constraints;
import com.example.myapplication.UserMVC.Model.User;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.ConstraintApi;
import com.example.myapplication.api.ConstraintTypeApi;
import com.example.myapplication.databinding.FragmentConstraintSubmissionBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ConstraintSubmissionActivity extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentConstraintSubmissionBinding binding;
    private Date                                start, end;

    private ConstraintType selectedConstraintType;

    private final MaterialDatePicker<Pair<Long, Long>> materialDatePicker =
            MaterialDatePicker.Builder.dateRangePicker().build();

    private       RequestQueue                 queue;
    private final List<ConstraintType>         constraintTypes = new ArrayList<>();
    private       ArrayAdapter<ConstraintType> constraintTypeAdapter;

    private User user;
    OneLinerAdapter<Constraints> userRecyclerAdapter = new OneLinerAdapter<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentConstraintSubmissionBinding.inflate(inflater, container, false);
        queue   = Volley.newRequestQueue(requireActivity());

        new ViewModelProvider(requireActivity()).get(UserViewModel.class).
                getUserState().observe(getViewLifecycleOwner(), u -> {
                    user = u;
                    if (user.isLoggedIn())
                        getAllConstraintsType();
                });


        materialDatePicker.addOnPositiveButtonClickListener(this::onDateChose);
        binding.btnSelectDates.setOnClickListener(
                v -> {
                    if (!materialDatePicker.isVisible() && !materialDatePicker.isAdded()) {
                        materialDatePicker
                                .show(requireActivity().getSupportFragmentManager(), "null");
                    }
                });

        constraintTypeAdapter = new ArrayAdapter<>(requireContext(),
                                                   android.R.layout.simple_spinner_item,
                                                   constraintTypes);

        constraintTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.constraintTypeSpinner.setAdapter(constraintTypeAdapter);
        binding.constraintTypeSpinner.setOnItemSelectedListener(this);

        binding.rvUserConstraints.setAdapter(userRecyclerAdapter);
        binding.rvUserConstraints.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.addConstraintButton.setOnClickListener(this::onAddConstraint);
        binding.addConstraintButton.setEnabled(false);

        userRecyclerAdapter.setOnItemClickListener(this::onOldConstrainClicked);

        return binding.getRoot();
    }

    private void onOldConstrainClicked(Constraints constraints, View view) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete the constraint")
                .setMessage("Do tou want to delete \n" + constraints.toPrettyString())
                .setPositiveButton("Yes", (dialog, which) -> deleteConstraint(constraints))
                .setNegativeButton("No", null)
                .create().show();
    }

    private void deleteConstraint(Constraints constraints) {
        userRecyclerAdapter.removeEntry(constraints);
        queue.add(
                ConstraintApi.deleteConstraint(
                        user.getId(),
                        user.getAuthToken(),
                        constraints.getId(),
                        (jsonObject, responseError, throwable) -> {
                            if (responseError != null || throwable != null)
                                Snackbar.make(requireView(), "Unable to delete the constraint",
                                              BaseTransientBottomBar.LENGTH_SHORT).show();
                            try {
                                if (jsonObject != null && !jsonObject.getBoolean("deleted")) {
                                    userRecyclerAdapter.addEntry(constraints);
                                }
                            } catch (JSONException e) {
                                new AlertDialog.Builder(requireContext())
                                        .setTitle("Failed to delete the constraint")
                                        .setMessage("Try Again?\n" + constraints.toPrettyString())
                                        .setPositiveButton("Yes",
                                                           (dialog, which) -> deleteConstraint(constraints))
                                        .setNegativeButton("No", null)
                                        .create().show();
                            }
                        }));

    }

    private void getAllConstraintsType() {
        queue.add(
                ConstraintTypeApi.getConstraintsTypes(user.getId(), user.getAuthToken(),
                                                      this::onConstraintTypeArrived));
    }

    private void addConstraint(Constraints constraints) {
        queue.add(ConstraintApi.addConstraints(user.getId(), user.getAuthToken(), constraints,
                                               this::onAddSuccess));
    }

    private void onConstraintTypeArrived(
            JSONArray jsonArray,
            Api.ResponseError responseError,
            Throwable throwable) {
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    constraintTypes.add(ConstraintType.fromJson(object));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (constraintTypes.get(0) != null) {
                    binding.constraintTypeSpinner.setSelection(0);
                    constraintTypeAdapter.notifyDataSetChanged();
                }
            }
        } else {
            Snackbar.make(requireView(), "Some error occured",
                          BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    void onAddConstraint(View v) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.requireNonNull(DateUtils.toDate(start.toString())));
        Constraints constraints = new Constraints(
                null,
                Integer.parseInt(user.getId()),
                binding.selfDescription.getText().toString(),
                calendar.get(Calendar.WEEK_OF_YEAR),
                selectedConstraintType.getId(),
                binding.permanentCheckbox.isActivated(),
                start.toString(),
                end.toString(), 0, "0");

        addConstraint(constraints);

    }

    private void onAddSuccess(JSONObject jsonObject, Api.ResponseError responseError,
                              Throwable throwable) {

        if (responseError != null || throwable != null)
            Snackbar.make(requireView(), "Unable to add your constraint",
                          BaseTransientBottomBar.LENGTH_SHORT).show();
        else {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Success")
                    .setMessage("Your " + "request " + "was " + "submitted")
                    .setPositiveButton("Another one?", ((dialog, which) -> clearAll()))
                    .setNegativeButton("Leave", (dialog, which) -> {
                        NavHostFragment.findNavController(ConstraintSubmissionActivity.this).popBackStack();
                    })
                    .show();
        }

    }

    private void clearAll() {
        start = null;
        end   = null;
        binding.permanentCheckbox.setActivated(false);
        binding.tvDates.setText("");
        binding.selfDescription.setText("");
        userRecyclerAdapter.clearList();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedConstraintType = (ConstraintType) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(requireContext(), "Please choose the constraint type", Toast.LENGTH_SHORT).show();
    }

    private void onDateChose(Pair<Long, Long> longLongPair) {
        userRecyclerAdapter.clearList();
        start = new Date(longLongPair.first);
        end   = new Date(longLongPair.second);
        Pair<String, String> datePair  = DateUtils.stringFromDialog(longLongPair);
        String               startDate = datePair.first, endDate = datePair.second;
        binding.addConstraintButton.setEnabled(true);

        binding.tvDates.setText(String.format("%s-%s", startDate, endDate));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int startWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(end);
        int endWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        getAllConstrainsInWeek(startWeek, endWeek);

    }


    private void getAllConstrainsInWeek(int startDate, int endDate) {
        queue.add(ConstraintApi.getConstraintsByWeek(
                user.getId(),
                user.getAuthToken(),
                startDate, endDate,
                this::onSetConstrainArrived));
        queue.add(ConstraintApi.getConstraintsByDate(
                user.getId(),
                user.getAuthToken(),
                startDate, endDate,
                this::onSetConstrainArrived));
    }

    private void onSetConstrainArrived(JSONArray jsonArray, Api.ResponseError responseError,
                                       Throwable throwable) {

        for (int i = 0; jsonArray != null && i < jsonArray.length(); i++) {
            try {
                Constraints c = Constraints.fromJSON(jsonArray.getJSONObject(i));
                userRecyclerAdapter.addEntry(c);
                Log.d("TAG", "onSetConstrainArrived: " + c);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
