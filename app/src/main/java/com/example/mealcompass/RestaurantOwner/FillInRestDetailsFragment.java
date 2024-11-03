package com.example.mealcompass.RestaurantOwner;


import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantRepository;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.User;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentFillInRestDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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

        // Initialize layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.businessHoursRecyclerView.setLayoutManager(layoutManager);

        // adapter for business hours recycler view
        List<FillInBusinessHoursItem> businessHoursItems = new ArrayList<>();
        businessHoursItems.add(new FillInBusinessHoursItem("Mon", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Tues", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Wed", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Thurs", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Fri", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Sat", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Sun", "9:00 AM", "6:00 PM", "", "", false, false));

        FillInBusinessHoursAdapter adapter1 = new FillInBusinessHoursAdapter(businessHoursItems, getChildFragmentManager());
        binding.businessHoursRecyclerView.setAdapter(adapter1);

        binding.splitHoursCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (int i = 0; i < businessHoursItems.size(); i++) {
                FillInBusinessHoursItem item = businessHoursItems.get(i);
                if (item.isSplitHours() != isChecked) {
                    item.setSplitHours(isChecked);
                    adapter1.notifyItemChanged(i);
                }
            }
        });

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
            if (validateBusinessHours(businessHoursItems)) {
                Toast.makeText(getContext(), "Valid business hours", Toast.LENGTH_SHORT).show();

                //check if all fields are filled
                String name = Objects.requireNonNull(binding.restNameEditText.getText()).toString();
                String cuisine = Objects.requireNonNull(binding.cuisineSelectMenu.getEditText()).getText().toString();
                String businessHours = formatBusinessHoursOutput(businessHoursItems); // format business hours (string output)
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
                    restaurantViewModel.setRestaurantBusinessHours(businessHours);
                    restaurantViewModel.setRestaurantPricing(Integer.parseInt(pricing));
                    restaurantViewModel.setRestaurantRating(Float.parseFloat(rating));
                    addRestaurantDetails();
                }
            }
        });
    }

    public boolean validateBusinessHours(List<FillInBusinessHoursItem> items) {
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a").withLocale(Locale.US);

    for (int i = 0; i < items.size(); i++) {
        FillInBusinessHoursItem currentItem = items.get(i);

        // if closed
        if (currentItem.isClosed()) {
            continue;
        }

        LocalTime openingTime = LocalTime.parse(currentItem.getOpeningHour(), timeFormatter);
        LocalTime closingTime = LocalTime.parse(currentItem.getClosingHour(), timeFormatter);

        if (i < items.size() - 1) {
            if (openingTime.isAfter(closingTime)) {
                // Invalid hours
                Toast.makeText(getContext(), "Invalid hours for " + currentItem.getDay(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (currentItem.isSplitHours()) {
            // check if split times are selected
            if (currentItem.getSplitOpeningHour().isEmpty() || currentItem.getSplitClosingHour().isEmpty()) {
                Toast.makeText(getContext(), "Please select split hours for " + currentItem.getDay(), Toast.LENGTH_SHORT).show();
                return false;
            }
            LocalTime openingSplitTime = LocalTime.parse(currentItem.getSplitOpeningHour(), timeFormatter);
            LocalTime closingSplitTime = LocalTime.parse(currentItem.getSplitClosingHour(), timeFormatter);

            if (closingTime.isAfter(openingSplitTime)) {
                // Overlapping detected
                Toast.makeText(getContext(), "Overlapping times on " + currentItem.getDay() , Toast.LENGTH_SHORT).show();
                return false;
            } else if (closingSplitTime.isBefore(openingSplitTime)) {
                // Invalid split hours
                Toast.makeText(getContext(), "Invalid split hours for " + currentItem.getDay(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
    return true;
}


    // string format for business hours
    private String formatBusinessHoursOutput(List<FillInBusinessHoursItem> items) {
        StringBuilder output = new StringBuilder();
        output.append("{");

        boolean isFirst = true;
        for (FillInBusinessHoursItem item : items) {
            if (!isFirst) {
                output.append(", ");
            }
            isFirst = false;

            // Add day with proper formatting
            output.append("'").append(item.getDay()).append("': '");

            if (item.isClosed()) {
                output.append("Closed");
            } else {
                // Format normal hours
                output.append((item.getOpeningHour()))
                        .append(" to ")
                        .append((item.getClosingHour()));

                // Add split hours if applicable
                if (item.isSplitHours()) {
                    output.append(", ")
                            .append((item.getSplitOpeningHour()))
                            .append(" to ")
                            .append((item.getSplitClosingHour()));
                }
            }
            output.append("'");
        }

        output.append("}");
        return output.toString();
    }


    private void addRestaurantDetails() {
        String name = restaurantViewModel.getRestaurantName().getValue();
        String cuisine = restaurantViewModel.getRestaurantCuisine().getValue();
        String businessHours = restaurantViewModel.getRestaurantBusinessHours().getValue();
        String contact = restaurantViewModel.getRestaurantContact().getValue();
        int pricing = restaurantViewModel.getRestaurantPricing().getValue();
        float rating = restaurantViewModel.getRestaurantRating().getValue();


        restaurantRepository.addRestaurant(name, businessHours, cuisine, contact, pricing, rating, isHalal)
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
                                .navigate(R.id.action_fillInRestDetailsFragment_to_fillInRestAddressFragment, bundle);
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


