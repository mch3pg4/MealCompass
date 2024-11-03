package com.example.mealcompass.Restaurants;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.widget.SearchView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentRestaurantsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
    private FusedLocationProviderClient fusedLocationClient;


    private int checkedItem = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // get user location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
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
        String userId;
        if (user != null) {
            userId = user.getUid();
        } else {
            userId = null;
        }

        // load user name and profile image
        // set up profile image
        userRepository.loadUserProfileImage(userId, binding.profileImageButton, requireContext());

        // set up search bar
        binding.restaurantsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return false;
            }

            private void performSearch(String query) {
                if (query == null || query.trim().isEmpty()) {
                    restaurantViewModel.fetchAllRestaurants();
                } else {
                    restaurantViewModel.searchRestaurants(query);
                }
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
                    // Prepare the list to display
                    List<RestaurantItem> restaurantItems = new ArrayList<>();

                    // Iterate over each restaurant to check if it is a favourite
                    for (Restaurant restaurant : restaurants) {
                        // Create a RestaurantItem and add it to the list
                        restaurantItems.add(new RestaurantItem(
                                restaurant.getRestaurantId(),
                                restaurant.getRestaurantImageUrl(),
                                restaurant.getRestaurantName(),
                                restaurant.getRestaurantAddress(),
                                restaurant.getRestaurantCuisine(),
                                restaurant.getRestaurantRating(),
                                restaurant.getRestaurantPricing(),
                                restaurant.getRestaurantBusinessHours(),
                                false,
                                restaurant.getRestaurantContact()
                        ));
                    }

                    // Set up the adapter with the complete list of restaurants
                    RestaurantsAdapter restaurantsAdapter = new RestaurantsAdapter(restaurantItems);
                    restaurantsRecyclerView.setAdapter(restaurantsAdapter);
                } else {
                    // Show a message if no restaurants are found
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
        String[] items = {"Open Now", "High Ratings", "Low Ratings","Pricing (High to Low)", "Pricing (Low to High)", "Nearest to Me"};
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> checkedItem = which);
        builder.setPositiveButton("Apply", (dialog, which) -> {
            if (checkedItem == 0) {
                Toast.makeText(getContext(), "Open Now", Toast.LENGTH_SHORT).show();
                restaurantViewModel.sortRestaurantsByOpenNow();
            } else if (checkedItem == 1) {
                restaurantViewModel.sortRestaurantsByRating("desc");
            } else if (checkedItem == 2) {
                restaurantViewModel.sortRestaurantsByRating("asc");
            } else if (checkedItem == 3) {
                restaurantViewModel.sortRestaurantsByPricing("desc");
            } else if (checkedItem == 4) {
                restaurantViewModel.sortRestaurantsByPricing("asc");
            } else if (checkedItem == 5) {
                Toast.makeText(getContext(), "Loading... This might take a while.", Toast.LENGTH_LONG).show();
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                double userLat = location.getLatitude();
                                double userLong = location.getLongitude();
                                restaurantViewModel.sortRestaurantsByDistance(userLat, userLong, requireContext());
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to get location. Make sure location permissions are enabled.", Toast.LENGTH_SHORT).show());
            }
            Toast.makeText(getContext(), "Filter applied", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Reset", (dialog, which) -> {
            dialog.dismiss();
            Toast.makeText(getContext(), "Filter reset", Toast.LENGTH_SHORT).show();
            restaurantViewModel.fetchAllRestaurants();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


