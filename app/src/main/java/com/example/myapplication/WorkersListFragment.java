package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Profile;
import com.example.myapplication.databinding.FragmentWorkersListBinding;

import java.util.ArrayList;

public class WorkersListFragment extends Fragment {
    private FragmentWorkersListBinding binding;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.date);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    ArrayList<Profile> profileArrayList= new ArrayList<Profile>() {{
        add(new Profile("tal","tal","tal@gmail.com","050-1234567"));
        add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567"));
        add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567"));
        add(new Profile("tal","tal","tal@gmail.com","050-1234567"));
        add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567"));
        add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567"));
        add(new Profile("tal","tal","tal@gmail.com","050-1234567"));
        add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567"));
        add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567"));
        add(new Profile("tal","tal","tal@gmail.com","050-1234567"));
        add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567"));
        add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567"));
        add(new Profile("tal","tal","tal@gmail.com","050-1234567"));
        add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567"));
        add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567"));
    }};

    public WorkersListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentWorkersListBinding.inflate(inflater, container, false);
        binding.rvWorkersList.setAdapter(new RecyclerView.Adapter<ViewHolder>() {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
                View view =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one_line,
                                parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                (holder).getTextView().setText(profileArrayList.get(position).getFirstName()+"\t\t\t"+
                        profileArrayList.get(position).getLastName()+"\t\t\t"+
                        profileArrayList.get(position).getPhoneNumber());
                holder.getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(requireContext())
                                .setTitle(((TextView) v).getText().toString())
                                .setMessage(profileArrayList.get(position).getEmail())
                                .setPositiveButton("Yay", (dialog, which) -> {

                                }).setNegativeButton("Nay",(d,w)->{})
                                .setOnDismissListener(dialog -> {

                                })
                                .create().show();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return profileArrayList.size();
            }
        });
        binding.rvWorkersList.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
