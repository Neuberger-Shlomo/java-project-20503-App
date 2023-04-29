package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.UserMVC.Model.User;
import com.example.myapplication.UserMVC.Model.UserViewModel;
import com.example.myapplication.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity implements ViewModelStoreOwner {


    NavController navController;
    UserViewModel userViewModel;
    private AppBarConfiguration appBarConfiguration;
    private MainActivityBinding binding;

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
        binding       = MainActivityBinding.inflate(getLayoutInflater());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        navController       = Navigation.findNavController(this,
                                                           R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        binding.fab.setVisibility(View.GONE);
        binding.toolbar.setOnMenuItemClickListener(this::OnToolVarItemSelected);


    }

    private boolean OnToolVarItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                navController.navigate(R.id.Login);
                break;
            case R.id.logout:
                userViewModel.logout(null,
                                     (aBoolean, responseError, throwable) -> {
                                         navController
                                                 .navigate(R.id.EntryFragment);
                                         updateMenu(null,
                                                    userViewModel.getUserState().getValue());
                                     });
                break;
            case R.id.manager:
                navController.navigate(R.id.managerMainFragment);
                break;
        }
        updateMenu();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        updateMenu(binding.toolbar.getMenu(), userViewModel.getUserState().getValue());
        NavController navController = Navigation.findNavController(this,
                                                                   R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
               || super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        User user = userViewModel.getUserState().getValue();
        if (user == null)
            return true;
        return updateMenu(menu, user);
    }


    public boolean updateMenu() {
        if (binding == null || userViewModel.getUserState().getValue() == null)
            return false;
        return updateMenu(binding.toolbar.getMenu(), userViewModel.getUserState().getValue());
    }

    private boolean updateMenu(Menu menu, User user) {
        if (menu == null)
            return false;
        MenuItem loginItem   = menu.findItem(R.id.login);
        MenuItem logoutItem  = menu.findItem(R.id.logout);
        MenuItem managerItem = menu.findItem(R.id.manager);
        if (loginItem == null || logoutItem == null || managerItem == null)
            return false;
        boolean userNotLogged = "".equals(user.getAuthToken()) ||
                                user.getAuthToken() == null;

        menu.findItem(R.id.login).setVisible(userNotLogged);
        menu.findItem(R.id.logout).setVisible(!userNotLogged);
        menu.findItem(R.id.manager).setVisible(!userNotLogged && user.isAdmin());

        return true;
    }


}