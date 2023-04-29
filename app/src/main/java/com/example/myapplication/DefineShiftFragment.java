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

/**
 * DefineShiftFragment:
 * this fragment manager define a new shift. (and add it to the available shifts)
 */
public class DefineShiftFragment extends Fragment {

    private static final String                     TAG = "DefineShiftFragment";
    ShiftsViewModel shiftViewModel;
    private              FragmentDefineShiftBinding binding;
    private UserViewModel userViewModel;

    /**
     * inflate the view  and set the listeners
     *
     * @param inflater
     * @param container          the parent view
     * @param savedInstanceState saved previous state. so we can restore it.
     * @return the View for the fregment
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {
        binding = FragmentDefineShiftBinding.inflate(inflater, container, false);

        shiftViewModel = new ViewModelProvider(requireActivity()).get(ShiftsViewModel.class);
        userViewModel  = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        // get shifts from the server
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

    /**
     * handle event: manager click on define Shift button
     *
     * @param view The view where we clicked the button
     */
    private void onDefineShiftClicked(View view) {
        String pickedDate = binding.dpDefineShift.getDayOfMonth() + "-" +
                            (binding.dpDefineShift.getMonth() + 1) + "-" +
                            binding.dpDefineShift.getYear();
        int pickedDay   = binding.dpDefineShift.getDayOfMonth();
        int pickedMonth = binding.dpDefineShift.getMonth() + 1;
        int pickedYear  = binding.dpDefineShift.getYear();
        //field "insert number of employees required"
        Editable defineShiftText = binding.tfDefineShift.getText();
        //field "insert shift duration"
        Editable defineShift3Text = binding.tfDefineShift3.getText();
        //gets the current state of shifts data from the ViewModel and put it in an ArrayList
        ArrayList<Shift> shifts = shiftViewModel.getShiftstate().getValue();
        if (defineShiftText == null)
            // TODO: Alert the user about the error
            return;
        if (defineShift3Text == null)
            // TODO: Alert the user about the error
            return;
        if (shifts == null)
            // TODO: Alert the user about the error
            return;
        if (defineShiftText.toString().isEmpty()) {
            Snackbar.make(view, "Please Enter Number Of Required Workers!",
                          Snackbar.LENGTH_LONG)
                    .setAction("Ok", null).show();
        }

        if (defineShift3Text.toString().isEmpty()) {
            Snackbar.make(view, "Please Enter The Shift Duration!", Snackbar.LENGTH_LONG)
                    .setAction("Ok", null).show();
        }
        //after all the fields are filled:

        if (!defineShift3Text.toString().isEmpty() &&
            !defineShiftText.toString().isEmpty()) {
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
            if (date == null)
                return;
            c.setTime(date);
            int weekNumber            = c.get(Calendar.WEEK_OF_YEAR);
            int dayNumber             = c.get(Calendar.DAY_OF_WEEK);
            int numOfScheduledWorkers = 0;

            int starTestedHour = hour, endTestedHour = hour + duration;
            //check if the shifts overlapping each other
            for (Shift s : shifts) {
                boolean isSameDate = Objects.equals(s.getShiftDate(), pickedDate);
                boolean isHourOverLap =
                        // if shift start hour is inside an existing shifts time range =overlap
                        s.getStartHour() <= starTestedHour && starTestedHour <= s.getStartHour() + s.getDuration()
                        ||
                        s.getStartHour() <= endTestedHour && endTestedHour <= s.getStartHour() + s.getDuration()
                        ||
                        //  if shifts time range is inside the time range of the new shift =overlap
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
            // check if the shift is in the past
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
