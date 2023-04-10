package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.ViewModel.ShiftsViewModel;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.databinding.FragmentScheduleStatusBinding;

import java.util.ArrayList;

public class ScheduleStatusFragment extends Fragment {
    private FragmentScheduleStatusBinding binding;

    ShiftsViewModel shiftViewModel;
    private UserViewModel userViewModel;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final CardView cardView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.line1);
            cardView = (CardView) view.findViewById(R.id.cardView0);

        }

        public TextView getTextView() {
            return textView;
        }
        public CardView getCardView() {
            return cardView;
        }
    }

    ArrayList<Shift> visibleShiftsArrayList = null;

    public ScheduleStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScheduleStatusBinding.inflate(inflater, container, false);

        shiftViewModel = new ViewModelProvider(requireActivity()).get(ShiftsViewModel.class);
        userViewModel  = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        shiftViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                ((shifts, responseError, throwable) -> {
                    Log.d("Testings", "onCreateView: Finish loading");
                })
                              );
        RecyclerView.Adapter<ScheduleStatusFragment.ViewHolder> adapter = new RecyclerView.Adapter<ScheduleStatusFragment.ViewHolder>() {

            @NonNull
            @Override
            public ScheduleStatusFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                        int viewType) {
                View view =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one_line_dis1,
                                parent, false);
                return new ScheduleStatusFragment.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ScheduleStatusFragment.ViewHolder holder, int position) {
                (holder).getTextView().setText("Shift Date: "+ visibleShiftsArrayList.get(position).getShiftDate()
                        + "\nNumber Of Required Workers: " + visibleShiftsArrayList.get(position).getNumOfRequiredWorkers()
                        + "\nNumber Of Scheduled Workers: " + visibleShiftsArrayList.get(position).getNumOfScheduledWorkers());
                if(visibleShiftsArrayList.get(position).getNumOfScheduledWorkers() == visibleShiftsArrayList.get(position).getNumOfRequiredWorkers()){
                    (holder).getCardView().setCardBackgroundColor(Color.parseColor("#00FA9A"));
                }
                else{
                    (holder).getCardView().setCardBackgroundColor(Color.parseColor("#FFB6C1"));
                }
            }

            @Override
            public int getItemCount() {
                return visibleShiftsArrayList != null ? visibleShiftsArrayList.size() : 0 ;
            }
        };

        binding.rvScheduleStatus.setAdapter(adapter);

        binding.btnScheduleStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visibleShiftsArrayList = new ArrayList<>();

                String pickedDate = binding.dpScheduleStatus.getDayOfMonth() + "-" +
                        (binding.dpScheduleStatus.getMonth()+1) +"-"+
                        binding.dpScheduleStatus.getYear();

                for (Shift shift:shiftViewModel.getShiftstate().getValue()) {
                    if(shift.getShiftDate().equals(pickedDate)){
                        visibleShiftsArrayList.add(shift);
                    }
                }

                adapter.notifyDataSetChanged();

            }
        });
        binding.rvScheduleStatus.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
