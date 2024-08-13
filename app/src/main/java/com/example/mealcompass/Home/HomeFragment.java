package com.example.mealcompass.Home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentHomeBinding;

import java.util.List;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

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

        List<RecommendHistoryItem> recommendHistoryItems = List.of(
                new RecommendHistoryItem(R.drawable.restaurant_img, "The Fat Radish", "4.5"),
                new RecommendHistoryItem(R.drawable.restaurant_img, "Kopitiam", "4.0")
        );

        List<RecommendItemItem> recommendItemItems = List.of(
                new RecommendItemItem(R.drawable.restaurant_img, "Chicken Burger"),
                new RecommendItemItem(R.drawable.restaurant_img, "Chicken Salad")
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

            // Blur the background
            WindowManager.LayoutParams layoutParams = requireActivity().getWindow().getAttributes();
            layoutParams.dimAmount = 0.8f; // Adjust the dim amount as needed
            requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            requireActivity().getWindow().setAttributes(layoutParams);

            // Show the popup window at the center
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            // Handle rating bar logic
            RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);
            popupView.findViewById(R.id.confirmRatingButton).setOnClickListener(v1 -> {
                float rating = ratingBar.getRating();
                Toast.makeText(getContext(), "You have rated " + rating + "/5.0 stars", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();  // Close the popup
            });

            // Close the popup window on button click
            popupView.findViewById(R.id.closeRateRecommendation).setOnClickListener(v1 -> popupWindow.dismiss());

            // Restore the original dimming effect when the popup is dismissed
            popupWindow.setOnDismissListener(() -> {
                WindowManager.LayoutParams originalParams = requireActivity().getWindow().getAttributes();
                originalParams.dimAmount = 0f; // Reset dim amount to default
                requireActivity().getWindow().setAttributes(originalParams);
            });
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
}