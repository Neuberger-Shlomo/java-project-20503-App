package com.example.myapplication.ScheduleWorkerIntoShifts;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.UserMVC.Model.User;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.ViewModel.WorkersViewModel;
import com.example.myapplication.api.Api;

import java.util.ArrayList;
/**

  in this fragment:
 -display list of available workers for a certain shift
 - schedule worker to shift
 */
public class WorkerFragment extends DateListFragment<Profile> {
    // key to get the shift id from arguments

    public final static String SHIFT_ID_KEY = "shiftIdKey";
    private             int    shiftId      = -1; // initilize to no shift
    UserViewModel    userViewModel;
    WorkersViewModel workersViewModel;
    /**
     * inflate the view and initialize the components.
     *
     * @param inflater          to inflate the view
     * @param container          the parent view
     * @param savedInstanceState previous saved state (so we can restore the previous screen)
     * @return the inflated view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        binding.headerDatePicker.setText("Free Workers List");
        binding.dpDatePicker.setVisibility(View.GONE);
        binding.btnDatePicker.setText("Save Changes");
        binding.btn2DatePicker.setVisibility(View.VISIBLE);
        userViewModel    = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        workersViewModel = new ViewModelProvider(this).get(WorkersViewModel.class);
        // get the list of free workers for this shift
        if (getArguments() != null) {
            shiftId = getArguments().getInt(SHIFT_ID_KEY);
            User user = userViewModel.getUserState().getValue();
            workersViewModel.getFreeWorkersData(
                    user.getId(),
                    user.getAuthToken(),
                    shiftId, this::onDataArrived);
        } else {
            //if no shiftid given go to previous screen
            NavHostFragment.findNavController(WorkerFragment.this).popBackStack();

        }
        // handle click on the reset button
        binding.btn2DatePicker.setOnClickListener(view -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Reset Shift")
                    .setMessage("Are you sure you want\n to reset this shift?")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        String uid   = userViewModel.getUserState().getValue().getId();
                        String token = userViewModel.getUserState().getValue().getAuthToken();
                        //reset the shift in api
                        workersViewModel.resetShift(uid, token, shiftId, () -> {
                        }, (valid, responseError, throwable) -> {
                            adapter.clearList();
                            //get the list of free workers for the shift
                            workersViewModel.getFreeWorkersData(
                                    userViewModel.getUserState().getValue().getId(),
                                    userViewModel.getUserState().getValue().getAuthToken(),
                                    shiftId, this::onDataArrived);
                        });
                    })
                    .setNegativeButton("Discard", null)
                    .create().show();
        });

        return v;
    }

    /**
     * callback function - called when we get the list of free workers.
     * (update the view to show the list of workers)
     *
     * @param profiles the list of free workers
     * @param error    if error
     * @param t        if throwable
     */
    @Override
    public void onDataArrived(@Nullable ArrayList<Profile> profiles,
                              @Nullable Api.ResponseError error, @Nullable Throwable t) {
        super.onDataArrived(profiles, error, t);
        adapter.notifyItemRangeInserted(0, adapter.getItemCount());
    }
    /**
     * callback function for clicking pick date button
     * @param view         the view that we clicked
     * @param pickerValue the date picker value we choose
     */

    @Override
    protected void onPickClicked(View view, String pickerValue) {

    }
    /**
     * callback function when click on free worker from the list
     * add the selected worker to the shift
     *
     * @param model the selected worker
     * @param view  the view we clicked on
     */

    @Override
    protected void onItemClicked(Profile model, View view) {
        User user = userViewModel.getUserState().getValue();
        // add the selected worker to the shift (from the api)
        workersViewModel.addWorkerToShift(
                model.getId(),
                shiftId, user.getId(),
                user.getAuthToken(),
                () -> {
                },
                (valid, responseError, throwable) -> {
                    if (responseError != null || throwable != null) {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Error")
                                .setMessage(responseError != null ? responseError.getMessage() :
                                                    "Unknown reason")
                                .setPositiveButton("Ok", null)
                                .create().show();
                    } else if (Boolean.TRUE.equals(valid)) {
                        adapter.removeEntry(model);
                        adapter.clearList();
                        // get the updated list of free workers (from the api)
                        workersViewModel.getFreeWorkersData(
                                userViewModel.getUserState().getValue().getId(),
                                userViewModel.getUserState().getValue().getAuthToken(),
                                shiftId, this::onDataArrived);
                    }
                });
    }
}
