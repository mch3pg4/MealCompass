package com.example.mealcompass.Restaurants;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentRestaurantsBinding;

import java.util.ArrayList;
import java.util.List;


public class RestaurantsFragment extends Fragment {

    private FragmentRestaurantsBinding binding;


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

        RecyclerView restaurantsRecyclerView = view.findViewById(R.id.restaurantsRecyclerView);
        restaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<RestaurantItem> restaurantItems = new ArrayList<>();
        restaurantItems.add(new RestaurantItem(R.drawable.restaurant_img, "The Fat Radish", "17 Orchard St, New York, NY 10002", "American", "4.5", "$$", "Open", false));

        restaurantItems.add(new RestaurantItem(R.drawable.restaurant_img, "The Fat Radish", "17 Orchard St, New York, NY 10002", "American", "4.5", "$$", "Open", false));

        restaurantItems.add(new RestaurantItem(R.drawable.restaurant_img, "The Fat Radish", "17 Orchard St, New York, NY 10002", "American", "4.5", "$$", "Open", false));

        RestaurantsAdapter restaurantsAdapter = new RestaurantsAdapter(restaurantItems);
        restaurantsRecyclerView.setAdapter(restaurantsAdapter);

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



        binding.profileImageButton.setOnClickListener(
                v -> NavHostFragment.findNavController(RestaurantsFragment.this)
                        .navigate(R.id.action_restaurantsFragment_to_profileFragment));
    }
}


