package com.example.mealcompass;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;
    private NavController.OnDestinationChangedListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeFragment) {
                navController.navigate(R.id.homeFragment);
            } else if (id == R.id.restaurantsFragment) {
                navController.navigate(R.id.restaurantsFragment);
            } else if (id == R.id.discoverFragment) {
                navController.navigate(R.id.discoverFragment);
            } else if (id == R.id.favoruitesFragment) {
                navController.navigate(R.id.favoruitesFragment);
            }
            return true;
        });



        // do not show title in the top app bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //hide top app bar when in main fragments and hide top app bar and bottom navbar when in login registration fragments
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.homeFragment || destination.getId() == R.id.restaurantsFragment || destination.getId() == R.id.discoverFragment || destination.getId() == R.id.favoruitesFragment
            || destination.getId() == R.id.restaurantOwnerFragment || destination.getId() == R.id.adminFragment) {
                Objects.requireNonNull(getSupportActionBar()).hide();
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
            } else if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.welcomeFragment || destination.getId() == R.id.registerFragment || destination.getId() == R.id.forgotPasswordFragment || destination.getId() == R.id.addProfilePicFragment
                    || destination.getId() == R.id.selectRoleFragment2 || destination.getId() == R.id.selectCuisineFragment2 || destination.getId() == R.id.selectDietFragment || destination.getId() == R.id.selectAllergyFragment
                    || destination.getId() == R.id.onboardingFragment || destination.getId() == R.id.fillInRestDetailsFragment || destination.getId() == R.id.addRestImageFragment || destination.getId() == R.id.addMenuItemsFragment) {
                Objects.requireNonNull(getSupportActionBar()).hide();
                binding.bottomNavigationView.setVisibility(View.GONE);
            }
            else {
                Objects.requireNonNull(getSupportActionBar()).show();
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);

        if (R.id.logout == item.getItemId()) {
            FirebaseAuth.getInstance().signOut();
            navController.navigate(R.id.welcomeFragment);
            return true;
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}