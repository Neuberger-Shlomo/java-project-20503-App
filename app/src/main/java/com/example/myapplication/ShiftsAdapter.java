package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Shift;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.ViewHolder> {

    private final ArrayList<Shift> shifts;

    public ShiftsAdapter(ArrayList<Shift> shifts) {
        this.shifts = shifts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shift shift = shifts.get(position);
        String formattedDate = formatShiftDate(shift.getShiftDate());
        holder.shiftDate.setText(formattedDate);
        holder.shiftDate.setText(shift.getShiftDate());
        holder.shiftTime.setText(shift.getStartHour() + ":00 - " + (shift.getStartHour() + shift.getDuration()) + ":00");
        holder.scheduledWorkers.setText("Scheduled Workers: " + shift.getNumOfScheduledWorkers() + "/" + shift.getNumOfRequiredWorkers());
    }

        @Override
    public int getItemCount() {
        return shifts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView shiftDate;
        private final TextView shiftTime;
        private final TextView scheduledWorkers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shiftDate = itemView.findViewById(R.id.shift_date);
            shiftTime = itemView.findViewById(R.id.shift_time);
            scheduledWorkers = itemView.findViewById(R.id.scheduled_workers);
        }
    }
    private String formatShiftDate(String shiftDateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, yyyy-MM-dd");

        try {
            Date shiftDate = inputFormat.parse(shiftDateString);
            return outputFormat.format(shiftDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return shiftDateString;
        }
    }
}


