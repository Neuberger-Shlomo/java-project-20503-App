package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Model.Shift;
import com.example.myapplication.ViewModel.ShiftsViewModel;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.databinding.FragmentDefineShiftBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;


public class DefineShiftFragment extends Fragment {

    private static final String TAG = "DefineShiftFragment";
    private FragmentDefineShiftBinding binding;

    ArrayList<Shift> shiftsArrayList = new ArrayList<Shift>();

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
                ((shifts, responseError, throwable) -> {
                    Log.d(TAG, "onCreateView: Finish loading");
                })
                    );

        binding.tpDefineShift.setIs24HourView(true);
        binding.dpDefineShift.setMinDate(System.currentTimeMillis());

        binding.btnDefineShifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pickedDate = binding.dpDefineShift.getDayOfMonth() + "-" +
                                    (binding.dpDefineShift.getMonth() + 1) + "-" +
                                    binding.dpDefineShift.getYear();

                if (binding.tfDefineShift.getText().toString().matches("")) {
                    Snackbar.make(view, "Please Enter Number Of Required Workers!",
                                  Snackbar.LENGTH_LONG)
                            .setAction("Ok", null).show();
                }

                if (binding.tfDefineShift3.getText().toString().matches("")) {
                    Snackbar.make(view, "Please Enter The Shift Duration!", Snackbar.LENGTH_LONG)
                            .setAction("Ok", null).show();
                }

                if (!binding.tfDefineShift3.getText().toString().matches("") &&
                    !binding.tfDefineShift.getText().toString().matches("")) {
                    int      numOfWorkers =
                            Integer.parseInt(binding.tfDefineShift.getText().toString());
                    int      duration     =
                            Integer.parseInt(binding.tfDefineShift3.getText().toString());
                    int      hour         = binding.tpDefineShift.getHour();
                    int      id           =
                            (int) Math.random() * (100) + shiftViewModel.getShiftstate().getValue().size();
                    boolean  flag         = false;
                    Calendar c            = Calendar.getInstance();

                    for (Shift s : shiftViewModel.getShiftstate().getValue()) {
                        if (s.getShiftDate() == pickedDate
                            && (s.getStartHour() <= hour + duration || s.getStartHour() >= hour - duration)) {
                            flag = true;
                        }
                    }
                    if (hour <= c.get(Calendar.HOUR_OF_DAY)) {
                        flag = true;
                    }

                    if (flag == false) {
                        shiftViewModel.addEntry(new Shift(pickedDate, numOfWorkers, id, hour,
                                                          duration));
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
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
