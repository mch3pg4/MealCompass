package com.example.mealcompass.RestaurantOwner;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantRepository;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentFillInRestDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class FillInRestDetailsFragment extends Fragment {
    private FragmentFillInRestDetailsBinding binding;
    private RestaurantViewModel restaurantViewModel;
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;

    private UserViewModel userViewModel;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    String isHalal = "No";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        restaurantRepository = new RestaurantRepository();
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFillInRestDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = null;
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
        }

        userViewModel.setUserId(userId);


        // adapter for list of cuisines
        List<String> items = Arrays.asList(getResources().getStringArray(R.array.cuisine_list));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.cuisine_list_item, items);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) binding.cuisineSelectMenu.getEditText();
        if (autoCompleteTextView != null) {
            autoCompleteTextView.setAdapter(adapter);
        }

        // dont show keyboard when edit text is clicked
        Objects.requireNonNull(binding.cuisineSelectMenu.getEditText()).setFocusable(false);


        binding.halalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isHalal = "Yes";
            } else {
                isHalal = "No";
            }
        });

        binding.prevButton.setOnClickListener(v -> NavHostFragment.findNavController(FillInRestDetailsFragment.this)
                .navigate(R.id.action_fillInRestDetailsFragment_to_selectRoleFragment2));

        binding.nextButton.setOnClickListener(v -> {
            //check if all fields are filled
            String name = Objects.requireNonNull(binding.restNameEditText.getText()).toString();
            String cuisine = Objects.requireNonNull(binding.cuisineSelectMenu.getEditText()).getText().toString();
            String contact = Objects.requireNonNull(binding.restContactEditText.getText()).toString();
            String pricing = Objects.requireNonNull(binding.restPricingEditText.getText()).toString();
            String rating = Objects.requireNonNull(binding.restRatingEditText.getText()).toString();


            if (name.isEmpty()) {
                binding.restNameEditText.setError("Please enter restaurant name");
            } else if (cuisine.isEmpty()) {
                binding.cuisineAutoCompleteTextView.setError("Please select cuisine");
            } else if (contact.isEmpty()) {
                binding.restContactEditText.setError("Please enter contact number");
            } else if (!contact.matches("^01[0-9]{8,9}$")) {
                binding.restContactEditText.setError("Please enter a valid contact number");
            } else if (pricing.isEmpty() || Integer.parseInt(pricing) < 1 || Integer.parseInt(pricing) > 5) {
                binding.restPricingEditText.setError("Please enter a valid pricing range");
            } else if (rating.isEmpty() || Float.parseFloat(rating) < 0 || Float.parseFloat(rating) > 5) {
                binding.restRatingEditText.setError("Please enter a valid rating");
            }
            else {
                restaurantViewModel.setRestaurantName(name);
                restaurantViewModel.setRestaurantCuisine(cuisine);
                restaurantViewModel.setRestaurantContact(contact);
                restaurantViewModel.setRestaurantPricing(Integer.parseInt(pricing));
                restaurantViewModel.setRestaurantRating(Float.parseFloat(rating));
                addRestaurantDetails();
            }

        });
    }

    private void addRestaurantDetails() {
        String name = restaurantViewModel.getRestaurantName().getValue();
        String cuisine = restaurantViewModel.getRestaurantCuisine().getValue();
        String contact = restaurantViewModel.getRestaurantContact().getValue();
        int pricing = restaurantViewModel.getRestaurantPricing().getValue();
        float rating = restaurantViewModel.getRestaurantRating().getValue();


        restaurantRepository.addRestaurant(name, cuisine, contact, pricing, rating, isHalal)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //get restaurant id and store in view model
                    String restaurantId = task.getResult().getId();
                    restaurantViewModel.setRestaurantId(restaurantId);
                    userRepository.updateOwnerRestaurants(userViewModel.getUserId().getValue(), restaurantId);

                    // pass restaurant id to next fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("restaurantId", restaurantId);

                    Toast.makeText(getContext(), "Restaurant added successfully", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(FillInRestDetailsFragment.this)
                            .navigate(R.id.action_fillInRestDetailsFragment_to_fillInBusinessHoursFragment, bundle);
                } else {
                    Toast.makeText(getContext(), "Error adding restaurant", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


