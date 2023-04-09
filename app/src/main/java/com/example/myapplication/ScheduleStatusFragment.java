package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Constrains;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.databinding.FragmentScheduleStatusBinding;
import com.example.myapplication.databinding.FragmentWorkersConstrainsBinding;

import java.util.ArrayList;

public class ScheduleStatusFragment extends Fragment {
    private FragmentScheduleStatusBinding binding;

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
    ArrayList<Shift> shiftsArrayList= new ArrayList<Shift>() {{
        add(new Shift("6-4-2023",4,new ArrayList<Profile>() {{
            add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
            add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567",1));
            add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567",2));
            add(new Profile("tal","tal","tal@gmail.com","050-1234567",3));}}));
        add(new Shift("7-4-2023",6,new ArrayList<Profile>() {{
            add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
            add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567",1));
            add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567",2));
            add(new Profile("tal","tal","tal@gmail.com","050-1234567",3));}}));
        add(new Shift("8-4-2023",4,new ArrayList<Profile>() {{
            add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
            add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567",1));
            add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567",2));
            add(new Profile("tal","tal","tal@gmail.com","050-1234567",3));}}));
        add(new Shift("9-4-2023",4,new ArrayList<Profile>() {{
            add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
            add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567",2));
            add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567",2));
            add(new Profile("tal","tal","tal@gmail.com","050-1234567",3));}}));
    }};

    public ScheduleStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScheduleStatusBinding.inflate(inflater, container, false);
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
                    (holder).getCardView().setBackgroundColor(Color.parseColor("#00FA9A"));
                }
                else{
                    (holder).getCardView().setBackgroundColor(Color.parseColor("#FFB6C1"));
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

                for (Shift shift:shiftsArrayList) {
                    if(shift.getShiftDate().equals(pickedDate)){
                        visibleShiftsArrayList.add(shift);
                        if(shift.getNumOfScheduledWorkers() == shift.getNumOfRequiredWorkers()){
                            binding.tvScheduleStatus.setText("Shift Is Full");
                            binding.tvScheduleStatus.setTextColor(Color.parseColor("#00FA9A"));

                        }
                        else{
                            binding.tvScheduleStatus.setText("Shift Is Not Full!");
                            binding.tvScheduleStatus.setTextColor(Color.parseColor("#FFB6C1"));
                        }
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
