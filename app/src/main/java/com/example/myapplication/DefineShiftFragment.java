package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Model.Shift;
import com.example.myapplication.UserMVC.Model.User;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.ViewModel.ShiftsViewModel;
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

        binding.tpDefineShift.setOnTimeChangedListener((picker, h, m) -> picker.setMinute(0));


        return binding.getRoot();
    }

    private void onDefineShiftClicked(View view) {
        String pickedDate = binding.dpDefineShift.getDayOfMonth() + "-" +
                            (binding.dpDefineShift.getMonth() + 1) + "-" +
                            binding.dpDefineShift.getYear();
        int              pickedDay        = binding.dpDefineShift.getDayOfMonth();
        int              pickedMonth      = binding.dpDefineShift.getMonth() + 1;
        int              pickedYear       = binding.dpDefineShift.getYear();
        Editable         defineShiftText  = binding.tfDefineShift.getText();
        Editable         defineShift3Text = binding.tfDefineShift3.getText();
        ArrayList<Shift> shifts           = shiftViewModel.getShiftstate().getValue();
        if (defineShiftText == null)
            // TODO: Alert the user about the error
            return;
        if (defineShift3Text == null)
            // TODO: Alert the user about the error
            return;
        if (shifts == null)
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
            int     hour         = binding.tpDefineShift.getHour();
            int     id           = (int) Math.random();
            boolean overlayFlag  = false;
            boolean validityFlag = true;


            Calendar         c    = Calendar.getInstance();
            SimpleDateFormat df   = new SimpleDateFormat("dd-MM-yyyy");
            Date             date = null;
            try {
                date = df.parse(pickedDate);
            } catch (ParseException e) {
                //TODO: Don't crash the APP here
                throw new RuntimeException(e);
            }
            c.setTime(new Date(System.currentTimeMillis()));
            int weekNumber            = c.get(Calendar.WEEK_OF_YEAR);
            int dayNumber             = c.get(Calendar.DAY_OF_WEEK);
            int numOfScheduledWorkers = 0;

            int starTestedHour = hour, endTestedHour = hour + duration;
            for (Shift s : shifts) {
                boolean isSameDate = Objects.equals(s.getShiftDate(), pickedDate);
                boolean isHourOverLap =
                        // is to be created shift inside other
                        s.getStartHour() <= starTestedHour && starTestedHour <= s.getStartHour() + s.getDuration()
                        ||
                        s.getStartHour() <= endTestedHour && endTestedHour <= s.getStartHour() + s.getDuration()
                        ||
                        // is the shift in side the to be created
                        starTestedHour <= s.getStartHour() && s.getStartHour() <= endTestedHour
                        ||
                        starTestedHour <= s.getStartHour() + s.getDuration() && s.getStartHour() + s.getDuration() <= endTestedHour;

                if (isSameDate
                    && isHourOverLap) {
                    overlayFlag = true;
                    Snackbar.make(view, "Shift Already Exists For This Time!\nCan't " +
                                        "Overlay Shifts", Snackbar.LENGTH_LONG)
                            .setAction("Ok", null).show();
                }
            }

            if (pickedDay == c.get(Calendar.DAY_OF_MONTH)
                && pickedMonth == c.get(Calendar.MONTH)
                && pickedYear == c.get(Calendar.YEAR)
                && hour <= c.get(Calendar.HOUR_OF_DAY)) {
                Snackbar.make(view, "Can't Set Shift To This Hour!",
                              Snackbar.LENGTH_LONG)
                        .setAction("Ok", null).show();
                validityFlag = false;
            }


            if (!overlayFlag && validityFlag) {
                User user = userViewModel.getUserState().getValue();
                shiftViewModel.addShift(new Shift(pickedDate, numOfRequiredWorkers, id,
                                                  hour,
                                                  duration, weekNumber, dayNumber,
                                                  numOfScheduledWorkers), user.getId(),
                                        user.getAuthToken(), () -> {
                        }, (valid, responseError, throwable) -> {
                            if (Boolean.TRUE.equals(valid))
                                Snackbar.make(view, "Shift defined Successfully!",
                                              Snackbar.LENGTH_LONG)
                                        .setAction("Ok", null).show();
                        });

            }

        }
    }
}
