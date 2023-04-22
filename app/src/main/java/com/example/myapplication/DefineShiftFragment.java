package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.Model.Shift;
import com.example.myapplication.ViewModel.ShiftsViewModel;
import com.example.myapplication.User.Model.UserViewModel;
import com.example.myapplication.databinding.FragmentDefineShiftBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class DefineShiftFragment extends Fragment {

    private static final String                     TAG = "DefineShiftFragment";
    private              FragmentDefineShiftBinding binding;

    ShiftsViewModel shiftViewModel;
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {
        binding = FragmentDefineShiftBinding.inflate(inflater, container, false);

        shiftViewModel = new ViewModelProvider(requireActivity()).get(ShiftsViewModel.class);
        userViewModel  = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        shiftViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                (shifts, responseError, throwable) -> {
                });

        binding.tpDefineShift.setIs24HourView(true);
        binding.dpDefineShift.setMinDate(System.currentTimeMillis());

        binding.btnDefineShifts.setOnClickListener(this::onDefineShiftClicked);

        return binding.getRoot();
    }

    private void onDefineShiftClicked(View view) {
        String pickedDate = binding.dpDefineShift.getDayOfMonth() + "-" +
                            (binding.dpDefineShift.getMonth() + 1) + "-" +
                            binding.dpDefineShift.getYear();
        Editable defineShiftText = binding.tfDefineShift.getText();
        Editable defineShift3Text = binding.tfDefineShift.getText();
        ArrayList<Shift> shifts = shiftViewModel.getShiftstate().getValue();
        if(defineShiftText == null)
            // TODO: Alert the user about the error
            return;
        if(defineShift3Text == null)
            // TODO: Alert the user about the error
            return;
        if(shifts == null)
            // TODO: Alert the user about the error
            return;
        if (defineShiftText.toString().matches("")) {
            Snackbar.make(view, "Please Enter Number Of Required Workers!",
                          Snackbar.LENGTH_LONG)
                    .setAction("Ok", null).show();
        }

        if (defineShift3Text.toString().matches("")) {
            Snackbar.make(view, "Please Enter The Shift Duration!", Snackbar.LENGTH_LONG)
                    .setAction("Ok", null).show();
        }

        if (!defineShift3Text.toString().matches("") &&
            !defineShiftText.toString().matches("")) {
            int numOfRequiredWorkers =
                    Integer.parseInt(defineShiftText.toString());
            int duration =
                    Integer.parseInt(defineShift3Text.toString());
            int     hour = binding.tpDefineShift.getHour();
            int     id   = (int) Math.random();
            boolean flag = false;


            Calendar         c    = Calendar.getInstance();
            SimpleDateFormat df   = new SimpleDateFormat("dd-MM-yyyy");
            Date             date = null;
            try {
                date = df.parse(pickedDate);
            } catch (ParseException e) {
                //TODO: Don't crash the APP here
                throw new RuntimeException(e);
            }
            c.setTime(date);
            int weekNumber            = c.get(Calendar.WEEK_OF_YEAR);
            int dayNumber             = c.get(Calendar.DAY_OF_WEEK);
            int numOfScheduledWorkers = 0;

            for (Shift s : shifts) {
                if (Objects.equals(s.getShiftDate(), pickedDate)
                    && (s.getStartHour() <= hour + duration || s.getStartHour() >= hour - duration)) {
                    flag = true;
                }
            }
            if (hour <= c.get(Calendar.HOUR_OF_DAY)) {
                flag = true;
            }

            if (!flag) {
                shiftViewModel.addShift(new Shift(pickedDate, numOfRequiredWorkers, id,
                                                  hour,
                                                  duration, weekNumber, dayNumber,
                                                  numOfScheduledWorkers), () -> {
                }, (valid, responseError, throwable) -> {
                    if (Boolean.TRUE.equals(valid))
                        NavHostFragment.findNavController(DefineShiftFragment.this).popBackStack();
                });

            } else {
                if (hour <= c.get(Calendar.HOUR_OF_DAY)) {
                    Snackbar.make(view, "Can't Set Shift To This Hour!",
                                  Snackbar.LENGTH_LONG)
                            .setAction("Ok", null).show();
                } else
                    Snackbar.make(view, "Shift Already Exists For This Time!\nCan't " +
                                        "Overlay Shifts", Snackbar.LENGTH_LONG)
                            .setAction("Ok", null).show();
            }

        }
    }
}
