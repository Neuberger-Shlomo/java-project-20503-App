package com.example.myapplication;
/**
 * This class represents a fragment in the Android application that displays information about a
 * worker.
 * It displays the worker's profile and allows a super admin to promote or demote the worker's
 * role level.
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.RoleLevel;
import com.example.myapplication.UserMVC.Model.User;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.UsersApi;
import com.example.myapplication.databinding.FragmentWorkerViewBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class WorkerFragment extends Fragment implements ViewModelStoreOwner {
    final public static String                    WORKER_ID_KEY = "WORKER_ID_KEY";
    User         user;
    Profile      profile;
    ProfileModel profileModel;
    RequestQueue queue;
    private             FragmentWorkerViewBinding binding;
    private String employeeID;

    /**
     inflate the view  and set the listeners
     * @param inflater
     * @param container             the parent view
     * @param savedInstanceState saved previous state. so we can restore it.
     * @return the view for the fregment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(requireContext());
        Bundle arguments = getArguments();


        profileModel = new ViewModelProvider(this)
                .get(ProfileModel.class);
        profileModel.getProfileState()
                .observe(getViewLifecycleOwner(), p -> {
                    if (user != null) {
                        setProfile(p);
                    }
                });
        //check changes in the user state
        new ViewModelProvider(getActivity()).get(UserViewModel.class)
                .getUserState().observe(getViewLifecycleOwner(), u -> {
                    user = u;
                    if (user != null)
                        queue.add(UsersApi.getUserById(user.getId(), user.getAuthToken(),
                                                       employeeID,
                                                       () -> {
                                                       }, this::onUserArrived));
                });
        //inflate the view
        binding = FragmentWorkerViewBinding.inflate(inflater, container, false);
        if (arguments == null) {
            NavHostFragment.findNavController(WorkerFragment.this).popBackStack();
            return null;
        }
        employeeID = arguments.getString(WORKER_ID_KEY, "");
        //if no id was passed, go back to the previous fragment
        if (employeeID == null) {
            NavHostFragment.findNavController(WorkerFragment.this).popBackStack();
            return null;
        }

        binding.btnSetAdmin.setVisibility(View.GONE);


        return binding.getRoot();
    }

    /**
     * Set the profile for to update or downgrade the worker role level
     *
     * @param p the profile received from the server
     */
    @SuppressLint("SetTextI18n")
    private void setProfile(Profile p) {
        binding.tvId.setText("Profile id: " + p.getId());
        binding.tvRole.setText("Profile role is: " + p.getMaxRole().toString());
        binding.tvProfile.setText(p.toPrettyString());
        profile = p;
        //downgrade worker role level to manager
        if (user.isSuperAdmin())
            if (p.getMaxRole().ordinal() < RoleLevel.MANAGER.ordinal()) {
                binding.btnSetAdmin.setText("Make manager");
                binding.btnSetAdmin.setVisibility(View.VISIBLE);
                binding.btnSetAdmin.setOnClickListener(this::onPromoteClicked);
                //downgrade worker role level to normal
            } else if (p.getMaxRole().ordinal() ==
                       RoleLevel.MANAGER.ordinal() && p.getMaxRole() != RoleLevel.SUPER_ADMIN) {
                binding.btnSetAdmin.setText("Make normal");
                binding.btnSetAdmin.setVisibility(View.VISIBLE);
                binding.btnSetAdmin.setOnClickListener(this::onDemoteClicked);
            } else {
                //hide button
                binding.btnSetAdmin.setVisibility(View.GONE);
            }
    }

    /**
     * Handle Demote button clicked.
     * @param view
     */
    private void onDemoteClicked(View view) {
        queue.add(
                UsersApi.demoteUserByID(
                        user.getId(),
                        user.getAuthToken(),
                        profile.getUsers().get(0).getId(), null,
                        (jsonObject, responseError, throwable) -> {
                            if (jsonObject != null) {
                                queue.add(UsersApi.getUserById(user.getId(), user.getAuthToken(),
                                                               employeeID,
                                                               () -> {
                                                               }, this::onUserArrived));
                            }
                        }));
    }

    /**
     * Handle promote button clicked.
     * @param view
     */
    private void onPromoteClicked(View view) {
        queue.add(
                UsersApi.promoteUserByID(
                        user.getId(),
                        user.getAuthToken(),
                        profile.getUsers().get(0).getId(), null,
                        (jsonObject, responseError, throwable) -> {
                            if (jsonObject != null) {
                                queue.add(UsersApi.getUserById(user.getId(), user.getAuthToken(),
                                                               employeeID,
                                                               () -> {
                                                               }, this::onUserArrived));
                            }
                        }));
    }

    /**
     *handle when the user data received from the server
     * @param jsonObject jsonarray contain the user data.
     * @param responseError if error
     * @param throwable if throwable
     */
    private void onUserArrived(JSONArray jsonObject, Api.ResponseError responseError,
                               Throwable throwable) {

        if (responseError != null || throwable != null) {
            new AlertDialog.
                    Builder(requireContext()).setTitle("Some error occurred")
                    .setPositiveButton("ok",
                                       (dialog, which) ->
                                               NavHostFragment
                                                       .findNavController(WorkerFragment.this)
                                                       .popBackStack())
                    .create().show();

        } else {
            int maxRole = 0;
            for (int i = 0; i < jsonObject.length(); i++) {
                try {
                    JSONObject object  = jsonObject.getJSONObject(i);
                    Profile    profile = Profile.fromJSON(object.getJSONObject("profile"));
                    User       user1   = User.fromJSON(object);
                    profileModel.setProfile(profile);
                    profileModel.addUser(user1);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    /**
     * ViewModel for the Profile.
     */
    public static class ProfileModel extends AndroidViewModel {
        private final MutableLiveData<Profile> profileState =
                new MutableLiveData<>(new Profile());

        public ProfileModel(@NonNull Application application) {
            super(application);
        }

        /**  @return the LiveData object for the Profile */
        public LiveData<Profile> getProfileState() {
            return profileState;
        }

        //add user to the profile
        public void addUser(User u) {
            profileState.getValue().getUsers().add(u);
            profileState.setValue(profileState.getValue());
        }

        //set profile for the view
        public void setProfile(Profile p) {
            if (!Objects.equals(p, profileState.getValue())) {
                profileState.setValue(p);
            }
        }

    }

}
