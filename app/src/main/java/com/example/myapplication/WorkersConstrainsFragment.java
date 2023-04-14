package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Constraints;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.ViewModel.ShiftsViewModel;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.ViewModel.WorkersConstrainsViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentDatePickingBinding;

import java.util.ArrayList;

public class WorkersConstrainsFragment extends Fragment {

    private FragmentDatePickingBinding binding;

    private WorkersConstrainsViewModel workersConstrainsViewModel;
    private UserViewModel userViewModel;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private Constraints constraints;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.line1);

        }

        public Constraints getConstraints() {
            return constraints;
        }

        public void setConstraints(Constraints constraints) {
            this.constraints = constraints;
            getTextView().setText("Full Name: "+ constraints.getFirstName() + "\t\t" +
                    constraints.getLastName()
                    + "\nPhone Number: " + constraints.getPhoneNumber()
                    + "\nConstrain start Date: " + constraints.getStartDate());
        }

        public TextView getTextView() {
            return textView;
        }
    }

        ArrayList<Constraints> visibleConstrainsArrayList = null;

    public WorkersConstrainsFragment() {
        // Required empty public constructor
    }



    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentDatePickingBinding.inflate(inflater, container, false);

        workersConstrainsViewModel = new ViewModelProvider(requireActivity()).get(WorkersConstrainsViewModel.class);
        userViewModel  = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        workersConstrainsViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                ((shifts, responseError, throwable) -> {
                    Log.d("Testings", "onCreateView: Finish loading");
                })
        );
        RecyclerView.Adapter<WorkersConstrainsFragment.ViewHolder> adapter = new RecyclerView.Adapter<WorkersConstrainsFragment.ViewHolder>() {

            @NonNull
            @Override
            public WorkersConstrainsFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                        int viewType) {
                View view =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one_line_dis1,
                                parent, false);
                return new WorkersConstrainsFragment.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull WorkersConstrainsFragment.ViewHolder holder, int position) {
                holder.setConstraints(visibleConstrainsArrayList.get(holder.getAdapterPosition()));

                (holder).getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ConstMessage = "Type: " + visibleConstrainsArrayList.get(holder.getAdapterPosition()).getConstType()+
                                "\nConstrain: " + visibleConstrainsArrayList.get(holder.getAdapterPosition()).getData()+
                                "\nis permanent: " + visibleConstrainsArrayList.get(holder.getAdapterPosition()).isPermanent()+
                                "\nEnd Date: " + visibleConstrainsArrayList.get(holder.getAdapterPosition()).getEndDate();

                        new AlertDialog.Builder(requireContext())
                                .setTitle("Worker details:")
                                .setMessage(ConstMessage)
                                .setPositiveButton("Ok", (dialog, which) -> {})
                                .setOnDismissListener(dialog -> {})
                                .create().show();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return visibleConstrainsArrayList != null ? visibleConstrainsArrayList.size() : 0 ;
            }
        };

        binding.rvDatePicker.setAdapter(adapter);
        binding.headerDatePicker.setText("Workers Constrains");

        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visibleConstrainsArrayList = new ArrayList<>();

                String pickedDate = binding.dpDatePicker.getYear() + "-" +
                        +0+(binding.dpDatePicker.getMonth()+1) +"-"+
                        binding.dpDatePicker.getDayOfMonth();

                for (Constraints constraint:workersConstrainsViewModel.getWorkersConstraintsstate().getValue()) {
                    if(constraint.getStartDate().equals(pickedDate)){
                        visibleConstrainsArrayList.add(constraint);
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
