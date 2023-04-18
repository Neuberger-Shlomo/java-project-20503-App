package com.example.myapplication;

// Import necessary classes for the activity

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.ConstraintType;

import java.util.ArrayList;
import java.util.List;

// The ConstraintSubmissionActivity class extends AppCompatActivity, making it an Android activity
public class ConstraintSubmissionActivity extends AppCompatActivity {

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

    // The onCreate method is called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_submission);

        // Initialize UI elements
        startDatePicker = findViewById(R.id.start_date_picker);
        endDatePicker = findViewById(R.id.end_date_picker);
        permanentCheckbox = findViewById(R.id.permanent_checkbox);
        selfDescription = findViewById(R.id.self_description);
        constraintTypeSpinner = findViewById(R.id.constraint_type_spinner);
        addConstraintButton = findViewById(R.id.add_constraint_button);

        // Create the list of ConstraintType objects and set up an ArrayAdapter for the spinner
        List<ConstraintType> constraintTypes = createDummyConstraintTypes();
        ArrayAdapter<ConstraintType> constraintTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, constraintTypes);
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
                Toast.makeText(ConstraintSubmissionActivity.this, "Please choose the constraint type", Toast.LENGTH_SHORT).show();

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
                String endDate = endYear + "-" + (endMonth + 1) + "-" + endDay;

                // Get the other input values
                boolean isPermanent = permanentCheckbox.isChecked();
                String description = selfDescription.getText().toString();

                // Check if the description field is empty, and if so, show message and return (without creating constraint)
                if (description.isEmpty()) {
                    Toast.makeText(ConstraintSubmissionActivity.this, "Please enter a self description", Toast.LENGTH_SHORT).show();
                    return;
                }

                //later to add ----here i need to create constraint object (row) and add it to the database
                //ater to add ---- addConstraintToDatabase(startDate, endDate, isPermanent, description, selectedConstraintType, userId);

                // show that the constraint was add
                Toast.makeText(ConstraintSubmissionActivity.this, "Constraint added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}