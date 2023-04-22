package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.RoleLevel;
import com.example.myapplication.Model.Shift;
import com.example.myapplication.User.Model.BasicUser;
import com.example.myapplication.User.Model.UserViewModel;
import com.example.myapplication.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity implements ViewModelStoreOwner {

    private AppBarConfiguration appBarConfiguration;
    private MainActivityBinding binding;
    private RequestQueue        restQueue;

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    NavController navController;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding       = MainActivityBinding.inflate(getLayoutInflater());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setContentView(binding.getRoot());
        restQueue = Volley.newRequestQueue(this);

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

    public void hideAppBar() {
        binding.appBar.setVisibility(View.GONE);
    }

    public void showAppBar() {
        binding.appBar.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        BasicUser user = userViewModel.getUserState().getValue();
        if (user == null)
            return true;
        return updateMenu(menu, user);
    }


    public boolean updateMenu() {
        if (binding == null || userViewModel.getUserState().getValue() == null)
            return false;
        return updateMenu(binding.toolbar.getMenu(), userViewModel.getUserState().getValue());
    }

    public boolean updateMenu(BasicUser user) {
        return updateMenu(binding.toolbar.getMenu(), user);
    }

    public boolean updateMenu(Menu menu) {
        return this.updateMenu(menu, userViewModel.getUserState().getValue());
    }

    private boolean updateMenu(Menu menu, BasicUser user) {
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
        menu.findItem(R.id.manager).setVisible(RoleLevel.MANAGER.equals(user.getLevel()));

        return true;
    }

    public RequestQueue getRestQueue() {
        return restQueue;
    }

}