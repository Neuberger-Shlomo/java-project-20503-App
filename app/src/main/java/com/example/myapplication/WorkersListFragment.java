package com.example.myapplication;

import android.app.AlertDialog;
import android.graphics.Color;
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
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.ViewModel.WorkersViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentWorkersListBinding;

import java.util.ArrayList;

public class WorkersListFragment extends Fragment implements ViewModelStoreOwner {
    private FragmentWorkersListBinding binding;

    WorkersViewModel workersViewModel;
    private UserViewModel userViewModel;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private Profile profile;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.line1);

        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
            getTextView().setText("First Name:\t\t"+profile.getFirstName()+"\n"+
                    "Last Name:\t\t"+profile.getLastName());
            getTextView().setTextSize(25);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    private ArrayList<Profile> visibleWorkersArrayList = null;

    public WorkersListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentWorkersListBinding.inflate(inflater, container, false);

        workersViewModel = new ViewModelProvider(requireActivity()).get(WorkersViewModel.class);
        userViewModel  = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        workersViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                ((workers, responseError, throwable) -> {
                    Log.d("Testings", "onCreateView: Finish loading");
                })
        );

        visibleWorkersArrayList = new ArrayList<>();
        for(Profile p:workersViewModel.getWorkersState().getValue()){
            visibleWorkersArrayList.add(p);
        }

        RecyclerView.Adapter<WorkersListFragment.ViewHolder> adapter = new RecyclerView.Adapter<WorkersListFragment.ViewHolder>() {

            @NonNull
            @Override
            public WorkersListFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                        int viewType) {
                View view =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one_line_dis1,
                                parent, false);
                return new WorkersListFragment.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull WorkersListFragment.ViewHolder holder, int position) {

                holder.setProfile(visibleWorkersArrayList.get(holder.getAdapterPosition()));
                (holder).getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("More Details:")
                                .setMessage("Phone Number: " + visibleWorkersArrayList.get(holder.getAdapterPosition()).getPhoneNumber()+"\n"+
                                        "E-Mail: " +visibleWorkersArrayList.get(holder.getAdapterPosition()).getEmail())
                                .setPositiveButton("Ok", (dialog, which) -> {})
                                .setOnDismissListener(dialog -> {})
                                .create().show();
                    }
                });

            }

            @Override
            public int getItemCount() {
                return visibleWorkersArrayList != null ? visibleWorkersArrayList.size() : 0 ;
            }
        };

        binding.rvWorkersList.setAdapter(adapter);
        binding.rvWorkersList.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
