package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Model.ShiftRequest;
import com.example.myapplication.databinding.FragmentShiftsRequestsBinding;

import java.util.ArrayList;


public class ShiftsRequestsFragment extends Fragment {

    private FragmentShiftsRequestsBinding binding;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.line1);

        }

        public TextView getTextView() {
            return textView;
        }
    }
    ArrayList<ShiftRequest> visibleShiftArrayList = null;
    ArrayList<ShiftRequest> shiftRequestArrayList= new ArrayList<ShiftRequest>() {{
        add(new ShiftRequest("tal","tal","050-1234567","6-4-2023"));
        add(new ShiftRequest("Gal","Gal","050-1234567","6-4-2023"));
        add(new ShiftRequest("Yuval","Yuval","050-1234567","8-4-2023"));
        add(new ShiftRequest("tal","tal","050-1234567","8-4-2023"));
        add(new ShiftRequest("Gal","Gal","050-1234567","8-4-2023"));
        add(new ShiftRequest("Yuval","Yuval","050-1234567","7-4-2023"));
        add(new ShiftRequest("Yuval","Yuval","050-1234567","7-4-2023"));
    }};

    public ShiftsRequestsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentShiftsRequestsBinding.inflate(inflater, container, false);
        RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
                View view =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one_line_dis1,
                                parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                (holder).getTextView().setText("Full Name: "+ visibleShiftArrayList.get(position).getFirstName() + "\t\t" +
                        visibleShiftArrayList.get(position).getLastName() + "\nPhone Number: " +
                        visibleShiftArrayList.get(position).getPhoneNumber() + "\nShift Requested Date: " +
                        visibleShiftArrayList.get(position).getshiftDate());
            }

            @Override
            public int getItemCount() {
                return visibleShiftArrayList != null ? visibleShiftArrayList.size() : 0 ;
            }
        };

        binding.rvShiftRequests.setAdapter(adapter);

        binding.btnShiftsRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visibleShiftArrayList = new ArrayList<>();

                String pickedDate = binding.dpShiftsRequests.getDayOfMonth() + "-" +
                        (binding.dpShiftsRequests.getMonth()+1) +"-"+
                        binding.dpShiftsRequests.getYear();

                for (ShiftRequest request:shiftRequestArrayList) {
                    if(request.getshiftDate().equals(pickedDate)){
                        visibleShiftArrayList.add(request);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });
        binding.rvShiftRequests.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
