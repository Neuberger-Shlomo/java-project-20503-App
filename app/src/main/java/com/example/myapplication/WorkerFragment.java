package com.example.myapplication;

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
    private             FragmentWorkerViewBinding binding;
    final public static String                    WORKER_ID_KEY = "WORKER_ID_KEY";

    private String employeeID;

    User         user;
    Profile      profile;
    ProfileModel profileModel;
    RequestQueue queue;

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

        new ViewModelProvider(getActivity()).get(UserViewModel.class)
                .getUserState().observe(getViewLifecycleOwner(), u -> {
                    user = u;
                    if (user != null)
                        queue.add(UsersApi.getUserById(user.getId(), user.getAuthToken(),
                                                       employeeID,
                                                       () -> {
                                                       }, this::onUserArrived));
                });

        binding = FragmentWorkerViewBinding.inflate(inflater, container, false);
        if (arguments == null) {
            NavHostFragment.findNavController(WorkerFragment.this).popBackStack();
            return null;
        }
        employeeID = arguments.getString(WORKER_ID_KEY, "");

        if (employeeID == null) {
            NavHostFragment.findNavController(WorkerFragment.this).popBackStack();
            return null;
        }

        binding.btnSetAdmin.setVisibility(View.GONE);


        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void setProfile(Profile p) {
        binding.tvId.setText("Profile id: " + p.getId());
        binding.tvRole.setText("Profile role is: " + p.getMaxRole().toString());
        binding.tvProfile.setText(p.toPrettyString());
        profile = p;
        if (user.isSuperAdmin())
            if (p.getMaxRole().ordinal() < RoleLevel.MANAGER.ordinal()) {
                binding.btnSetAdmin.setText("Make manager");
                binding.btnSetAdmin.setVisibility(View.VISIBLE);
                binding.btnSetAdmin.setOnClickListener(this::onPromoteClicked);
            } else if (p.getMaxRole().ordinal() ==
                       RoleLevel.MANAGER.ordinal() && p.getMaxRole() != RoleLevel.SUPER_ADMIN) {
                binding.btnSetAdmin.setText("Make normal");
                binding.btnSetAdmin.setVisibility(View.VISIBLE);
                binding.btnSetAdmin.setOnClickListener(this::onDemoteClicked);
            } else {
                binding.btnSetAdmin.setVisibility(View.GONE);
            }
    }

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

    public static class ProfileModel extends AndroidViewModel {
        private final MutableLiveData<Profile> profileState =
                new MutableLiveData<>(new Profile());

        public ProfileModel(@NonNull Application application) {
            super(application);
        }

        public LiveData<Profile> getProfileState() {
            return profileState;
        }

        public void addUser(User u) {
            profileState.getValue().getUsers().add(u);
            profileState.setValue(profileState.getValue());
        }

        public void setProfile(Profile p) {
            if (!Objects.equals(p, profileState.getValue())) {
                profileState.setValue(p);
            }
        }

    }

}
