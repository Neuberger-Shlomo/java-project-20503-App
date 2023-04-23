package com.example.myapplication;

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

import com.example.myapplication.Model.UserButton;
import com.example.myapplication.databinding.FragmentUserMainBinding;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    private FragmentUserMainBinding binding;

    ArrayList<UserButton> buttonsArrayList = new ArrayList<UserButton>() {{
        add(new UserButton("choose shifts",R.id.AvailableShiftsUserRequestsFragment));
    }};


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

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentUserMainBinding.inflate(inflater, container, false);
        binding.rvUserMain.setAdapter(new RecyclerView.Adapter<ViewHolder>() {

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
                (holder).getTextView().setText(buttonsArrayList.get(holder.getAdapterPosition()).getButtonName());
                holder.getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavHostFragment.findNavController(UserFragment.this)
                                .navigate(buttonsArrayList.get(holder.getAdapterPosition()).getFragment());
                    }
                });
            }

            @Override
            public int getItemCount() {
                return buttonsArrayList.size();
            }
        });
        binding.rvUserMain.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
