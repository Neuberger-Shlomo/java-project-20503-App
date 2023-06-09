package com.example.myapplication;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLinerAdapter;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.UserMVC.Model.User;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.ScheduleAPI;
import com.example.myapplication.databinding.FragemntMyShiftsBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MyShiftsFragment extends Fragment {

    OneLinerAdapter<Shift> adapter   = new OneLinerAdapter<>();
    EventDecorator         decorator = new EventDecorator();
    RequestQueue           queue;
    User user = new User();
    private FragemntMyShiftsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        queue   = Volley.newRequestQueue(requireContext());
        binding = FragemntMyShiftsBinding.inflate(inflater, container, false);
        new ViewModelProvider(requireActivity()).get(UserViewModel.class)
                .getUserState().observe(getViewLifecycleOwner(), u -> {
                    user = u;
                    getShiftsRange(user);
                });

        binding.calender.addDecorator(decorator);
        binding.calender.setOnDateChangedListener(this::onDateChange);
        binding.calender.setOnMonthChangedListener((widget, date) -> getShiftsRange(user));


        binding.rvShifts.setAdapter(adapter);
        binding.rvShifts.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.btnNewDate.setOnClickListener(v -> this.enableCalender());

        enableCalender();
        return binding.getRoot();
    }

    private void onDateChange(MaterialCalendarView widget, CalendarDay date, boolean b) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
        calendar.set(Calendar.MONTH, date.getMonth() - 1);
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        getShiftsBy(user, calendar);
    }


    void getShiftsBy(User user, Calendar calendar) {
        binding.progressBar.setVisibility(View.VISIBLE);
        queue.add(ScheduleAPI.getSchedulesByUser(
                user.getId(),
                user.getAuthToken(),
                calendar.get(Calendar.WEEK_OF_YEAR),
                calendar.get(Calendar.DAY_OF_WEEK),
                this::onDailyShiftArrived));
    }

    void getShiftsRange(User user) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, binding.calender.getCurrentDate().getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.YEAR, binding.calender.getCurrentDate().getYear());
        int starWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        int endWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        for (int j = starWeek; j <= endWeek; j++) {
            queue.add(ScheduleAPI.getSchedulesByUser(user.getId(), user.getAuthToken(), j,
                                                     this::onMonthlyShiftArrived));
        }


    }

    private void onDailyShiftArrived(JSONArray jsonArray, Api.ResponseError responseError,
                                     Throwable throwable) {
        if (jsonArray != null) {
            adapter.clearList();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject o = jsonArray.getJSONObject(i);
                    adapter.addEntry(Shift.fromJSON(o.getJSONObject("request").getJSONObject(
                            "shift")));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
            if (adapter.getItemCount() > 0)
                hideCalender();
        }
        binding.progressBar.setVisibility(View.GONE);

    }


    private void onMonthlyShiftArrived(JSONArray jsonArray, Api.ResponseError responseError,
                                       Throwable throwable) {
        binding.progressBar.setVisibility(View.VISIBLE);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject o = jsonArray.getJSONObject(i);
                    Shift s =
                            Shift.fromJSON(o.getJSONObject("request").getJSONObject(
                                    "shift"));
                    decorator.addDay(s.getShiftDate());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
            binding.calender.invalidateDecorators();
        }
        binding.progressBar.setVisibility(View.GONE);
    }


    void enableCalender() {
        hideView(binding.btnNewDate);
        hideView(binding.rvShifts);
        showView(binding.calender);
    }

    void hideCalender() {
        if (adapter.getItems().size() > 0) {
            binding.btnNewDate.setText(String.format("Change Date\n %s",
                                                     adapter.getItems().get(0).getDate()));
        }
        showView(binding.btnNewDate);
        showView(binding.rvShifts);
        hideView(binding.calender);
    }


    private void hideView(View widget) {
        if (widget.getVisibility() == View.VISIBLE) {
            int shortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_longAnimTime);
            widget.animate()
                    .scaleY(0f)
                    .scaleX(0f)
                    .setDuration(shortAnimationDuration).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            widget.setVisibility(View.GONE);
                        }
                    });
        } else if (widget.getVisibility() == View.GONE) {
            int shortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_longAnimTime);
            widget.animate()
                    .scaleY(1f)
                    .scaleX(1f)
                    .setDuration(shortAnimationDuration).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            widget.setVisibility(View.VISIBLE);
                        }
                    });

        }
    }

    private void showView(View widget) {
        if (widget.getVisibility() == View.GONE || widget.getVisibility() == View.INVISIBLE) {
            int shortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_longAnimTime);
            widget.animate()
                    .scaleY(1f)
                    .scaleX(1f)
                    .setDuration(shortAnimationDuration).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            widget.setVisibility(View.VISIBLE);
                        }
                    });

        }
    }


    public static class EventDecorator implements DayViewDecorator {

        ArrayList<String> days;

        public EventDecorator() {
            days = new ArrayList<>();
        }

        @Override
        public boolean shouldDecorate(CalendarDay date) {
            if (days.size() == 0)
                return false;
            return days.contains(date.getDay() +
                                 "-" +
                                 (date.getMonth()) +
                                 "-" +
                                 date.getYear());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(12, Color.RED));
        }

        public ArrayList<String> getDays() {
            return days;
        }

        public void setDays(ArrayList<String> days) {
            this.days = days;
        }

        public boolean addDay(String day) {
            if (!this.days.contains(day)) {
                days.add(day);
                return true;
            }
            return false;
        }

    }

}
