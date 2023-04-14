package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Shift;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ShiftsActivity extends AppCompatActivity {

    private TextView weekTitle;
    private RecyclerView shiftsRecyclerView;
    private ShiftsAdapter shiftsAdapter;

    private Button previousWeekButton;
    private Button nextWeekButton;
    private Calendar currentWeekStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);

        weekTitle = findViewById(R.id.week_title);
        shiftsRecyclerView = findViewById(R.id.shifts_recycler_view);

        // Set week title
        String weekStart = "2023-04-10";
        weekTitle.setText("Week: " + weekStart);

        // example shift data
        ArrayList<Shift> shifts = new ArrayList<>();
        shifts.add(new Shift("2023-04-10", 2, 1, 9, 8));
        shifts.add(new Shift("2023-04-11", 3, 2, 14, 8));
        shifts.add(new Shift("2023-04-12", 1, 3, 18, 4));

        // Set up the RecyclerView and adapter
        shiftsAdapter = new ShiftsAdapter(shifts);
        shiftsRecyclerView.setAdapter(shiftsAdapter);



        previousWeekButton = findViewById(R.id.previous_week_button);
        nextWeekButton = findViewById(R.id.next_week_button);

        currentWeekStart = Calendar.getInstance();
        currentWeekStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        updateWeekTitle();



    }

    // handle button clicks
    public void onPreviousWeekClicked(View view) {
        currentWeekStart.add(Calendar.DATE, -7);
        updateWeekTitle();
        // Update the shift data for the new week FROM THE POSTGRES DB
    }

    public void onNextWeekClicked(View view) {
        currentWeekStart.add(Calendar.DATE, 7);
        updateWeekTitle();
        // Update the shift data for the new week FROM THE POSTGRES DB
         }

    private void updateWeekTitle() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String weekStart = sdf.format(currentWeekStart.getTime());
        weekTitle.setText("Week: " + weekStart);
    }
}
