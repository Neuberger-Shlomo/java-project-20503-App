package com.example.myapplication.AutoSchedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.ScheduleJob;
import com.example.myapplication.User.Model.BasicUser;
import com.example.myapplication.User.Model.UserViewModel;
import com.example.myapplication.api.JobApi;
import com.example.myapplication.databinding.FragmentAutoScheduleBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;

import java.sql.Date;
import java.util.Calendar;


public class AutoScheduleJobFragment extends Fragment {
    FragmentAutoScheduleBinding          binding;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker
            .Builder
            .dateRangePicker()
            .build();
    private Date start, end;

    String howTo = "This scheduler system will auto organize employees to shifts\n" +
                   "\t1. Choose the date range you want to auto schedule\n" +
                   "\t2. Click \"Start Job\"\n" +
                   "\t3. Sit back and relax";
    private RequestQueue queue;

    UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue         = Volley.newRequestQueue(requireActivity());
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAutoScheduleBinding.inflate(inflater, container, false);

        materialDatePicker.addOnPositiveButtonClickListener(this::onDateChose);

        binding.btnSelectDates.setOnClickListener(
                v -> materialDatePicker
                        .show(requireActivity().getSupportFragmentManager(), "null"));
        binding.howTo.setText(howTo);
        binding.btnStartJob.setOnClickListener(this::onStartClicked);

        return binding.getRoot();
    }

    private void onStartClicked(View view) {
        BasicUser user = userViewModel.getUserState().getValue();
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
                                new MaterialAlertDialogBuilder(requireActivity())
                                        .setTitle("Job created")
                                        .setMessage("Job id is" + (object != null ?
                                                object.getInt("id") : "Error"));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                                      ));
    }

    private void onDateChose(Pair<Long, Long> longLongPair) {

        start = new Date(longLongPair.first);
        end   = new Date(longLongPair.second);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(start.getTime()));


        String startDate = String.format("%s/%s/%s",
                                         calendar.get(Calendar.DAY_OF_MONTH),
                                         calendar.get(Calendar.MONTH)+1,
                                         calendar.get(Calendar.YEAR)
                                        );
        calendar.setTime(new Date(end.getTime()));
        String endDate = String.format("%s/%s/%s",
                                       calendar.get(Calendar.DAY_OF_MONTH),
                                       calendar.get(Calendar.MONTH),
                                       calendar.get(Calendar.YEAR)
                                      );


        binding.tvDates.setText(String.format("%s-%s", startDate, endDate));

    }


}
