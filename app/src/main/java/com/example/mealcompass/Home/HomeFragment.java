package com.example.mealcompass.Home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private UserViewModel userViewModel;


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
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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

        fetchRecommendations();


        // set up welcome text with user's name
        userRepository.getUserName(userId).addOnSuccessListener(name -> {
            if (name != null) {
                binding.welcomeText.setText(String.format("Welcome, %s!", name));
            }
        });

        // set up profile image
        userRepository.loadUserProfileImage(userId, binding.profileImageButton, requireContext());

        RecyclerView recommendRestaurantsRecyclerView = view.findViewById(R.id.homeRecyclerView);
        recommendRestaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView recommendHistoryRecyclerView = view.findViewById(R.id.historyOfRestaurantsRecyclerView);
        recommendHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //horizontal layout
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recommendItemRecyclerView = view.findViewById(R.id.recommendedItemsRecyclerView);
        recommendItemRecyclerView.setLayoutManager(horizontalLayoutManager);


        List<RecommendRestaurantsItem> recommendRestaurantsItems = List.of(
                new RecommendRestaurantsItem(R.drawable.restaurant_img, "The Fat Radish", "4.5")
        );

        List<RecommendItemItem> recommendItemItems = List.of(
                new RecommendItemItem(R.drawable.restaurant_img, "Chicken Burger"),
                new RecommendItemItem(R.drawable.restaurant_img, "Chicken Salad")
        );

        List<RecommendHistoryItem> recommendHistoryItems = List.of(
                new RecommendHistoryItem(R.drawable.restaurant_img, "The Fat Radish", "4.5"),
                new RecommendHistoryItem(R.drawable.restaurant_img, "Kopitiam", "4.0")
        );

        RecommendRestaurantsAdapter recommendRestaurantsAdapter = new RecommendRestaurantsAdapter(recommendRestaurantsItems);
        recommendRestaurantsRecyclerView.setAdapter(recommendRestaurantsAdapter);

        RecommendHistoryAdapter recommendHistoryAdapter = new RecommendHistoryAdapter(recommendHistoryItems);
        recommendHistoryRecyclerView.setAdapter(recommendHistoryAdapter);

        RecommendItemAdapter recommendItemAdapter = new RecommendItemAdapter(recommendItemItems);
        recommendItemRecyclerView.setAdapter(recommendItemAdapter);

//        FrameLayout blurOverlay = view.findViewById(R.id.blurOverlay);

        binding.rateRecommendationButton.setOnClickListener(v -> {
            // Inflate the popup layout
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.rate_recommendation_popup, null);

            // Create the popup window
            final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

            // Set focusable and elevation for the popup window
            popupWindow.setFocusable(true);
            popupWindow.setElevation(150);


            // Show the popup window at the center
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            // Handle rating bar logic
            RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);
            popupView.findViewById(R.id.confirmRatingButton).setOnClickListener(v1 -> {
                float rating = ratingBar.getRating();
                Toast.makeText(getContext(), "You have rated " + rating + "/5.0 stars", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();  // Close the popup
            });

            popupView.findViewById(R.id.cancelRatingButton).setOnClickListener(v1 -> popupWindow.dismiss());

            // Close the popup window on button click
            popupView.findViewById(R.id.closeRateRecommendation).setOnClickListener(v1 -> popupWindow.dismiss());

        });


        binding.refreshResultsButton.setOnClickListener(
                v -> Toast.makeText(getContext(), "Refreshing results pressed", Toast.LENGTH_SHORT).show());

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

            RetrofitClient.getRetrofitClient().getRecommendations(userIdObj).enqueue(new Callback<List<RecommendRestaurantsItem>>() {
                @Override
                public void onResponse(Call<List<RecommendRestaurantsItem>> call, Response<List<RecommendRestaurantsItem>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<RecommendRestaurantsItem> recommendations = response.body();
                        for (RecommendRestaurantsItem recommendation : recommendations) {
                            Log.d("Recommendation", "Restaurant: " + recommendation.getRestaurantName());
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to get recommendations", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<RecommendRestaurantsItem>> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed to connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}