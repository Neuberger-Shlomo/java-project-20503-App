package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Model.ShiftRequest;
import com.example.myapplication.ViewModel.ShiftRequestViewModel;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.ViewModel.WorkersConstrainsViewModel;
import com.example.myapplication.databinding.FragmentDatePickingBinding;
import com.example.myapplication.databinding.FragmentShiftsRequestsBinding;

import java.util.ArrayList;


public class ShiftsRequestsFragment extends Fragment {

    private FragmentDatePickingBinding binding;

    private ShiftRequestViewModel shiftRequestViewModel;
    private UserViewModel userViewModel;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private ShiftRequest shiftRequest;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.line1);

        }

        public ShiftRequest getShiftRequest() {
            return shiftRequest;
        }

        public void setShiftRequest(ShiftRequest shiftRequest) {
            this.shiftRequest = shiftRequest;
            getTextView().setText("Full Name: "+ shiftRequest.getFirstName() + "\t\t" +
                    shiftRequest.getLastName() + "\nPhone Number: " +
                    shiftRequest.getPhoneNumber() + "\nShift Requested Date: " +
                    shiftRequest.getShiftDate());
        }

        public TextView getTextView() {
            return textView;
        }
    }
    ArrayList<ShiftRequest> visibleShiftArrayList = null;

    public ShiftsRequestsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentDatePickingBinding.inflate(inflater, container, false);

        shiftRequestViewModel = new ViewModelProvider(requireActivity()).get(ShiftRequestViewModel.class);
        userViewModel  = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        shiftRequestViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                ((shifts, responseError, throwable) -> {
                    Log.d("Testings", "onCreateView: Finish loading");
                })
        );
        RecyclerView.Adapter<ShiftsRequestsFragment.ViewHolder> adapter = new RecyclerView.Adapter<ShiftsRequestsFragment.ViewHolder>() {

            @NonNull
            @Override
            public ShiftsRequestsFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                           int viewType) {
                View view =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one_line_dis1,
                                parent, false);
                return new ShiftsRequestsFragment.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ShiftsRequestsFragment.ViewHolder holder, int position) {
                holder.setShiftRequest(visibleShiftArrayList.get(holder.getAdapterPosition()));

                (holder).getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String requestMessage = "Request Timestamp: " + visibleShiftArrayList.get(holder.getAdapterPosition()).getTimestamp()+
                                "\nShift Start Hour: " + visibleShiftArrayList.get(holder.getAdapterPosition()).getStartHour()+
                                "\nShift Duration: " + visibleShiftArrayList.get(holder.getAdapterPosition()).getDuration();

                        new AlertDialog.Builder(requireContext())
                                .setTitle("More Info:")
                                .setMessage(requestMessage)
                                .setPositiveButton("Ok", (dialog, which) -> {})
                                .setOnDismissListener(dialog -> {})
                                .create().show();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return visibleShiftArrayList != null ? visibleShiftArrayList.size() : 0 ;
            }
        };

        binding.rvDatePicker.setAdapter(adapter);
        binding.headerDatePicker.setText("Shifts Requests");

        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visibleShiftArrayList = new ArrayList<>();

                String pickedDate = binding.dpDatePicker.getDayOfMonth() + "-" +
                        (binding.dpDatePicker.getMonth()+1) +"-"+
                        binding.dpDatePicker.getYear();

                for (ShiftRequest request:shiftRequestViewModel.getShiftRequestState().getValue()) {
                    if(request.getShiftDate().equals(pickedDate)){
                        visibleShiftArrayList.add(request);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });
        binding.rvDatePicker.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
