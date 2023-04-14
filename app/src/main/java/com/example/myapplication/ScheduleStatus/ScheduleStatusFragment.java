package com.example.myapplication.ScheduleStatus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.ViewModel.ShiftsViewModel;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentScheduleStatusBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ScheduleStatusFragment extends Fragment {
    private FragmentScheduleStatusBinding binding;
    private OneLinerAdapter<Shift>        shiftsAdapter;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {

        binding = FragmentScheduleStatusBinding.inflate(inflater, container, false);

        ShiftsViewModel shiftViewModel =
                new ViewModelProvider(requireActivity()).get(ShiftsViewModel.class);
        UserViewModel userViewModel =
                new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        if (userViewModel.getUserState().getValue() == null)
            return binding.getRoot();

        shiftViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                this::onDataArrived);
        shiftsAdapter = new OneLinerAdapter<>();
        shiftsAdapter.setBindViewHolderListener(this::onShiftBind);

        binding.rvScheduleStatus.setAdapter(shiftsAdapter);

        binding.btnScheduleStatus.setOnClickListener(this::onPickClicked);
        binding.rvScheduleStatus.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onPickClicked(View v) {
        String pickedDate = binding.dpScheduleStatus.getDayOfMonth() + "-" +
                            (binding.dpScheduleStatus.getMonth() + 1) + "-" +
                            binding.dpScheduleStatus.getYear();
        shiftsAdapter.setFilter(pickedDate, (item, s) -> !item.getDate().equals(s));
    }

    void onShiftBind(Shift shift, OneLineViewHolder<Shift> holder, int position) {
        holder.setItem(shift);
        holder.setText("Shift Date: " + shift.getDate()
                       + "\nNumber Of Required Workers: " + shift.getNumOfRequiredWorkers()
                       + "\nNumber Of Scheduled Workers: " + shift.getNumOfScheduledWorkers());
    }

    void onDataArrived(@Nullable ArrayList<Shift> shifts, @Nullable Api.ResponseError error,
                       @Nullable Throwable t) {
        StringBuilder builder = new StringBuilder();
        if (shifts != null) {
            for (Shift shift : shifts) {
                shiftsAdapter.addEntry(shift, false);
            }
            return;
        }else if(error != null){
            builder.append(error.getMessage());
        }else if(t!=null){
            builder.append(t.getMessage());
        }else{
            builder.append("Unknown Error");
        }
        Snackbar.make(requireView(), builder.toString(), BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
