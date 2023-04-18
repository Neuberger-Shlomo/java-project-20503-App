package com.example.myapplication;

// Import necessary classes for the activity

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Model.ConstraintType;

import java.util.ArrayList;
import java.util.List;

// The ConstraintSubmissionActivity class now extends Fragment
public class ConstraintSubmissionActivity extends Fragment {

    // Declare UI elements as private members
    private DatePicker startDatePicker, endDatePicker;
    private CheckBox permanentCheckbox;
    private EditText selfDescription;
    private Spinner constraintTypeSpinner;
    private Button addConstraintButton;

    private ConstraintType selectedConstraintType;

    // This function creates a list of dummy ConstraintType objects for demonstration purposes
    private List<ConstraintType> createDummyConstraintTypes() {
        List<ConstraintType> constraintTypes = new ArrayList<>();

        constraintTypes.add(new ConstraintType(1, 1, "PERSONAL REASON"));
        constraintTypes.add(new ConstraintType(2, 2, "VACATION"));
        constraintTypes.add(new ConstraintType(3, 3, "SICK"));

        return constraintTypes;
    }

    // Override onCreateView to inflate the layout for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment using the inflater
        View view = inflater.inflate(R.layout.activity_constraint_submission, container, false);

        // Initialize UI elements with the view
        startDatePicker = view.findViewById(R.id.start_date_picker);
        endDatePicker = view.findViewById(R.id.end_date_picker);
        permanentCheckbox = view.findViewById(R.id.permanent_checkbox);
        selfDescription = view.findViewById(R.id.self_description);
        constraintTypeSpinner = view.findViewById(R.id.constraint_type_spinner);
        addConstraintButton = view.findViewById(R.id.add_constraint_button);

        // Create the list of ConstraintType objects and set up an ArrayAdapter for the spinner
        List<ConstraintType> constraintTypes = createDummyConstraintTypes();
        ArrayAdapter<ConstraintType> constraintTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, constraintTypes);
        constraintTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        constraintTypeSpinner.setAdapter(constraintTypeAdapter);

        // Set an OnItemSelectedListener for the spinner to update the selectedConstraintType variable when the user selects an item
        constraintTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedConstraintType = (ConstraintType) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(requireContext(), "Please choose the constraint type", Toast.LENGTH_SHORT).show();
            }
        });

        // Set an OnClickListener for the addConstraintButton to validate the inputs and create a new constraint object
        addConstraintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected start and end dates
                int startDay = startDatePicker.getDayOfMonth();
                int startMonth = startDatePicker.getMonth();
                int startYear = startDatePicker.getYear();
                String startDate = startYear + "-" + (startMonth + 1) + "-" + startDay;

                int endDay = endDatePicker.getDayOfMonth();
                int endMonth = endDatePicker.getMonth();
                int endYear = endDatePicker.getYear();
                String endDate = endYear + "-" + (endMonth +1)+"-" + endDay;
                // Get the other input values
                boolean isPermanent = permanentCheckbox.isChecked();
                String description = selfDescription.getText().toString();

                // Check if the description field is empty, and if so, show message and return (without creating constraint)
                if (description.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter a self description", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Later to add: Create constraint object (row) and add it to the database
                // addConstraintToDatabase(startDate, endDate, isPermanent, description, selectedConstraintType, userId);

                // Show that the constraint was added
                Toast.makeText(requireContext(), "Constraint added successfully", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
