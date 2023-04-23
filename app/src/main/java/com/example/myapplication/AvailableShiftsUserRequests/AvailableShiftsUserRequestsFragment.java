
package com.example.myapplication.AvailableShiftsUserRequests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.DefineShiftFragment;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.Model.ShiftRequest;
import com.example.myapplication.User.Model.BasicUser;
import com.example.myapplication.User.Model.UserViewModel;
import com.example.myapplication.ViewModel.ShiftRequestViewModel;
import com.example.myapplication.ViewModel.ShiftsViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentAvailableShiftsUserRequestsBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AvailableShiftsUserRequestsFragment extends DateListFragment<Shift> {
    private FragmentAvailableShiftsUserRequestsBinding binding;

    // communicate with the server . import shifts
    private ShiftsViewModel       shiftViewModel;
    private ShiftRequestViewModel shiftRequestViewModel;

    //  communicate with the server, show which user is logged in
    private UserViewModel userViewModel;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {

        super.onCreateView(inflater, container, savedInstanceState);

        shiftViewModel        = new ViewModelProvider(requireActivity()).get(ShiftsViewModel.class);
        shiftRequestViewModel =
                new ViewModelProvider(requireActivity()).get(ShiftRequestViewModel.class);
        userViewModel         = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        adapter.setBindViewHolderListener(this::onShiftBind);
        shiftViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                this::onDataArrived);

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
    @Override
    protected void onPickClicked(View view, String pickerValue) {

        //insert the date from pickdate to the string pickedDate
        String pickedDate = binding.dpAvailableShiftsUserRequests.getDayOfMonth() + "-" +
                            (binding.dpAvailableShiftsUserRequests.getMonth() + 1) + "-" +
                            binding.dpAvailableShiftsUserRequests.getYear();
        //show only shifts equal to the picked date
        adapter.setFilter(pickedDate, (item, s) -> !item.getDate().equals(s));
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

    @Override
    protected void onItemClicked(Shift shift, View view) {
        if (userViewModel.getUserState().getValue() == null)
            return;
        String suid    = userViewModel.getUserState().getValue().getId();
        int    uid     = Integer.parseInt(suid);
        String token   = userViewModel.getUserState().getValue().getAuthToken();
        int    shiftId = shift.getId();

        shiftRequestViewModel.addShiftRequest(new ShiftRequest(shiftId, uid), suid, token, () -> {
        }, (valid, responseError, throwable) -> {
            if (Boolean.TRUE.equals(valid))
                NavHostFragment.findNavController(AvailableShiftsUserRequestsFragment.this).popBackStack();
        });
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


}