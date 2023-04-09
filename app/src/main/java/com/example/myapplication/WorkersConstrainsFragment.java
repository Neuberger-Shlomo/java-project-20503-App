package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Constrains;
import com.example.myapplication.databinding.FragmentWorkersConstrainsBinding;

import java.util.ArrayList;

public class WorkersConstrainsFragment extends Fragment {

    private FragmentWorkersConstrainsBinding binding;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.line1);

        }

        public TextView getTextView() {
            return textView;
        }
    }

        ArrayList<Constrains> visibleConstrainsArrayList = null;
        ArrayList<Constrains> constrainsArrayList= new ArrayList<Constrains>() {{
            add(new Constrains("tal","tal","050-1234567","Can't Work", "6-4-2023"));
            add(new Constrains("Gal","Gal","050-1234567","Sick", "6-4-2023"));
            add(new Constrains("Yuval","Yuval","050-1234567","Holiday", "8-4-2023"));
            add(new Constrains("tal","tal","050-1234567","Vacation", "8-4-2023"));
            add(new Constrains("tal","tal","050-1234567","Can't Work", "8-4-2023"));
            add(new Constrains("Gal","Gal","050-1234567","Sick", "7-4-2023"));
            add(new Constrains("Yuval","Yuval","050-1234567","Holiday", "7-4-2023"));
            add(new Constrains("tal","tal","050-1234567","Vacation", "9-4-2023"));
        }};

    public WorkersConstrainsFragment() {
        // Required empty public constructor
    }



    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentWorkersConstrainsBinding.inflate(inflater, container, false);
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
                (holder).getTextView().setText("Full Name: "+ visibleConstrainsArrayList.get(position).getFirstName() + "\t\t" +
                        visibleConstrainsArrayList.get(position).getLastName() + "\nPhone Number: " +
                        visibleConstrainsArrayList.get(position).getPhoneNumber() + "\nConstrain Date: " +
                        visibleConstrainsArrayList.get(position).getConstrainDate()+ "\nConstrain: " +
                        visibleConstrainsArrayList.get(position).getConstrain());
            }

            @Override
            public int getItemCount() {
                return visibleConstrainsArrayList != null ? visibleConstrainsArrayList.size() : 0 ;
            }
        };

        binding.rvWorkersConstrains.setAdapter(adapter);

        binding.btnWorkersConstrains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visibleConstrainsArrayList = new ArrayList<>();

                String pickedDate = binding.dpWorkersConstrains.getDayOfMonth() + "-" +
                        (binding.dpWorkersConstrains.getMonth()+1) +"-"+
                        binding.dpWorkersConstrains.getYear();

                for (Constrains constrain:constrainsArrayList) {
                    if(constrain.getConstrainDate().equals(pickedDate)){
                        visibleConstrainsArrayList.add(constrain);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });
        binding.rvWorkersConstrains.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
