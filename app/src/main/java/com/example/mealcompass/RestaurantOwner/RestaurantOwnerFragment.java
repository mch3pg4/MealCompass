package com.example.mealcompass.RestaurantOwner;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentRestaurantOwnerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class RestaurantOwnerFragment extends Fragment {
    private FragmentRestaurantOwnerBinding binding;
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
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantOwnerBinding.inflate(inflater, container, false);
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

        // set up profile image
        userRepository.loadUserProfileImage(userId, binding.profileImageButton, requireContext());

        binding.profileImageButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_restaurantOwnerFragment2_to_profileFragment));

        // set up restaurant details
        restaurantViewModel.fetchRestaurantByOwnerId(userId);

        restaurantViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurantList -> {
            if (restaurantList != null && !restaurantList.isEmpty()) {
                binding.restaurantNameText.setText(restaurantList.get(0).getRestaurantName());
                binding.restaurantAddress.setText(restaurantList.get(0).getRestaurantAddress());
                binding.restaurantCuisine.setText(String.format("Cuisine: %s", restaurantList.get(0).getRestaurantCuisine()));
                binding.restaurantContact.setText(String.format("Contact: %s", restaurantList.get(0).getRestaurantContact()));
                binding.restaurantRating.setRating(restaurantList.get(0).getRestaurantRating());
                binding.restaurantPricing.setText(String.format("Pricing: %s", restaurantPricingCount(restaurantList.get(0).getRestaurantPricing())));
                binding.statusText.setText(String.format("Status: %s", restaurantList.get(0).getRestaurantStatus()));
                Glide.with(requireContext())
                        .load(restaurantList.get(0).getRestaurantImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.restaurantImage);

                // Convert the business hours Map to a formatted string
                try {
                    // Replace single quotes with double quotes to make it a valid JSON string
                    String jsonString = restaurantList.get(0).getRestaurantBusinessHours().replace("'", "\"");

                    // Convert the string to a JSON object
                    JSONObject jsonObject = new JSONObject(jsonString);

                    // Build a formatted string from the JSON object
                    StringBuilder formattedHours = new StringBuilder();
                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String day = keys.next();
                        String hours = jsonObject.getString(day);
                        formattedHours.append(day).append(": ").append(hours).append("\n");
                    }

                    // Set the formatted string to the TextView
                    binding.restaurantBusinessHours.setText(String.format("Business Hours:\n%s", formattedHours.toString()));

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.restaurantBusinessHours.setText("Business Hours: Not Available");
                }
            }
        });



        binding.editButton.setOnClickListener(v-> {
            Toast.makeText(getContext(), "Edit Restaurant Details", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigate(R.id.action_restaurantOwnerFragment2_to_editRestaurantFragment);
        });

        binding.restaurantMapView.setVisibility(View.GONE);



    }

    public String restaurantPricingCount(float price) {
        if (price == 1) {
            return "$";
        } else if (price == 2) {
            return "$$";
        } else if (price == 3) {
            return "$$$";
        } else if (price == 4) {
            return "$$$$";
        } else {
            return "N/A";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}