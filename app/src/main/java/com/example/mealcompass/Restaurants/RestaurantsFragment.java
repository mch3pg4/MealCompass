package com.example.mealcompass.Restaurants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentRestaurantsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class RestaurantsFragment extends Fragment {

    private FragmentRestaurantsBinding binding;

    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private UserViewModel userViewModel;
    private RestaurantViewModel restaurantViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = null;
        if (user != null) {
            userId = user.getUid();
        }

        // load user name and profile image
        // set up profile image
        userRepository.loadUserProfileImage(userId, binding.profileImageButton, requireContext());

        // set up search bar
        binding.restaurantsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // search for restaurants
                if (query == null || query.trim().isEmpty()) {
                    restaurantViewModel.fetchAllRestaurants();
                } else {
                    restaurantViewModel.searchRestaurants(query.trim());
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        // restaurants recycler view
        RecyclerView restaurantsRecyclerView = view.findViewById(R.id.restaurantsRecyclerView);
        restaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialize viewmodel
        restaurantViewModel  = new ViewModelProvider(this).get(RestaurantViewModel.class);

        // observe restaurant items
        restaurantViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurants -> {
            if (restaurants != null && !restaurants.isEmpty()) {
                // populate recyclerview with restaurants
                List<RestaurantItem> restaurantItems = new ArrayList<>();
                for (Restaurant restaurant : restaurants) {
                    restaurantItems.add(new RestaurantItem(
                            restaurant.getRestaurantImageUrl(),
                            restaurant.getRestaurantName(),
                            restaurant.getRestaurantAddress(),
                            restaurant.getRestaurantCuisine(),
                            restaurant.getRestaurantRating(),
                            restaurant.getRestaurantPricing(),
                            restaurant.getRestaurantBusinessHours(),
                            false));
                }
                RestaurantsAdapter restaurantsAdapter = new RestaurantsAdapter(restaurantItems);
                restaurantsRecyclerView.setAdapter(restaurantsAdapter);
            } else {
                // show error message
                Toast.makeText(getContext(), "No restaurants found", Toast.LENGTH_SHORT).show();
            }
        });

        // get all restaurants
        restaurantViewModel.fetchAllRestaurants();

        // set up filter button
        binding.filterButton.setOnClickListener(v -> showFilterRestaurantsAlertDialog());



        // toggle between list and grid view
        binding.restaurantsViewGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.listButton) {
                    restaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else if (checkedId == R.id.gridButton) {
                    restaurantsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                }
            }
        });

        // load profile image
        binding.profileImageButton.setOnClickListener(
                v -> NavHostFragment.findNavController(RestaurantsFragment.this)
                        .navigate(R.id.action_restaurantsFragment_to_profileFragment));

    }
    public void showFilterRestaurantsAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Filter Restaurants");
        String[] items = {"Open now", "Highly rated", "High price", "Low price", "Nearest"};
        boolean[] checkedItems = {false, false, false, false, false};

        builder.setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
            if (which == 0) {
                if (isChecked) {
                    Toast.makeText(getContext(), "Open Now", Toast.LENGTH_SHORT).show();
                }
            } else if (which == 1) {
                if (isChecked) {
                    Toast.makeText(getContext(), "Highly Rated", Toast.LENGTH_SHORT).show();
                }
            } else if (which == 2) {
                if (isChecked) {
                    Toast.makeText(getContext(), "High Price", Toast.LENGTH_SHORT).show();
                }
            } else if (which == 3) {
                if (isChecked) {
                    Toast.makeText(getContext(), "Low Price", Toast.LENGTH_SHORT).show();
                }
            } else if (which == 4) {
                if (isChecked) {
                    Toast.makeText(getContext(), "Nearest", Toast.LENGTH_SHORT).show();
                }
            } else if (which == 5) {
                if (isChecked) {
                    Toast.makeText(getContext(), "Furthest", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setPositiveButton("Apply", (dialog, which) -> Toast.makeText(getContext(), "Filter Applied", Toast.LENGTH_SHORT).show());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


