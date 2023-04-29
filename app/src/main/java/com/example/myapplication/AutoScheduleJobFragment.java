package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.Model.ScheduleJob;
import com.example.myapplication.UserMVC.Model.User;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.api.JobApi;
import com.example.myapplication.databinding.FragmentAutoScheduleBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.sql.Date;
import java.util.Calendar;

/**
 * this fragment is for scheduling jobs automatically
 */

public class AutoScheduleJobFragment extends Fragment {

    private static final String TAG = "AutoScheduleJobFragment";
    FragmentAutoScheduleBinding          binding;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker
            .Builder
            .dateRangePicker()
            .build();
    //adapter for the recycler view that show the jobs scheduled
    OneLinerAdapter<ScheduleJob> adapter = new OneLinerAdapter<>();
    String                       howTo   = "This scheduler system will auto organize employees to" +
                                           " shifts\n" +
                                           "\t1. Choose the date range you want to auto " +
                                           "schedule\n" +
                                           "\t2. Click \"Start Job\"\n" +
                                           "\t3. Sit back and relax";
    UserViewModel userViewModel;
    private Date start, end;
    private RequestQueue queue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: view is is being created");
        queue         = Volley.newRequestQueue(requireActivity());
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: view is created");
        binding = FragmentAutoScheduleBinding.inflate(inflater, container, false);
        // Set up click listeners and adapters

        materialDatePicker.addOnPositiveButtonClickListener(this::onDateChose);
        binding.btnSelectDates.setOnClickListener(
                v -> materialDatePicker
                        .show(requireActivity().getSupportFragmentManager(), "null"));
        binding.howTo.setText(howTo);
        binding.btnStartJob.setOnClickListener(this::onStartClicked);
        binding.rvJob.setAdapter(adapter);
        binding.rvJob.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.btnGetJobs.setOnClickListener(this::onGetRequest);
        return binding.getRoot();
    }

    /**
     * callback function for button get jobs
     * ( get all jobs within the selected date range_
     *
     * @param view the view that we bind to button
     */
    private void onGetRequest(View view) {
        Log.d(TAG, "onGetRequest: get all jobs");
        User user = userViewModel.getUserState().getValue();
        // request to get all jobs, handling responses and errors

        queue.add(JobApi.getAllJobs(
                user.getId(),
                user.getAuthToken(), null,
                ((jsonArray, responseError, throwable) -> {
                    if (jsonArray != null) {
                        // if valid response parse the string and add each job to the adapter
                        JsonParser.parseString(jsonArray.toString())
                                .getAsJsonArray()
                                .forEach(e -> adapter.addEntry(new Gson().fromJson(e.toString(),
                                                                                   ScheduleJob.class)));
                    } else if (responseError != null || throwable != null) {
                        Snackbar.make(requireView(), responseError != null ?
                                              responseError.getMessage() :
                                              "Unknown error",
                                      Snackbar.LENGTH_LONG).show();
                        if (throwable != null)
                            Log.e(TAG, "onGetRequest: error when getting jobs", throwable);
                        else {
                            Log.e(TAG, "onGetRequest: Server returned with " + responseError);
                        }
                    } else {
                        // if no response and no error, show a "No information" message
                        Snackbar.make(requireView(), "No information",
                                      Snackbar.LENGTH_LONG).show();
                    }
                })));


    }

    /**
     * callback function for button get jobs
     * ( get all jobs within the selected date range_
     *
     * @param view the view that we bind to button
     */
    private void onStartClicked(View view) {
        Log.d(TAG, "onStartClicked");
        User user = userViewModel.getUserState().getValue();
        // request to start the job
        queue.add(
                JobApi.requestSchedule(
                        user.getId(),
                        user.getAuthToken(),
                        new ScheduleJob() {{
                            setStartDate(start);
                            setEndDate(end);
                            setUserId(Integer.valueOf(user.getId()));
                        }},
                        () -> {
                        },
                        (object, responseError, throwable) -> {
                            try {
                                //if valid response, show the job id
                                new MaterialAlertDialogBuilder(requireActivity())
                                        .setTitle("Job created")
                                        .setMessage("Job id is" + (object != null ?
                                                object.getInt("id") : "Error"));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }));
    }

    /**
     * callback for the date range picker.
     *
     * @param longLongPair the selected date range
     */
    private void onDateChose(Pair<Long, Long> longLongPair) {

        start = new Date(longLongPair.first);
        end   = new Date(longLongPair.second);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(start.getTime()));


        String startDate = String.format("%s/%s/%s",
                                         calendar.get(Calendar.DAY_OF_MONTH),
                                         calendar.get(Calendar.MONTH) + 1,
                                         calendar.get(Calendar.YEAR)
                                        );
        calendar.setTime(new Date(end.getTime()));
        String endDate = String.format("%s/%s/%s",
                                       calendar.get(Calendar.DAY_OF_MONTH),
                                       calendar.get(Calendar.MONTH) + 1,
                                       calendar.get(Calendar.YEAR)
                                      );

        binding.btnStartJob.setEnabled(true);

        binding.tvDates.setText(String.format("%s-%s", startDate, endDate));

    }


}
