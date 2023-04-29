package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Common.Utils.DateUtils;
import com.example.myapplication.Common.Views.Fragments.DateListFragment;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.Model.Constraints;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.ViewModel.WorkersConstrainsViewModel;

import java.util.Date;

/**
 * the fragment display constraints of workers.
 * - display more details about the constraint when clicked on a constraint
 */
public class WorkersConstrainsFragment extends DateListFragment<Constraints> {
    /**
     * inflate the view  and set the listeners
     *
     * @param inflater
     * @param container          the parent view
     * @param savedInstanceState saved previous state. so we can restore it.
     * @return the view for the fregment
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding.headerDatePicker.setText("Workers Constrains");

        WorkersConstrainsViewModel workersConstrainsViewModel =
                new ViewModelProvider(this).get(WorkersConstrainsViewModel.class);
        UserViewModel userViewModel =
                new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        // if no  user is login return the root view
        if (userViewModel.getUserState().getValue() == null)
            return view;
        // get  constraints data for the current logged in user

        workersConstrainsViewModel.getData(
                userViewModel.getUserState().getValue().getId(),
                userViewModel.getUserState().getValue().getAuthToken(),
                this::onDataArrived);

        return view;
    }

    /**
     * filter shifts via the adapter (to show only shifts for the selected date)
     *
     * @param view        the view clicked
     * @param pickerValue the selected date
     */
    @Override
    protected void onPickClicked(View view, String pickerValue) {
        Date date = DateUtils.toDateRegularFormat(pickerValue);
        adapter.setFilter(date,
                          (item, s) -> DateUtils.toDate(item.getEndDate()).compareTo(s) == -1);
    }

    /**
     * -bind the modael data to view
     * -show the first and last names of workers
     *
     * @param model    the constraint model represent the data.
     * @param holder   OneLineViewHolder that display data.
     * @param position the position of the item in the list.
     */

    @Override
    protected void onModelBind(Constraints model, OneLineViewHolder<Constraints> holder,
                               int position) {
        super.onModelBind(model, holder, position);
        holder.setText("First Name: " + model.getFirstName() +
                       "\nLast Name: " + model.getLastName());
    }


    /**
     * constraint is clicked -> show data about the constraint
     *
     * @param model the constraint model
     * @param view  the view
     */
    @Override
    protected void onItemClicked(Constraints model, View view) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Worker details:")
                .setMessage(
                        "Type: " + model.getConstType() +
                        "\nConstrain: " + model.getData() +
                        "\nis permanent: " + model.isPermanent() +
                        "\nEnd Date: " + model.getEndDate())
                .setPositiveButton("Ok", null)
                .create().show();
    }

}
