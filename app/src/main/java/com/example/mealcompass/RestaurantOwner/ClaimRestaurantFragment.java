package com.example.mealcompass.RestaurantOwner;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.Restaurant;
import com.example.mealcompass.Restaurants.RestaurantItem;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentClaimRestaurantBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class ClaimRestaurantFragment extends Fragment {
    private FragmentClaimRestaurantBinding binding;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private UserViewModel userViewModel;
    private RestaurantViewModel restaurantViewModel;
    private String userId;
    private String selectedRestaurantId, selectedRestaurantName;

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
        binding = FragmentClaimRestaurantBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            userId = null;
        }

        RecyclerView claimRestRecyclerView = binding.claimRestRecyclerView;
        claimRestRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        restaurantViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurantList -> {
            if (restaurantList != null && !restaurantList.isEmpty()) {
                // prepare list to display
                List<RestaurantItem> claimRestaurants = new ArrayList<>();
                for (Restaurant restaurant : restaurantList) {
                    claimRestaurants.add(new RestaurantItem(
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
                ClaimRestaurantAdapter claimRestaurantAdapter = new ClaimRestaurantAdapter(claimRestaurants);
                claimRestRecyclerView.setAdapter(claimRestaurantAdapter);

                // show alert dialog to confirm claim restaurant and add to user's restaurant list in firestore
                claimRestaurantAdapter.setOnRestaurantSelectedListener((restaurantId, restaurantName) -> {
                    selectedRestaurantId = restaurantId;
                    selectedRestaurantName = restaurantName;
                });
            } else {
                Toast.makeText(requireContext(), "No restaurants found", Toast.LENGTH_SHORT).show();
            }
        });

        restaurantViewModel.fetchAllRestaurants();
        binding.prevButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_claimRestaurantFragment_to_registerRestaurantFragment));

        binding.nextButton.setOnClickListener(v -> {
            // show alert dialog to confirm claim restaurant and add to user's restaurant list in firestore
            showClaimRestaurantAlertDialog(selectedRestaurantId);
        });

    }

    private void showClaimRestaurantAlertDialog(String restaurantId) {
        // show alert dialog to confirm claim restaurant and add to user's restaurant list in firestore
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                .setTitle("Claim " + selectedRestaurantName)
                .setMessage("Are you sure you want to claim this restaurant?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // add restaurant to user's restaurant list in firestore
                    userRepository.updateOwnerRestaurants(userId, restaurantId);
                    Toast.makeText(requireContext(), "Restaurant claimed", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).navigate(R.id.action_claimRestaurantFragment_to_restaurantOwnerFragment2);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create();
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}