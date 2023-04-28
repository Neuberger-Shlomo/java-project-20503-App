package com.example.myapplication.Entry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.databinding.FragmentEntryBinding;

import java.util.ArrayList;
import java.util.Objects;

public class EntryFragment extends Fragment {

    private static final String               TAG = "EntryFragment";
    private              FragmentEntryBinding binding;
    private              UserViewModel        userViewModel;


    ArrayList<NavItem> items   = new ArrayList<NavItem>() {{
        add(new NavItem(
                "Login",
                "Login to your user",
                R.id.Login,
                false, true));
        add(new NavItem(
                "Signup",
                "Not registered come and join",
                R.id.RegisterFragment,
                false, true));
        add(new NavItem(
                "My Shifts",
                "Enter here to register to a shift",
                R.id.myShiftsFragment,
                true, false));
        add(new NavItem(
                "Request Shifts",
                "Enter here to",
                R.id.AvailableShiftsUserRequestsFragment,
                true, false));
        add(new NavItem("Schedule Restrictions",
                        "Here you can add any constraint to shifts",
                        R.id.constraintSubmissionActivity,
                        true, false));
    }};
    RouteAdapter       adapter = new RouteAdapter();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {
        binding       = FragmentEntryBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //        binding.btnMan.setVisibility(View.GONE);
        userViewModel.getUserState().observe(
                getViewLifecycleOwner(),
                (user) -> {
                    ((MainActivity) requireActivity()).updateMenu();
                    filterEntries();
                    if (user.getAuthToken() != null && !user.getAuthToken().isEmpty()) {
                        binding.txFirstName.setText(user.getProfile().getFirstName());
                    }
                });

        binding.rvButtons.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));



        binding.rvButtons.setAdapter(adapter);
        items.forEach(adapter::addEntry);
        adapter.setBindViewHolderListener(this::onNavItemCrated);
        filterEntries();
    }


    public void filterEntries() {
        adapter.setFilter(
                Objects.requireNonNull(userViewModel.getUserState().getValue()).isLoggedIn(),
                (item, loggedIn) -> (!loggedIn && item.isLoggedInOnly()) || (loggedIn && item.isLoggedOutOnly()));
    }


    private void onNavItemCrated(NavItem navItem,
                                 OneLineViewHolder<NavItem> holder,
                                 int i) {
        holder.setItem(navItem);
        if (holder instanceof RouteHolder) {
            ((RouteHolder) holder).setTitle(holder.getItem().name);
            ((RouteHolder) holder).setDescription(holder.getItem().description);
            holder.setOnClickListener(this::onItemClicked);
        }

    }

    private void onItemClicked(NavItem navItem, View view) {
        NavHostFragment.findNavController(EntryFragment.this)
                .navigate(navItem.navTarget);
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}