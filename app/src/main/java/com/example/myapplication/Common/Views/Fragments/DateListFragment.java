package com.example.myapplication.Common.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.api.Api;
import com.example.myapplication.databinding.FragmentDatePickingBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public abstract class DateListFragment<Model extends IModel> extends Fragment implements ViewModelStoreOwner {

    protected FragmentDatePickingBinding binding;
    protected OneLinerAdapter<Model>     adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDatePickingBinding.inflate(inflater, container, false);
        adapter = new OneLinerAdapter<>();
        adapter.setBindViewHolderListener(this::onModelBind);
        binding.rvDatePicker.setAdapter(adapter);
        binding.rvDatePicker.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.btnDatePicker.setOnClickListener((v) -> {
            String pickedDate = binding.dpDatePicker.getDayOfMonth() + "-" +
                                (binding.dpDatePicker.getMonth() + 1) + "-" +
                                binding.dpDatePicker.getYear();
            this.onPickClicked(v, pickedDate);
        });

        return binding.getRoot();

    }

    /**
     * Handles the pick date action
     *
     * @param view of the clicked the button
     */
    abstract protected void onPickClicked(View view, String pickerValue);

    /**
     * Handles the binding of a shift to the holder
     *
     * @param model    of the current view
     * @param holder   of the current view
     * @param position of the shift in the list
     */
    protected void onModelBind(Model model, OneLineViewHolder<Model> holder, int position) {
        holder.setText(model.toPrettyString());
        holder.setItem(model);
        holder.setOnClickListener(this::onItemClicked);
    }

    /**
     * Handles the holder item clicked
     *
     * @param model of the calling view holder
     * @param view  of the caller
     */
    abstract protected void onItemClicked(Model model, View view);

    /**
     * Handles the getData call default implementation
     *
     * @param models received form the server
     * @param error  error received from the server
     * @param t      thrown by the Volley or parsing
     */
    public void onDataArrived(@Nullable ArrayList<Model> models, @Nullable Api.ResponseError error,
                              @Nullable Throwable t) {
        StringBuilder builder = new StringBuilder();
        if (models != null) {
            for (Model shift : models) {
                adapter.addEntry(shift, false);
            }
            return;
        } else if (error != null) {
            builder.append(error.getMessage());
        } else if (t != null) {
            builder.append(t.getMessage());
        } else {
            builder.append("Unknown Error");
        }
        Snackbar.make(requireView(), builder.toString(), BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}
