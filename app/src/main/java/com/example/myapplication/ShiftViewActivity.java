/*
package com.example.myapplication;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Model.Shift;

import java.util.ArrayList;
import java.util.List;

public class ShiftViewActivity extends Fragment {
    private TableLayout shiftsTable;
    private Button sendButton;
    private List<Shift> Availableshifts = new ArrayList<>();
    private List<Shift> selectedShifts = new ArrayList<>();
    private int maxShiftsPerDay = 4; // MAXIMUM shifts per day in the week (to know the number of table rows)

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_shift_view, container, false);
        shiftsTable = v.findViewById(R.id.shift_table);
        sendButton = v.findViewById(R.id.send_button);

        sendButton.setOnClickListener(sendButtonClickListener);

        for (int i = 0; i < 7 * maxShiftsPerDay; i++) {
            Availableshifts.add(new Shift("2023-04-15", 2, new ArrayList<>(), 2, 2, 2));
        }

        populateShiftsTable(Availableshifts);
        return v;
    }

    private void populateShiftsTable(List<Shift> shifts) {
        TableRow headerRow = new TableRow(requireContext());
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (String day : daysOfWeek) {
            TextView headerTextView = new TextView(requireContext());
            headerTextView.setText(day);
            headerTextView.setGravity(Gravity.CENTER);
            headerTextView.setBackgroundColor(Color.LTGRAY);
            headerTextView.setPadding(8, 8, 8, 8);
            headerRow.addView(headerTextView);
        }

        shiftsTable.addView(headerRow);

        for (int i = 0; i < maxShiftsPerDay; i++) {
            TableRow row = new TableRow(requireContext());

            for (int j = 0; j < 7; j++) {
                Button shiftButton = new Button(requireContext());
                int shiftIndex = i * 7 + j;
                Shift shift = shifts.get(shiftIndex);
                shiftButton.setText(shift.getStartHour() + ":00 - " + (shift.getStartHour() + shift.getDuration()) % 24 + ":00");
                shiftButton.setId(shift.getId());
                shiftButton.setPadding(4, 4, 4, 4);
                shiftButton.setOnClickListener(shiftButtonClickListener);
                row.addView(shiftButton);
            }
            shiftsTable.addView(row);
        }
    }

    private View.OnClickListener shiftButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            if (button.getTag() == null || !((boolean) button.getTag())) {
                button.getBackground().setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_200), PorterDuff.Mode.MULTIPLY);
                button.setTag(true);
            } else {

                button.getBackground().clearColorFilter();
                button.setTag(false);
            }
        }
    };private View.OnClickListener sendButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println("Send button clicked");
            updateSelectedShifts();
            // TO DO later on: Send the selectedShifts list to your server
            System.out.println("Selected shifts: " + selectedShifts);
        }
    };

    // Update the list of selected shifts
private void updateSelectedShifts() {
    selectedShifts.clear();
    // Change the starting index to 1 to skip the header row
    for (int i = 1; i <= maxShiftsPerDay; i++) {
        TableRow row = (TableRow) shiftsTable.getChildAt(i);

        for (int j = 0; j < 7; j++) {
            // Check if the child view is an instance of Button
            if (row.getChildAt(j) instanceof Button) {
                Button button = (Button) row.getChildAt(j);

                if (button.getTag() != null && (boolean) button.getTag()) {
                    // Add the corresponding shift to the selected Shifts list
                    int shiftIndex = (i - 1) * 7 + j; // Subtract 1 from i to account for the header row
                    selectedShifts.add(Availableshifts.get(shiftIndex));
                }
            }
        }
    }
}

    } */

package com.example.myapplication;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Model.Shift;

import java.util.ArrayList;
import java.util.List;

public class ShiftViewActivity extends Fragment {
    private TableLayout shiftsTable;
    private Button sendButton;
    private List<Shift> Availableshifts = new ArrayList<>();
    private List<Shift> selectedShifts = new ArrayList<>();
    private int maxShiftsPerDay = 4; // MAXIMUM shifts per day in the week (to know the number of table rows)

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_shift_view, container, false);
        shiftsTable = v.findViewById(R.id.shift_table);
        sendButton = v.findViewById(R.id.send_button);

        sendButton.setOnClickListener(sendButtonClickListener);

        for (int i = 0; i < 7 * maxShiftsPerDay; i++) {
            Availableshifts.add(new Shift("2023-04-15", 2, new ArrayList<>(), 2, 2, 2));
        }

        populateShiftsTable(Availableshifts);
        return v;
    }

    private void populateShiftsTable(List<Shift> shifts) {
        TableRow headerRow = new TableRow(requireContext());
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (String day : daysOfWeek) {
            TextView headerTextView = new TextView(requireContext());
            headerTextView.setText(day);
            headerTextView.setGravity(Gravity.CENTER);
            headerTextView.setBackgroundColor(Color.LTGRAY);
            headerTextView.setPadding(8, 8, 8, 8);
            headerRow.addView(headerTextView);
        }

        shiftsTable.addView(headerRow);

        for (int i = 0; i < maxShiftsPerDay; i++) {
            TableRow row = new TableRow(requireContext());

            for (int j = 0; j < 7; j++) {
                Button shiftButton = new Button(requireContext());
                int shiftIndex = i * 7 + j;
                Shift shift = shifts.get(shiftIndex);
                shiftButton.setText(shift.getStartHour() + ":00 - " + (shift.getStartHour() + shift.getDuration()) % 24 + ":00");
                shiftButton.setId(shift.getId());
                shiftButton.setPadding(4, 4, 4, 4);
                shiftButton.setOnClickListener(shiftButtonClickListener);
                row.addView(shiftButton);
            }
            shiftsTable.addView(row);
        }
    }

    private View.OnClickListener shiftButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            if (button.getTag() == null || !((boolean) button.getTag())) {
                button.getBackground().setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_200), PorterDuff.Mode.MULTIPLY);
                button.setTag(true);
            } else {

                button.getBackground().clearColorFilter();
                button.setTag(false);
            }
        }
    };private View.OnClickListener sendButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println("Send button clicked");
            updateSelectedShifts();
            // TO DO later on: Send the selectedShifts list to your server
            System.out.println("Selected shifts: " + selectedShifts);
        }
    };

    // Update the list of selected shifts
    private void updateSelectedShifts() {
        selectedShifts.clear();
        // Change the starting index to 1 to skip the header row
        for (int i = 1; i <= maxShiftsPerDay; i++) {
            TableRow row = (TableRow) shiftsTable.getChildAt(i);

            for (int j = 0; j < 7; j++) {
                // Check if the child view is an instance of Button
                if (row.getChildAt(j) instanceof Button) {
                    Button button = (Button) row.getChildAt(j);

                    if (button.getTag() != null && (boolean) button.getTag()) {
                        // Add the corresponding shift to the selected Shifts list
                        int shiftIndex = (i - 1) * 7 + j; // Subtract 1 from i to account for the header row
                        selectedShifts.add(Availableshifts.get(shiftIndex));
                    }
                }
            }
        }
    }

}