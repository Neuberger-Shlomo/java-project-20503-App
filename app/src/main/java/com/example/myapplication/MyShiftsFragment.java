package com.example.myapplication;
/**
 * this fragment  displays the shifts of a specific user and date
 * so the user see the shifts
 * add functenality-  red dots in calender to mark days where the user has shifts
 */

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

    /**
     inflate the view  and set the listeners
     * @param inflater
     * @param container             the parent view
     * @param savedInstanceState saved previous state. so we can restore it.
     * @return the view for the fregment
     */
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

    /**
     *show the shifts of the user that selected in the calender
     *
     * @param widget    the MaterialCalendarView widget
     * @param date      the selected date
     * @param b         does the date selected
     */
    private void onDateChange(MaterialCalendarView widget, CalendarDay date, boolean b) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
        calendar.set(Calendar.MONTH, date.getMonth() - 1);
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        getShiftsBy(user, calendar);
    }

    /**
     * show the shifts of the user for specific date
     * @param user     the user using the screen to see his shifts
     * @param calendar  the calender to get the week from
     */
    void getShiftsBy(User user, Calendar calendar) {
        binding.progressBar.setVisibility(View.VISIBLE);
        queue.add(ScheduleAPI.getSchedulesByUser(
                user.getId(),
                user.getAuthToken(),
                calendar.get(Calendar.WEEK_OF_YEAR),
                calendar.get(Calendar.DAY_OF_WEEK),
                this::onDailyShiftArrived));
    }

    /**
     * show the shifts of the user
     * @param user the user using the screen to see his shifts
     */
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

    /**
     * when dailey shift recived from the server
     * update the adapter and hide the calender (and show only the list of shifts
     * @param jsonArray        the json array of the shifts
     * @param responseError     error if happend
     *  @param throwable         throwable if happend
     *
     */

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


    /**
     * when monthly shift recived from the server
     * update the adapter and hide the calender
     * @param jsonArray        the json array of the shifts
     * @param responseError     error if happend
     *  @param throwable         throwable if happend
     *
     */
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

    /**
     * hide view
     *
     * @param widget    the view we want to hide
     */
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

    /**
     * show view
     *
     * @param widget    the view we want to show
     */
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

    /**
     * day decorator for the calendar view. it add dot below days that have shifts.
     */
    public static class EventDecorator implements DayViewDecorator {

        ArrayList<String> days;

        public EventDecorator() {
            days = new ArrayList<>();
        }

        //check if the day has shift
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

        //add red dots in calender to the days that has shifts
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

        /**
         * add day with shift to the decorator to mark the day has shift (to add dot to this day
         * in the calender)
         * @param day   the day we want to add
         * @return true if we add day, false if already existsd in the list
         */
        public boolean addDay(String day) {
            if (!this.days.contains(day)) {
                days.add(day);
                return true;
            }
            return false;
        }

    }

}
