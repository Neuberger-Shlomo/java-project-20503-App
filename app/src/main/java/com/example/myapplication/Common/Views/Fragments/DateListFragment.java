package com.example.myapplication.Common.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentDatePickingBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * handles date selection and data display.
 * @param <Model> = type of data to display
 */
public abstract class DateListFragment<Model extends IModel> extends Fragment implements ViewModelStoreOwner {

    protected FragmentDatePickingBinding binding;
    protected OneLinerAdapter<Model> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // inflate the layout
        binding = FragmentDatePickingBinding.inflate(inflater, container, false);
        // initialize  adapter
        adapter = new OneLinerAdapter<>();
        // set the adapter's listener
        adapter.setBindViewHolderListener(this::onModelBind);
        // set the adapter to the recycler view
        binding.rvDatePicker.setAdapter(adapter);
        // set the recycler view layout manager
        binding.rvDatePicker.setLayoutManager(new LinearLayoutManager(requireContext()));
        // set the date picker listener to the button
        binding.btnDatePicker.setOnClickListener((v) -> {
            String pickedDate = binding.dpDatePicker.getDayOfMonth() + "-" +
                    (binding.dpDatePicker.getMonth() + 1) + "-" +
                    binding.dpDatePicker.getYear();
            this.onPickClicked(v, pickedDate);
        });
        return binding.getRoot();
    }

    // set the search view's on click listener
    public void setOnSearchListener(View.OnClickListener l) {
        if (binding.searchView.getVisibility() != View.VISIBLE)
            binding.searchView.setVisibility(View.VISIBLE);
        binding.searchView.setOnSearchClickListener(l);
    }

    // set the search view's query text listener
    public void setOnQueryListener(SearchView.OnQueryTextListener l) {
        if (binding.searchView.getVisibility() != View.VISIBLE)
            binding.searchView.setVisibility(View.VISIBLE);
        binding.searchView.setOnQueryTextListener(l);
    }

    /**
      handle the pick date action.
     *
     * @param view        View of button clicked
     * @param pickerValue Selected date
     */
    abstract protected void onPickClicked(View view, String pickerValue);

    /**
     * Binds a model object to the ViewHolder.
     *
     * @param model    Model object to bind
     * @param holder   ViewHolder
     * @param position Position in the list
     */
    protected void onModelBind(Model model, OneLineViewHolder<Model> holder, int position) {
        holder.setText(model.toPrettyString());
        holder.setItem(model);
        holder.setOnClickListener(this::onItemClicked);
    }


     // handle item clicked
    abstract protected void onItemClicked(Model model, View view);

    /**
     * Handle data from server.
     * if data valid- add data to the adapter.
     * if error- show error message
     *
     * @param models List of model objects received from the server
     * @param error  error from the server
     * @param t      Exception thrown during the process
     */
    public void onDataArrived(@Nullable ArrayList<Model> models, @Nullable Api.ResponseError error,
                              @Nullable Throwable t) {
        StringBuilder builder = new StringBuilder();
        // if data received
        if (models != null) {
            for (Model shift : models) {
                // add each model to the adapter
                adapter.addEntry(shift, false);
            }
            return;
        }
        else if (error != null) {
            builder.append(error.getMessage());
        }
        else if (t != null) {
            builder.append(t.getMessage());
        }
        else {
            builder.append("Unknown Error");
        }
        // show the error message in Snackbar
        Snackbar.make(requireView(), builder.toString(), BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}

