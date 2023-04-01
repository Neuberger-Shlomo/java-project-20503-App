package com.example.myapplication;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.ViewModel.UserViewModel;
import com.example.myapplication.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity implements ViewModelStoreOwner {

    private AppBarConfiguration appBarConfiguration;
    private MainActivityBinding binding;
    private RequestQueue restQueue;

    private UserViewModel userViewModel;

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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        restQueue = Volley.newRequestQueue(this);

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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