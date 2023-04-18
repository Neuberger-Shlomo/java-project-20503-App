package com.example.myapplication.ScheduleWorkerIntoShifts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.EntryFragment;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.R;
import com.example.myapplication.ScheduleStatus.ScheduleStatusFragment;
import com.example.myapplication.ViewModel.ShiftsViewModel;
import com.example.myapplication.User.Model.UserViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;


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
