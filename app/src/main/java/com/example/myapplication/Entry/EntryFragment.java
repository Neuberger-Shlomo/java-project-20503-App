package com.example.myapplication.Entry;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.Common.Views.Fragments.IModel;
import com.example.myapplication.Common.Views.ViewHolder.OneLiner.OneLineViewHolder;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.User.Model.UserViewModel;
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
                "Shifts",
                "List my shifts",
                R.id.Login,
                true, false));
        add(new NavItem(
                "Signup",
                "Not registered come and join",
                R.id.RegisterFragment,
                false, true));
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
                    if (user.getAuthToken() != null && !user.getAuthToken().isEmpty()) {
                        //                        binding.buttonSecond.setText(R.string.logout);
                        //                        binding.buttonSecond.setOnClickListener
                        //                        (this::onLogout);
                        binding.txFirstName.setText(user.getProfile().getFirstName());
                    } else {
                        //                        binding.buttonSecond.setText(R.string.login);
                        //                        binding.buttonSecond.setOnClickListener
                        //                        (this::onLogin);
                        //                        binding.btnMan.setVisibility(View.GONE);
                    }

                });

        binding.rvButtons.setLayoutManager(new StaggeredGridLayoutManager(2,
                                                                          StaggeredGridLayoutManager.VERTICAL));


        binding.rvButtons.setAdapter(adapter);
        items.forEach(adapter::addEntry);
        adapter.setBindViewHolderListener(this::onNavItemCrated);
    }

    private void onNavItemCrated(NavItem navItem,
                                 OneLineViewHolder<NavItem> holder,
                                 int i) {
        holder.setItem(navItem);
        if (holder instanceof RouteHolder) {
            ((RouteHolder) holder).setTitle(holder.getItem().name);
            ((RouteHolder) holder).setDescription(holder.getItem().description);
            holder.setOnClickListener(this::onItemClicked);
            if (Objects.requireNonNull(userViewModel.getUserState().getValue()).isLoggedIn()) {
                if (navItem.isLoggedInOnly()) {
                    ((RouteHolder) holder).show();
                } else {
                    ((RouteHolder) holder).hide();
                }
            } else {
                if (navItem.isLoggedOutOnly()) {
                    ((RouteHolder) holder).show();
                } else {
                    ((RouteHolder) holder).hide();
                }
            }

        }

    }

    private void onItemClicked(NavItem navItem, View view) {
        NavHostFragment.findNavController(EntryFragment.this)
                .navigate(navItem.navTarget);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    void onLogin(View v) {
        NavHostFragment.findNavController(EntryFragment.this)
                .navigate(R.id.Login);
    }

    void onLogout(View v) {
        try {

            userViewModel.logout(
                    () -> {
                    },
                    (aBoolean, responseError, throwable) ->
                            ((MainActivity) requireActivity())
                                    .updateMenu());
        } catch (Exception e) {
            Log.e(TAG, "onLogout: Error occured", e);
        }
    }


    void onMangerClicked(View v) {
        NavHostFragment.findNavController(EntryFragment.this)
                .navigate(R.id.from_login_to_manger);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}