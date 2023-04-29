package com.example.myapplication.Entry;
/**
 * fragment that provides a list of navigation items to the user
 * (by click on an item the user will move to the desierd screen)
 */

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
    //identify we are in the entry fragment
    private static final String               TAG = "EntryFragment";
    // list of navigation options the user can choose by clicking on them
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
    //  adapter for RecyclerView ( to present the navigation options)
    RouteAdapter       adapter = new RouteAdapter();
    private              FragmentEntryBinding binding;
    private              UserViewModel        userViewModel;

    //create the view hierarchy
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
                            ) {
        binding       = FragmentEntryBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        return binding.getRoot();
    }

    //initilize the view, set event listeners and adapters
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Observe changes in the user login state and updates the UI accordingly
        userViewModel.getUserState().observe(
                getViewLifecycleOwner(),
                (user) -> {
                    ((MainActivity) requireActivity()).updateMenu();
                    filterEntries();
                    // If the user is logged in, set the first name to the user profile name

                    if (user.getAuthToken() != null && !user.getAuthToken().isEmpty()) {
                        binding.txFirstName.setText(user.getProfile().getFirstName());
                    }
                });
        //set the recycler view layout manager
        binding.rvButtons.setLayoutManager(new StaggeredGridLayoutManager(2,
                                                                          StaggeredGridLayoutManager.VERTICAL));


        //set the recycler view adapter
        binding.rvButtons.setAdapter(adapter);
        items.forEach(adapter::addEntry);
        adapter.setBindViewHolderListener(this::onNavItemCrated);
        filterEntries();
    }

    /**
     * update the filter used by the adapter to display navigation items
     * (depend if the user is logged in or not)
     */
    public void filterEntries() {
        adapter.setFilter(
                Objects.requireNonNull(userViewModel.getUserState().getValue()).isLoggedIn(),
                (item, loggedIn) -> (!loggedIn && item.isLoggedInOnly()) || (loggedIn && item.isLoggedOutOnly()));
    }

    /**
     * callback method
     * (we call it when a navigation item is created
     * and want bound him to its ViewHolder)
     *
     * @param navItem the navigation item we want to bind to the ViewHolder
     * @param holder  the ViewHolder
     * @param i       The index of the navigation item (in the adapter)
     */
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

    /**
     * callback method when navigation item is clicked
     *
     * @param navItem  navigation item we clicked on
     * @param view    The view that was clicked
     */
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