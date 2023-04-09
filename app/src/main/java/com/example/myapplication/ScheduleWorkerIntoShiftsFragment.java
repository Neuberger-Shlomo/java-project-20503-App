package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.databinding.FragmentScheduleStatusBinding;
import com.example.myapplication.databinding.FragmentScheduleWorkerIntoShiftBinding;

import java.util.ArrayList;


public class ScheduleWorkerIntoShiftsFragment extends Fragment {

    private FragmentScheduleWorkerIntoShiftBinding binding;

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
        ArrayList<Profile> visibleWorkersArrayList = null;
        private Shift currentShift;
    ArrayList<Profile> profileArrayList= new ArrayList<Profile>() {
        {
            add(new Profile("tal", "tal", "tal@gmail.com", "050-1234567",0));
            add(new Profile("Gal", "Gal", "Gal@gmail.com", "050-1234567",1));
            add(new Profile("Yuval", "Yuval", "Yuval@gmail.com", "050-1234567",2));
        }};
        ArrayList<Shift> shiftsArrayList= new ArrayList<Shift>() {{
            add(new Shift("6-4-2023",4,new ArrayList<Profile>() {{
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
                add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567",1));
                //add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567",2));
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",3));}}));
            add(new Shift("6-4-2023",6,new ArrayList<Profile>() {{
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
                add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567",1));
                add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567",2));
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",3));}}));
            add(new Shift("8-4-2023",4,new ArrayList<Profile>() {{
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
                }}));
            add(new Shift("9-4-2023",4,new ArrayList<Profile>() {{
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
                add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567",1));
                add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567",2));
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",3));}}));
        }};

    public ScheduleWorkerIntoShiftsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScheduleWorkerIntoShiftBinding.inflate(inflater, container, false);
        RecyclerView.Adapter<ScheduleWorkerIntoShiftsFragment.ViewHolder> adapter = new RecyclerView.Adapter<ScheduleWorkerIntoShiftsFragment.ViewHolder>() {

            @NonNull
            @Override
            public ScheduleWorkerIntoShiftsFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                        int viewType) {
                View view =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one_line_dis1,
                                parent, false);
                return new ScheduleWorkerIntoShiftsFragment.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ScheduleWorkerIntoShiftsFragment.ViewHolder holder, int position) {
                (holder).getTextView().setText("Shift Date: "+ visibleShiftsArrayList.get(position).getShiftDate()
                        + "\nNumber Of Required Workers: " + visibleShiftsArrayList.get(position).getNumOfRequiredWorkers()
                        + "\nNumber Of Scheduled Workers: " + visibleShiftsArrayList.get(position).getNumOfScheduledWorkers());
                if(visibleShiftsArrayList.get(position).getNumOfScheduledWorkers() == visibleShiftsArrayList.get(position).getNumOfRequiredWorkers()){
                    (holder).getCardView().setVisibility(View.GONE);
                }
                else{
                    (holder).getCardView().setVisibility(View.VISIBLE);
                }

                (holder).getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.rvShiftsToBeScheduled.setVisibility(view.GONE);
                        binding.dpScheduleWorkerIntoShifts.setVisibility(view.GONE);
                        binding.btnScheduleWorkerIntoShifts.setVisibility(view.GONE);
                        binding.cardView7.setVisibility(view.VISIBLE);
                        binding.rvWorkersToSchedule.setVisibility(view.VISIBLE);

                        currentShift = visibleShiftsArrayList.get(position);
                        ArrayList<Profile> workersInShift =new ArrayList<>();
                        workersInShift.addAll(currentShift.getScheduledWorkers());
                        visibleWorkersArrayList = new ArrayList<>();
                        //visibleWorkersArrayList.addAll(profileArrayList);

                        for(Profile profile:profileArrayList){
                            for(Profile p:workersInShift){
                                if(profile.getId() == p.getId()){
                                    continue;
                                }
                                visibleWorkersArrayList.add(profile);
                            }
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return visibleShiftsArrayList != null ? visibleShiftsArrayList.size() : 0 ;
            }
        };

        binding.rvShiftsToBeScheduled.setAdapter(adapter);

        binding.btnScheduleWorkerIntoShifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visibleShiftsArrayList = new ArrayList<>();

                String pickedDate = binding.dpScheduleWorkerIntoShifts.getDayOfMonth() + "-" +
                        (binding.dpScheduleWorkerIntoShifts.getMonth()+1) +"-"+
                        binding.dpScheduleWorkerIntoShifts.getYear();

                for (Shift shift:shiftsArrayList) {
                    if(shift.getShiftDate().equals(pickedDate)){
                        visibleShiftsArrayList.add(shift);
                    }
                }


                adapter.notifyDataSetChanged();

            }
        });

        binding.rvShiftsToBeScheduled.setLayoutManager(new LinearLayoutManager(requireContext()));
        /*-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_*/

        RecyclerView.Adapter<ScheduleWorkerIntoShiftsFragment.ViewHolder> adapter2 = new RecyclerView.Adapter<ScheduleWorkerIntoShiftsFragment.ViewHolder>() {

            @NonNull
            @Override
            public ScheduleWorkerIntoShiftsFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                                  int viewType) {
                View view =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one_line_dis1,
                                parent, false);
                return new ScheduleWorkerIntoShiftsFragment.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ScheduleWorkerIntoShiftsFragment.ViewHolder holder, int position) {
                (holder).getTextView().setText("Full Name: "+ visibleWorkersArrayList.get(position).getFirstName()
                        +"\t\t"+ visibleWorkersArrayList.get(position).getLastName()
                        + "\nPhone Number: " + visibleWorkersArrayList.get(position).getPhoneNumber());

                (holder).getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        visibleShiftsArrayList.get(position).setScheduledWorker(visibleWorkersArrayList.get(position));
                        visibleWorkersArrayList.remove(visibleWorkersArrayList.get(position));
                        shiftsArrayList.get(position);
                        notifyDataSetChanged();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return visibleWorkersArrayList != null ? visibleWorkersArrayList.size() : 0 ;
            }
        };
        binding.rvWorkersToSchedule.setAdapter(adapter2);


        binding.rvWorkersToSchedule.setLayoutManager(new LinearLayoutManager(requireContext()));



        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
