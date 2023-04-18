package com.example.myapplication;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Model.ConstraintType;
import com.example.myapplication.databinding.FragmentConstraintSubmissionBinding;

import java.util.ArrayList;
import java.util.List;

public class ConstraintSubmissionActivity extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentConstraintSubmissionBinding binding;

    private ConstraintType selectedConstraintType;

    private List<ConstraintType> createDummyConstraintTypes() {
        List<ConstraintType> constraintTypes = new ArrayList<>();

        constraintTypes.add(new ConstraintType(1, 1, "PERSONAL REASON"));
        constraintTypes.add(new ConstraintType(2, 2, "VACATION"));
        constraintTypes.add(new ConstraintType(3, 3, "SICK"));

        return constraintTypes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentConstraintSubmissionBinding.inflate(inflater, container, false);


        List<ConstraintType> constraintTypes = createDummyConstraintTypes();
        ArrayAdapter<ConstraintType> constraintTypeAdapter = new ArrayAdapter<>(requireContext(),
                                                                                android.R.layout.simple_spinner_item,
                                                                                constraintTypes);
        constraintTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.constraintTypeSpinner.setAdapter(constraintTypeAdapter);

        binding.constraintTypeSpinner.setOnItemSelectedListener(this);

        binding.addConstraintButton.setOnClickListener(this::onAddConstraint);

        return binding.getRoot();
    }

    void onAddConstraint(View v) {
        // Get the selected start and end dates
        int    startDay   = binding.startDatePicker.getDayOfMonth();
        int    startMonth = binding.startDatePicker.getMonth();
        int    startYear  = binding.startDatePicker.getYear();
        String startDate  = startYear + "-" + (startMonth + 1) + "-" + startDay;

        int    endDay   = binding.endDatePicker.getDayOfMonth();
        int    endMonth = binding.endDatePicker.getMonth();
        int    endYear  = binding.endDatePicker.getYear();
        String endDate  = endYear + "-" + (endMonth + 1) + "-" + endDay;

        boolean isPermanent = binding.permanentCheckbox.isChecked();
        String  description = binding.selfDescription.getText().toString();

        if (description.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a self description",
                           Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(requireContext(), "Constraint added successfully", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedConstraintType = (ConstraintType) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(requireContext(), "Please choose the constraint type", Toast.LENGTH_SHORT).show();
    }
}
