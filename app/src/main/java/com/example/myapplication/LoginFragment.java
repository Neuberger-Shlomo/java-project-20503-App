package com.example.myapplication;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Button;

import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.Model.User;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.api.Api;
import com.example.myapplication.api.UserApiCalls;
import com.example.myapplication.databinding.FragmentLoginBinding;
import com.example.myapplication.user.BasicUser;
import com.example.myapplication.user.RoleLevel;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements TextWatcher, Api.PostCall<JSONObject>, Api.PreCall {

    private FragmentLoginBinding binding;
    private Button btn;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        ((MainActivity) getActivity()).hideAppBar();
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.btnLogin.setEnabled(false);
        binding.etPassword.addTextChangedListener(this);
        UserViewModel userViewModel = new ViewModelProvider(requireActivity(),null).
                get(UserViewModel.class);
        userViewModel.getUiState().observe(getViewLifecycleOwner(), new Observer<BasicUser>() {
            @Override
            public void onChanged(@Nullable BasicUser basicUser) {
                if(basicUser == null){

                }else{

                }
            }
        });
        binding.btnLogin.setOnClickListener(view1 -> {
            String
                    username = binding.etUsername.getText().toString(),
                    password = binding.etPassword.getText().toString();
//            BasicUser.getInstance().setUsername(username);
//            BasicUser.getInstance().setPassword(password);
            final LoginFragment fragment = this;
            UserApiCalls.Login(
                    ((MainActivity) Objects.requireNonNull(getActivity())).getRestQueue(),
                   username,password,
                    this,
                    this);
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean noUsername = binding.etUsername.getText().toString().isEmpty();
        boolean noPassword = binding.etPassword.getText().toString().isEmpty();
        binding.btnLogin.setEnabled(
                !(noUsername || noPassword)
        );
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onPreCall() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onException(Throwable error) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        if (error == null) {((MainActivity) Objects.requireNonNull(getActivity())).showAppBar();
            NavHostFragment.findNavController(LoginFragment.this).popBackStack();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Error has occurred")
                    .setMessage(error.getMessage())
                    .setPositiveButton("OK", (d, w) -> {
                    });
            builder.create().show();
        }
    }


    @Override
    public void onPostCall(@Nullable JSONObject o, @Nullable Api.ResponseError error) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        try {


//            assert o != null;
//            BasicUser.getInstance().setAuthToken(o.getString("jwt"));
//            BasicUser.getInstance().setLevel(RoleLevel.values()[o.getInt("role")]);

        } catch (Exception e){
            this.onException(e);
        }

    }

}