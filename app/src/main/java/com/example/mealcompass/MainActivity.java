package com.example.mealcompass;

import android.os.Bundle;

import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;
    private NavController.OnDestinationChangedListener listener;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserViewModel userViewModel;
    private UserRepository userRepository;


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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // get the current user type to determine which bottom navigation view to show
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            userViewModel.setUserId(userId);

            userRepository.getUserType(userId).addOnSuccessListener(userType -> {
                if (userType != null) {
                    updateBottomNavView(userType);
                }
            });

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
                } else if (id == R.id.helpdeskFragment) {
                    navController.navigate(R.id.helpdeskFragment);
                } else if (id == R.id.menuItemFragment) {
                    navController.navigate(R.id.menuItemFragment);
                } else if (id == R.id.ratingsFragment2) {
                    navController.navigate(R.id.ratingsFragment2);
                } else if (id == R.id.restaurantOwnerFragment) {
                    navController.navigate(R.id.restaurantOwnerFragment);
                } else if (id == R.id.adminFragment) {
                    navController.navigate(R.id.adminFragment);
                } else if (id == R.id.helpdeskAdminFragment) {
                    navController.navigate(R.id.helpdeskAdminFragment);
                }
                return true;
            });

        }

            // do not show title in the top app bar
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

            //hide top app bar when in main fragments and hide top app bar and bottom navbar when in login registration fragments
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.homeFragment || destination.getId() == R.id.restaurantsFragment || destination.getId() == R.id.discoverFragment || destination.getId() == R.id.favoruitesFragment
                        || destination.getId() == R.id.restaurantOwnerFragment || destination.getId() == R.id.adminFragment) {
                    Objects.requireNonNull(getSupportActionBar()).hide();
                    binding.bottomNavigationView.setVisibility(View.VISIBLE);
                } else if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.welcomeFragment || destination.getId() == R.id.registerFragment || destination.getId() == R.id.forgotPasswordFragment || destination.getId() == R.id.addProfilePicFragment
                        || destination.getId() == R.id.selectRoleFragment2 || destination.getId() == R.id.selectCuisineFragment2 || destination.getId() == R.id.selectDietFragment || destination.getId() == R.id.selectAllergyFragment || destination.getId() == R.id.fillInRestAddressFragment
                        || destination.getId() == R.id.onboardingFragment || destination.getId() == R.id.fillInRestDetailsFragment || destination.getId() == R.id.addRestImageFragment || destination.getId() == R.id.addMenuItemsFragment) {
                    Objects.requireNonNull(getSupportActionBar()).hide();
                    binding.bottomNavigationView.setVisibility(View.GONE);
                } else {
                    Objects.requireNonNull(getSupportActionBar()).show();
                    binding.bottomNavigationView.setVisibility(View.VISIBLE);
                }
            });

        }

        public void updateBottomNavView(String userType) {
            switch (userType) {
                case "user":
                    binding.bottomNavigationView.getMenu().clear();
                    binding.bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);
                    break;
                case "owner":
                    binding.bottomNavigationView.getMenu().clear();
                    binding.bottomNavigationView.inflateMenu(R.menu.owner_bottom_nav_menu);
                    break;
                case "admin":
                    binding.bottomNavigationView.getMenu().clear();
                    binding.bottomNavigationView.inflateMenu(R.menu.admin_bottom_nav_menu);
                    break;
            }
        }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);

        if (R.id.logout == item.getItemId()) {
            //show alert dialog to let user confirm log out
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                FirebaseAuth.getInstance().signOut();
                navController.navigate(R.id.welcomeFragment);
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.show();
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