package com.example.myapplication.ScheduleWorkerIntoShifts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.Model.Shift;
import com.example.myapplication.R;
import com.example.myapplication.ScheduleStatusFragment;


/**
 * this class for fragment that schedule worker to shift
 * This fragment is the same as {@link ScheduleStatusFragment}
 * And changes only on the header and the item clicked call back
 */
public class ScheduleWorkerIntoShiftsFragment extends ScheduleStatusFragment {
    //handle fregment created, inflate view and intilize datepicker
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        binding.headerDatePicker.setText("Schedule Worker Into Shift");

        return v;
    }
    //handle when a shift is choosed
    @Override
    protected void onItemClicked(Shift model, View view) {
        Bundle b = new Bundle(); //bundle = class to store date in key-value pairs
        b.putInt(WorkerFragment.SHIFT_ID_KEY,model.getId());  // store the Shift ID in the bundle using a specific key
        NavHostFragment.findNavController(ScheduleWorkerIntoShiftsFragment.this)
                .navigate(R.id.ProfileListFragment,b);// navigate to the ProfileListFragment, passing the bundle as an argument
    }
}
