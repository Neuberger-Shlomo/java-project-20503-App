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

/**
 * APP ENTRY POINT
 * first app activity
 * contain:  navigation controller,  userViewModel, menu items.
 * inflates layout, set navigation controller, update menu.
 */


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

    /**
     * activity to start the app,
     * inflate define and initialize components.
     *
     * @param savedInstanceState data from previous activity (before shutdown)
     */
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
        // set visibility of FloatingActionButton to GONE.
        binding.fab.setVisibility(View.GONE);
        // define OnMenuItemClickListener for toolbar.
        binding.toolbar.setOnMenuItemClickListener(this::OnToolVarItemSelected);


    }

    /**
     * handle toolbar item selection (login, logout, manager)
     *
     * @param item selected MenuItem
     * @return boolean
     */
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

    // (override default method)  handle item selection in the options menu.

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * handle -user press up on ActionBar.
     * - updates the menu with the current user state
     * -navigate up to the parent activity
     *
     * @return true if navigation successful
     */
    @Override
    public boolean onSupportNavigateUp() {
        // update menu and get the navigation controller

        updateMenu(binding.toolbar.getMenu(), userViewModel.getUserState().getValue());
        NavController navController = Navigation.findNavController(this,
                                                                   // navigate up and return the
                                                                   // nev result

                                                                   R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
               || super.onSupportNavigateUp();
    }

    /**
     * update the menu with the current user state
     *
     * @param menu ,enu to update
     * @return true if updated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        User user = userViewModel.getUserState().getValue();
        // if user not logged in - dont show menu items
        if (user == null)
            return true;
        return updateMenu(menu, user);
    }

    /**
     * update the menu with the current user state
     *
     * @return true if the menu updated
     */
    public boolean updateMenu() {
        // update the menu if the binding and userViewModel not null

        if (binding == null || userViewModel.getUserState().getValue() == null)
            return false;
        return updateMenu(binding.toolbar.getMenu(), userViewModel.getUserState().getValue());
    }

    /**
     * update the menu with the current user state
     *
     * @param menu menu to update
     * @param user the current user
     * @return true if the menu updated
     */
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
        //  if the user logged in - dont show menu items

        menu.findItem(R.id.login).setVisible(userNotLogged);
        menu.findItem(R.id.logout).setVisible(!userNotLogged);
        menu.findItem(R.id.manager).setVisible(!userNotLogged && user.isAdmin());

        return true;
    }


}