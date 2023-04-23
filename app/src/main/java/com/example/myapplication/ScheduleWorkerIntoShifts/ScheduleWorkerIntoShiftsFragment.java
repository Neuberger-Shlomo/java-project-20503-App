package com.example.myapplication.ScheduleWorkerIntoShifts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.Model.Shift;
import com.example.myapplication.R;
import com.example.myapplication.ScheduleStatus.ScheduleStatusFragment;


/**
 * This fragment is the same as {@link ScheduleStatusFragment}
 * And changs only on the header and the item clicked call back
 */
public class ScheduleWorkerIntoShiftsFragment extends ScheduleStatusFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        binding.headerDatePicker.setText("");

        return v;
    }

    @Override
    protected void onItemClicked(Shift model, View view) {
        Bundle b = new Bundle();
        b.putInt(WorkerFragment.SHIFT_ID_KEY,model.getId());
        NavHostFragment.findNavController(ScheduleWorkerIntoShiftsFragment.this)
                .navigate(R.id.ProfileListFragment,b);
    }
}
