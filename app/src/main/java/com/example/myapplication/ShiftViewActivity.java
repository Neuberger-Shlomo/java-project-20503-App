package com.example.myapplication;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.Model.Shift;

import java.util.ArrayList;
import java.util.List;

public class ShiftViewActivity extends AppCompatActivity {
    private TableLayout shiftsTable;
    private int maxShiftsPerDay = 4; // mac shift per day for num of table rows

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_view);

        shiftsTable = findViewById(R.id.shift_table);

        // Populate dummy data for demonstration
        List<Shift> shifts = new ArrayList<>();
        for (int i = 0; i < 7 * maxShiftsPerDay; i++) {
            shifts.add(new Shift("2023-04-15",2,2,2,2,2));
        }

        populateShiftsTable(shifts);
    }

    private void populateShiftsTable(List<Shift> shifts) {
        // Set table header
        TableRow headerRow = new TableRow(this);
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (String day : daysOfWeek) {
            TextView headerTextView = new TextView(this);
            headerTextView.setText(day);
            headerTextView.setGravity(Gravity.CENTER);
            headerTextView.setBackgroundColor(Color.LTGRAY);
            headerTextView.setPadding(8, 8, 8, 8);
            headerRow.addView(headerTextView);
        }

        shiftsTable.addView(headerRow);

        // Set shift buttons
        for (int i = 0; i < maxShiftsPerDay; i++) {
            TableRow row = new TableRow(this);

            for (int j = 0; j < 7; j++) {
                Button shiftButton = new Button(this);
                int shiftIndex = i * 7 + j;
                Shift shift = shifts.get(shiftIndex);
                shiftButton.setText(shift.getStartHour() + ":00 - " + (shift.getStartHour()
                 + shift.getDuration()) % 24 + ":00");
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
                button.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple_200), PorterDuff.Mode.MULTIPLY);
                button.setTag(true);
            } else {
                button.getBackground().clearColorFilter();
                button.setTag(false);
            }
        }
    };
}