package com.example.myapplication;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.User.Model.UserViewModel;
import com.example.myapplication.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity implements ViewModelStoreOwner {

    private AppBarConfiguration appBarConfiguration;
    private MainActivityBinding binding;
    private RequestQueue restQueue;

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        restQueue = Volley.newRequestQueue(this);

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setVisibility(View.GONE);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void hideAppBar(){
        binding.appBar.setVisibility(View.GONE);}
    public void showAppBar(){
        binding.appBar.setVisibility(View.VISIBLE);
    }

    public RequestQueue getRestQueue() {
        return restQueue;
    }

}