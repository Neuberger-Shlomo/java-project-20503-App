package com.example.myapplication;


/**
 * manager fragment = manager screen where manager can choose screen to go to
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.ManagerButton;
import com.example.myapplication.databinding.FragmentManagerMainBinding;

import java.util.ArrayList;

public class ManagerFragment extends Fragment {

    // ArrayList of ManagerButton that represents different manager operations
    ArrayList<ManagerButton> buttonsArrayList = new ArrayList<ManagerButton>() {{
        add(new ManagerButton("Workers List", R.id.WorkersListFragment));
        add(new ManagerButton("Shift Requests", R.id.ShiftsRequestsFragment));
        add(new ManagerButton("Workers Constrains", R.id.WorkersConstrainsFragment));
        add(new ManagerButton("Shift Scheduling Status", R.id.ScheduleStatusFragment));
        add(new ManagerButton("Schedule Worker Into Shift", R.id.ScheduleWorkerIntoShiftsFragment));
        add(new ManagerButton("Define Shifts", R.id.DefineShiftRequirementsFragment));
        add(new ManagerButton("Auto Schedule", R.id.autoScheduleJob));
    }};
    private FragmentManagerMainBinding binding;

    public ManagerFragment() {
        // Required empty public constructor
    }

    /**
     inflate the view  and set the listeners
     * @param inflater
     * @param container             the parent view
     * @param savedInstanceState saved previous state. so we can restore it.
     * @return the view for the fregment
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {

        binding = FragmentManagerMainBinding.inflate(inflater, container, false);

        // setup RecyclerView with adapter
        binding.rvManagerMain.setAdapter(new RecyclerView.Adapter<ViewHolder>() {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
                View view =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_card_view,
                                                                         parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                // set text for textview and listener
                (holder).getTextView().setText(buttonsArrayList.get(holder.getAdapterPosition()).getButtonName());
                holder.getTextView().setOnClickListener(v -> NavHostFragment.findNavController(ManagerFragment.this)
                        .navigate(buttonsArrayList.get(holder.getAdapterPosition()).getFragment()));
            }

            // get the number of items in the RecyclerView
            @Override
            public int getItemCount() {
                return buttonsArrayList.size();
            }
        });

        // set layout manager for RecyclerView
        binding.rvManagerMain.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvManagerMain.smoothScrollToPosition(buttonsArrayList.size() - 1);
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // viewHolder for the recyclerView that represents the manager options
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final View     cardView;

        public ViewHolder(View view) {
            super(view);
            // Identify view components
            textView = (TextView) view.findViewById(R.id.date);
            cardView = (View) view.findViewById(R.id.card);

        }

        public void setOnClickListener(View.OnClickListener listener) {
            cardView.setOnClickListener(listener);
        }

        public TextView getTextView() {
            return textView;
        }
    }


}
