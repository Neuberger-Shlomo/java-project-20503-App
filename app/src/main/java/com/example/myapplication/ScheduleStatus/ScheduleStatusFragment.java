package com.example.myapplication.ScheduleStatus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.Model.Profile;
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
    private ShiftsViewModel               shiftViewModel;
    private UserViewModel                 userViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {

        binding = FragmentScheduleStatusBinding.inflate(inflater, container, false);

        shiftViewModel = new ViewModelProvider(requireActivity()).get(ShiftsViewModel.class);
        userViewModel  = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        if (userViewModel.getUserState().getValue() == null)
            return binding.getRoot();

        shiftViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                this::onDataArrived);

        shiftsAdapter = new OneLinerAdapter<>();
        shiftsAdapter.setBindViewHolderListener(this::onShiftBind);
        binding.rvScheduleStatus.setAdapter(shiftsAdapter);
        binding.rvScheduleStatus.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.btnScheduleStatus.setOnClickListener(this::onPickClicked);
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Handles the pick date action
     *
     * @param view of the clicked the button
     */
    public void onPickClicked(View view) {
        String pickedDate = binding.dpScheduleStatus.getDayOfMonth() + "-" +
                            (binding.dpScheduleStatus.getMonth() + 1) + "-" +
                            binding.dpScheduleStatus.getYear();
        shiftsAdapter.setFilter(pickedDate, (item, s) -> !item.getDate().equals(s));
    }

    /**
     * Handles the binding of a shift to the holder
     *
     * @param shift    of the current view
     * @param holder   of the current view
     * @param position of the shift in the list
     */
    void onShiftBind(Shift shift, OneLineViewHolder<Shift> holder, int position) {
        holder.setItem(shift);
        holder.setText("Shift Date: " + shift.getDate()
                       + "\nNumber Of Required Workers: " + shift.getNumOfRequiredWorkers()
                       + "\nNumber Of Scheduled Workers: " + shift.getNumOfScheduledWorkers());
        holder.setOnClickListener(this::onItemClicked);
    }

    /**
     * Handles the holder item clicked
     *
     * @param shift of the calling view holder
     * @param view  of the caller
     */
    private void onItemClicked(Shift shift, View view) {
        if (userViewModel.getUserState().getValue() == null)
            return;
        shiftViewModel.getProfileFromSchedule(userViewModel.getUserState().getValue().getId(),
                                              userViewModel.getUserState().getValue().getAuthToken(),
                                              shift.getId(),
                                              this::onPostProfileCall);
    }

    /**
     * Handles the getProfile call
     *
     * @param profiles      received form the server
     * @param responseError error received from the server
     * @param throwable     thrown by the Volley or parsing
     */
    private void onPostProfileCall(ArrayList<Profile> profiles,
                                   Api.ResponseError responseError,
                                   Throwable throwable) {
        if (profiles == null)
            return;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < profiles.size(); i++) {
            Profile p = profiles.get(i);
            stringBuilder.append("").append(i + 1).append(". ").append(p.toPrettyString()).append(
                    "\n");
        }
        String msg = stringBuilder.toString();
        new AlertDialog.Builder(requireContext())
                .setTitle("Workers in Shift:")
                .setMessage(msg.isEmpty() ? "No employs" : msg)
                .setPositiveButton("Ok", null)
                .create().show();
    }

    /**
     * Handles the getData call
     *
     * @param shifts received form the server
     * @param error  error received from the server
     * @param t      thrown by the Volley or parsing
     */
    void onDataArrived(@Nullable ArrayList<Shift> shifts, @Nullable Api.ResponseError error,
                       @Nullable Throwable t) {
        StringBuilder builder = new StringBuilder();
        if (shifts != null) {
            for (Shift shift : shifts) {
                shiftsAdapter.addEntry(shift, false);
            }
            return;
        } else if (error != null) {
            builder.append(error.getMessage());
        } else if (t != null) {
            builder.append(t.getMessage());
        } else {
            builder.append("Unknown Error");
        }
        Snackbar.make(requireView(), builder.toString(), BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
