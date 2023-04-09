package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Model.Shift;
import com.example.myapplication.databinding.FragmentDefineShiftBinding;

import java.util.ArrayList;


public class DefineShiftFragment extends Fragment {

    private FragmentDefineShiftBinding binding;

    ArrayList<Shift> shiftsArrayList= new ArrayList<Shift>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentDefineShiftBinding.inflate(inflater, container, false);

        binding.btnDefineShifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pickedDate = binding.dpDefineShift.getDayOfMonth() + "-" +
                        (binding.dpDefineShift.getMonth()+1) +"-"+
                        binding.dpDefineShift.getYear();

                int numOfWorkers = Integer.parseInt(binding.tfDefineShift.getText().toString());

                shiftsArrayList.add(new Shift(pickedDate,numOfWorkers));

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
