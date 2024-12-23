package com.example.mealcompass.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealcompass.MenuItem.MenuItem;
import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.Restaurant;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private UserViewModel userViewModel;
    private RestaurantViewModel restaurantViewModel;
    private float rating = 0.0f;
    private RecyclerView recommendItemRecyclerView;
    private String restaurantId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = null;
        if (user != null) {
            userId = user.getUid();
        }

        fetchRecommendations();

        // set up welcome text with user's name
        userRepository.getUserName(userId).addOnSuccessListener(name -> {
            if (name != null) {
                binding.welcomeText.setText(String.format("Welcome, %s!", name));
            }
        });

        // set up profile image
        userRepository.loadUserProfileImage(userId, binding.profileImageButton, requireContext());

        RecyclerView recommendHistoryRecyclerView = view.findViewById(R.id.historyOfRestaurantsRecyclerView);
        recommendHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //horizontal layout
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recommendItemRecyclerView = view.findViewById(R.id.recommendedItemsRecyclerView);
        recommendItemRecyclerView.setLayoutManager(horizontalLayoutManager);

        // initialize viewmodel
        userViewModel.getRecommendationHistory();
        userViewModel.getFavouriteRestaurants().observe(getViewLifecycleOwner(), favouriteRestaurants -> {
            if (favouriteRestaurants != null && !favouriteRestaurants.isEmpty()) {
                List<RecommendHistoryItem> recommendHistoryItems = new ArrayList<>();
                for (Restaurant restaurant : favouriteRestaurants) {
                    recommendHistoryItems.add(new RecommendHistoryItem(
                            restaurant.getRestaurantId(),
                            restaurant.getRestaurantImageUrl(),
                            restaurant.getRestaurantName(),
                            restaurant.getRestaurantAddress(),
                            restaurant.getRestaurantCuisine(),
                            restaurant.getRestaurantContact(),
                            restaurant.getRestaurantBusinessHours(),
                            restaurant.getRestaurantPricing(),
                            restaurant.getRestaurantRating()
                    ));
                }
                RecommendHistoryAdapter recommendHistoryAdapter = new RecommendHistoryAdapter(recommendHistoryItems);
                recommendHistoryRecyclerView.setAdapter(recommendHistoryAdapter);
            } else {
                binding.historyEmptyText.setVisibility(View.VISIBLE);
            }
        });

        binding.rateRecommendationButton.setOnClickListener(v -> {
            // Inflate the popup layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View popupView = inflater.inflate(R.layout.rate_recommendation_popup, binding.getRoot(), false);

            // Create the popup window
            final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT, true);

            // Set focusable and elevation for the popup window
            popupWindow.setFocusable(true);
            popupWindow.setElevation(150);

            // Show the popup window at the center
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            // Handle rating bar logic
            RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);
            popupView.findViewById(R.id.confirmRatingButton).setOnClickListener(v1 -> {
                rating = ratingBar.getRating();
                Toast.makeText(getContext(), "You have rated " + rating + "/5.0 stars", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();  // Close the popup
            });
            // Close the popup window on button click
            popupView.findViewById(R.id.cancelRatingButton).setOnClickListener(v1 -> popupWindow.dismiss());
            popupView.findViewById(R.id.closeRateRecommendation).setOnClickListener(v1 -> popupWindow.dismiss());
        });

        binding.getRecommendationsButton.setOnClickListener( v-> {
            // if recommendation is not yet rated, ask user to rate before recommending new restaurant
            if (rating == 0.0f) {
                Toast.makeText(getContext(), "Please rate the current recommendation ", Toast.LENGTH_SHORT).show();
            } else {
                // add recommendation history to database
                userViewModel.addRecommendationHistory(restaurantId, rating);
                // get new recommendations
                fetchRecommendations();
                Toast.makeText(getContext(), "Getting new recommendations", Toast.LENGTH_SHORT).show();
                rating = 0.0f;
            }
        });

        binding.searchButton.setOnClickListener(
                v -> NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_to_restaurantsFragment));

        binding.profileImageButton.setOnClickListener(
                v -> NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_to_profileFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchRecommendations() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            UserId userIdObj = new UserId(userId);

            // Show the progress bar when the request starts
            binding.progressBar.setVisibility(View.VISIBLE);

            RetrofitClient.getRetrofitClient().getRecommendations(userIdObj).enqueue(new Callback<List<RestaurantRecommendationResponse>>() {
                @Override
                public void onResponse(@NonNull Call<List<RestaurantRecommendationResponse>> call, @NonNull Response<List<RestaurantRecommendationResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        binding.progressBar.setVisibility(View.GONE); // Hide progress bar on success

                        // Clear previous recommendations
                        binding.restaurantName.setText("");
                        binding.restaurantCuisine.setText("");
                        Glide.with(requireContext())
                                .load(R.drawable.placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(binding.restaurantImage);

                        List<RestaurantRecommendationResponse> recommendations = response.body();
                        restaurantViewModel.fetchRestaurantByName(recommendations.get(0).getRestaurant());

                        restaurantViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurantList -> {
                            if (restaurantList != null && !restaurantList.isEmpty()) {
                                Restaurant restaurant = restaurantList.get(0);
                                restaurantId = restaurant.getRestaurantId();
                                binding.restaurantName.setText(restaurant.getRestaurantName());
                                binding.restaurantCuisine.setText(String.format("Cuisine: %s", restaurant.getRestaurantCuisine()));
                                Glide.with(requireContext())
                                        .load(restaurant.getRestaurantImageUrl())
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(binding.restaurantImage);

                                binding.recommendRestaurantsCard.setOnClickListener(v -> {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("restaurantId", restaurant.getRestaurantId());
                                    bundle.putString("restaurantName", restaurant.getRestaurantName());
                                    bundle.putString("restaurantImage", restaurant.getRestaurantImageUrl());
                                    bundle.putFloat("restaurantRating", restaurant.getRestaurantRating());
                                    bundle.putString("restaurantCuisine", restaurant.getRestaurantCuisine());
                                    bundle.putString("restaurantAddress", restaurant.getRestaurantAddress());
                                    bundle.putString("restaurantContact", restaurant.getRestaurantContact());
                                    bundle.putString("restaurantOpenOrClose", restaurant.getRestaurantBusinessHours());
                                    NavHostFragment.findNavController(HomeFragment.this)
                                            .navigate(R.id.action_homeFragment_to_restaurantDetailsFragment, bundle);
                                });

                                // Get menu items
                                List<RecommendItemItem> recommendItemItems = new ArrayList<>();
                                List<MenuItemRecommendation> topMenuItems = recommendations.get(0).getTopMenuItems();

                                for (MenuItemRecommendation menuItem : topMenuItems) {
                                    String itemName = menuItem.getItemName();
                                    Log.d("Menu Item", itemName);
                                    restaurantViewModel.fetchMenuItemByName(restaurant.getRestaurantId(), itemName);
                                }

                                restaurantViewModel.getMenuItemsLiveData().observe(getViewLifecycleOwner(), menuItems -> {
                                    if (menuItems != null && !menuItems.isEmpty()) {
                                        for (MenuItem item : menuItems) {
                                            recommendItemItems.add(new RecommendItemItem(
                                                    item.getMenuItemImage(),
                                                    item.getMenuItemName(),
                                                    item.getMenuItemPrice(),
                                                    item.getMenuItemCategory(),
                                                    item.getMenuItemDescription(),
                                                    item.getMenuItemAllergens(),
                                                    item.getMenuItemNutritionalValue()));
                                        }
                                        RecommendItemAdapter recommendItemAdapter = new RecommendItemAdapter(recommendItemItems);
                                        recommendItemRecyclerView.setAdapter(recommendItemAdapter);
                                    } else {
                                        Log.d("Menu Items", "No menu items found or null.");
                                    }
                                });

                            } else {
                                binding.progressBar.setVisibility(View.GONE); // Hide progress bar on failure
                                Toast.makeText(getContext(), "Failed to get recommendations", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        binding.progressBar.setVisibility(View.GONE); // Hide progress bar on failure
                        Toast.makeText(getContext(), "Failed to get recommendations", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<RestaurantRecommendationResponse>> call, @NonNull Throwable t) {
                    binding.progressBar.setVisibility(View.GONE); // Hide progress bar on failure
                    Toast.makeText(getContext(), "Failed to connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}