package com.example.myapplication;

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
import com.example.myapplication.databinding.FragmentScheduleWorkerIntoShiftBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class ScheduleWorkerIntoShiftsFragment extends Fragment {

    private FragmentScheduleWorkerIntoShiftBinding binding;
    private Shift globalShift = null;
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
        ArrayList<Profile> visibleWorkersArrayList = null;
        private Shift currentShift;
    ArrayList<Profile> profileArrayList= new ArrayList<Profile>() {
        {
            add(new Profile("tal", "tal", "tal@gmail.com", "050-1234567",0));
            add(new Profile("Gal", "Gal", "Gal@gmail.com", "050-1234567",1));
            add(new Profile("Yuval", "Yuval", "Yuval@gmail.com", "050-1234567",2));
            add(new Profile("Moshe", "Moshe", "Moshe@gmail.com", "050-1234567",3));
            add(new Profile("Dani", "Dani", "Dani@gmail.com", "050-1234567",4));
        }};

    public ScheduleWorkerIntoShiftsFragment() {
        // Required empty public constructor
    }


    ShiftsViewModel shiftViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentScheduleWorkerIntoShiftBinding.inflate(inflater, container, false);
        shiftViewModel = new ViewModelProvider(requireActivity()).get(ShiftsViewModel.class);
        userViewModel  = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        shiftViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                ((shifts, responseError, throwable) -> {
                    Log.d("Testings", "onCreateView: Finish loading");
                })
                              );
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
                (holder).getTextView().setText("Shift Date: "+ visibleShiftsArrayList.get(holder.getAdapterPosition()).getShiftDate()
                        + "\nNumber Of Required Workers: " + visibleShiftsArrayList.get(holder.getAdapterPosition()).getNumOfRequiredWorkers()
                        + "\nNumber Of Scheduled Workers: " + visibleShiftsArrayList.get(holder.getAdapterPosition()).getNumOfScheduledWorkers());
                if(visibleShiftsArrayList.get(holder.getAdapterPosition()).getNumOfScheduledWorkers() == visibleShiftsArrayList.get(holder.getAdapterPosition()).getNumOfRequiredWorkers()){
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

                        currentShift = visibleShiftsArrayList.get(holder.getAdapterPosition());
                        ArrayList<Profile> workersInShift =new ArrayList<>();
                        workersInShift.addAll(currentShift.getScheduledWorkers());
                        visibleWorkersArrayList = new ArrayList<>();
                        visibleWorkersArrayList.addAll(profileArrayList);

                        for(Profile profile:profileArrayList){
                            for(Profile p:workersInShift){
                                if(profile.getId() == p.getId()){
                                    visibleWorkersArrayList.remove(profile);
                                }
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

                for (Shift shift:shiftViewModel.getShiftstate().getValue()) {
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
                (holder).getTextView().setText("Full Name: "+ visibleWorkersArrayList.get(holder.getAdapterPosition()).getFirstName()
                        +"\t\t"+ visibleWorkersArrayList.get(holder.getAdapterPosition()).getLastName()
                        + "\nPhone Number: " + visibleWorkersArrayList.get(holder.getAdapterPosition()).getPhoneNumber());

                (holder).getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(globalShift == null) {
                            globalShift = visibleShiftsArrayList.get(holder.getAdapterPosition());
                        }
                        /* add loop that scans the viewModel arraylist and finds the parallel shift*/
                        Profile profile = visibleWorkersArrayList.get(holder.getAdapterPosition());
                        if(globalShift.getNumOfScheduledWorkers() < globalShift.getNumOfRequiredWorkers()) {
                            globalShift.setScheduledWorker(profile);
                            visibleWorkersArrayList.remove(visibleWorkersArrayList.get(holder.getAdapterPosition()));
                            shiftViewModel.getShiftstate().getValue().get(holder.getAdapterPosition());
                            notifyDataSetChanged();
                        }
                        else{
                            Snackbar.make(view, "This shift is full", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", null).show();
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return visibleWorkersArrayList != null ? visibleWorkersArrayList.size() : 0 ;
            }
        };
        binding.rvWorkersToSchedule.setAdapter(adapter2);

        binding.btn2ScheduleWorkerIntoShifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(globalShift != null){
                    for(Shift s:shiftViewModel.getShiftstate().getValue()){
                        if(globalShift.getId() == s.getId() && s.getShiftDate().equals(globalShift.getShiftDate())){
//                            shiftViewModel.addEntry(globalShift);
                            shiftViewModel.updateEntry(shiftViewModel.getShiftstate().getValue().indexOf(s), globalShift);
                        }
                    }
                    globalShift = null;
                }
                binding.rvShiftsToBeScheduled.setVisibility(view.VISIBLE);
                binding.dpScheduleWorkerIntoShifts.setVisibility(view.VISIBLE);
                binding.btnScheduleWorkerIntoShifts.setVisibility(view.VISIBLE);
                binding.cardView7.setVisibility(view.GONE);
                binding.rvWorkersToSchedule.setVisibility(view.GONE);
            }
        });

        binding.rvWorkersToSchedule.setLayoutManager(new LinearLayoutManager(requireContext()));

        shiftViewModel.getShiftstate().observe(getViewLifecycleOwner(),(observer)->adapter.notifyDataSetChanged());


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
