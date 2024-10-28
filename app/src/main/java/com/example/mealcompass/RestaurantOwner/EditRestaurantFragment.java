package com.example.mealcompass.RestaurantOwner;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.Restaurant;
import com.example.mealcompass.Restaurants.RestaurantRepository;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentEditProfileBinding;
import com.example.mealcompass.databinding.FragmentEditRestaurantBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class EditRestaurantFragment extends Fragment {
    private FragmentEditRestaurantBinding binding;
    private RestaurantViewModel restaurantViewModel;
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;
    private UserViewModel userViewModel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Uri restaurantImageUri;
    private String restaurantId, restaurantStatus;
    private final List<FillInBusinessHoursItem> businessHoursItems = new ArrayList<>();



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

        // Initialize the ActivityResultLauncher
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), result -> {
            if (result != null) {
                // Get the URI of the selected image
                Log.d("EditRestaurantFragment", "Image URI: " + result);

                restaurantImageUri = result;
                binding.itemImagePreview.setImageURI(restaurantImageUri);
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditRestaurantBinding.inflate(inflater, container, false);
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

        // Adapter for list of cuisines
        List<String> items = Arrays.asList(getResources().getStringArray(R.array.cuisine_list));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.cuisine_list_item, items);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) binding.cuisineSelectMenu.getEditText();
        if (autoCompleteTextView != null) {
            autoCompleteTextView.setAdapter(adapter);
        }

        // Initialize layout manager for business hours RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.businessHoursRecyclerView.setLayoutManager(layoutManager);

        // Initialize the list of business hours items

        FillInBusinessHoursAdapter adapter1 = new FillInBusinessHoursAdapter(businessHoursItems, getChildFragmentManager());
        binding.businessHoursRecyclerView.setAdapter(adapter1);

        // Set up split hours checkbox listener
        binding.splitHoursCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (int i = 0; i < businessHoursItems.size(); i++) {
                FillInBusinessHoursItem item = businessHoursItems.get(i);
                if (item.isSplitHours() != isChecked) {
                    item.setSplitHours(isChecked);
                    adapter1.notifyItemChanged(i);
                }
            }
        });

        // Fetch restaurant details by owner ID
        restaurantViewModel.fetchRestaurantByOwnerId(userId);
        restaurantViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurant -> {
            if (restaurant != null && !restaurant.isEmpty()) {
                populateRestaurantDetails(restaurant.get(0));
            } else {
                clearRestaurantDetails();
            }
        });

        // Set image picker listener
        binding.updateRestImageButton.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        // Handle "Done" button click
        binding.doneButton.setOnClickListener(v -> {
            // check if all fields are filled
            if (binding.restNameEditText.getText().toString().isEmpty() ||
                    binding.restContactEditText.getText().toString().isEmpty() ||
                    binding.restAddressEditText.getText().toString().isEmpty() ||
                    binding.cuisineAutoCompleteTextView.getText().toString().isEmpty() ||
                    binding.restPricingEditText.getText().toString().isEmpty() ||
                    binding.restRatingEditText.getText().toString().isEmpty() ||
                    binding.itemImagePreview.getDrawable() == null) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if ( !validateBusinessHours(businessHoursItems)) {
                // Validate business hours
                Toast.makeText(getContext(), "Invalid business hours", Toast.LENGTH_SHORT).show();

            } else {
                updateRestaurantDetails();

            }
        });
    }

    // Populate restaurant details
    private void populateRestaurantDetails(Restaurant restaurant) {
        restaurantId = restaurant.getRestaurantId();
        restaurantStatus = restaurant.getRestaurantStatus();
        binding.restNameEditText.setText(restaurant.getRestaurantName());
        binding.restContactEditText.setText(restaurant.getRestaurantContact());
        binding.restAddressEditText.setText(restaurant.getRestaurantAddress());
        binding.cuisineAutoCompleteTextView.setText(restaurant.getRestaurantCuisine());
        binding.restPricingEditText.setText(String.format("%s", restaurant.getRestaurantPricing()));
        binding.restRatingEditText.setText(String.format("%s", restaurant.getRestaurantRating()));
        binding.halalSwitch.setChecked("Yes".equals(restaurant.getIsHalal()));

        Glide.with(requireContext())
                .load(restaurant.getRestaurantImageUrl())
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.itemImagePreview);

        populateBusinessHours(restaurant.getRestaurantBusinessHours());
    }

    // Clear restaurant details
    private void clearRestaurantDetails() {
        binding.restNameEditText.setText("");
        binding.restContactEditText.setText("");
        binding.restAddressEditText.setText("");
        binding.cuisineAutoCompleteTextView.setText("");
        binding.restPricingEditText.setText("");
        binding.restRatingEditText.setText("");
        binding.halalSwitch.setChecked(false);
        binding.itemImagePreview.setImageResource(R.drawable.placeholder);
    }

    private void populateBusinessHours(String businessHoursString) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.businessHoursRecyclerView.setLayoutManager(layoutManager);

        FillInBusinessHoursAdapter adapter1 = new FillInBusinessHoursAdapter(businessHoursItems, getChildFragmentManager());
        binding.businessHoursRecyclerView.setAdapter(adapter1);

        // Parse business hours string and populate the businessHoursItems list
        Map<String, String[]> businessHoursMap = parseBusinessHoursStringWithSplit(businessHoursString);
        businessHoursItems.clear();
        // Track if any day has split hours
        boolean hasSplitHours = false;

        for (Map.Entry<String, String[]> entry : businessHoursMap.entrySet()) {
            String day = entry.getKey();
            String[] hours = entry.getValue();

            String openTime1 = "";
            String closeTime1 = "";
            String openTime2 = "";
            String closeTime2 = "";
            boolean isSplitHours = false;
            boolean isClosed = false;

            if (hours.length == 1 && hours[0].equalsIgnoreCase("Closed")) {
                isClosed = true;
            } else if (hours.length == 2) {
                openTime1 = hours[0].trim();
                closeTime1 = hours[1].trim();
            } else if (hours.length == 4) {
                openTime1 = hours[0].trim();
                closeTime1 = hours[1].trim();
                openTime2 = hours[2].trim();
                closeTime2 = hours[3].trim();
                isSplitHours = true;
                hasSplitHours = true; // Mark that at least one day has split hours
            }

            FillInBusinessHoursItem item = new FillInBusinessHoursItem(day, openTime1, closeTime1, openTime2, closeTime2, isClosed, isSplitHours);
            businessHoursItems.add(item);





        }

        // Set up split hours checkbox listener
        binding.splitHoursCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (int i = 0; i < businessHoursItems.size(); i++) {
                FillInBusinessHoursItem items = businessHoursItems.get(i);
                if (items.isSplitHours() != isChecked) {
                    items.setSplitHours(isChecked);
                    adapter1.notifyItemChanged(i);
                }
            }
        });

        // Automatically set the split hours checkbox if any day has split hours
        binding.splitHoursCheckBox.setChecked(hasSplitHours);

        // Notify the adapter that the data has changed
        adapter1.notifyDataSetChanged();

    }


    private String convertToUpperTimeFormat(String time) {
        // First handle the AM/PM conversion
        time = time.replace(" am", " AM").replace(" pm", " PM");

        // Add ":00" for times without minutes
        String[] parts = time.split(" ");
        if (parts.length == 2 && !parts[0].contains(":")) {
            time = parts[0] + ":00 " + parts[1];
        }

        return time;
    }


    private Map<String, String[]> parseBusinessHoursStringWithSplit(String businessHoursString) {
        // Initialize a map to store the parsed business hours
        Map<String, String[]> businessHoursMap = new HashMap<>();

        // Remove the curly braces and any extra spaces
        businessHoursString = businessHoursString.replaceAll("[{}']", "").trim();

        // Split the string into individual day-hour pairs
        String[] dayHourPairs = businessHoursString.split(", (?=[A-Za-z]+: )");

        // Loop through each pair to split it into day and hours
        for (String dayHour : dayHourPairs) {
            // Split by the colon to separate the day and hours
            String[] parts = dayHour.split(": ", 2);
            if (parts.length == 2) {
                String day = parts[0].trim();
                String hours = parts[1].trim();

                // Check if the restaurant is marked as "Closed"
                if (hours.equalsIgnoreCase("Closed")) {
                    // Mark the day as closed
                    businessHoursMap.put(day, new String[]{"Closed"});
                } else {
                    // Split the hours by comma to account for split hours
                    String[] hourParts = hours.split(", ");
                    if (hourParts.length == 1) {
                        // If there's only one part, it's a single open-close range
                        String[] timeRange = hourParts[0].split(" to ");
                        // Convert am/pm to AM/PM
                        timeRange[0] = convertToUpperTimeFormat(timeRange[0]);
                        timeRange[1] = convertToUpperTimeFormat(timeRange[1]);
                        businessHoursMap.put(day, timeRange);
                    } else if (hourParts.length == 2) {
                        // If there are two parts, there are split hours
                        String[] firstRange = hourParts[0].split(" to ");
                        String[] secondRange = hourParts[1].split(" to ");
                        if (firstRange.length == 2 && secondRange.length == 2) {
                            businessHoursMap.put(day, new String[]{
                                    convertToUpperTimeFormat(firstRange[0].trim()),
                                    convertToUpperTimeFormat(firstRange[1].trim()),
                                    convertToUpperTimeFormat(secondRange[0].trim()),
                                    convertToUpperTimeFormat(secondRange[1].trim())
                            });
                        }
                    }
                }
            }
        }
        return businessHoursMap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // function to update restaurant details
    public boolean validateBusinessHours(List<FillInBusinessHoursItem> items) {
        DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("h:mm a")
                .toFormatter(Locale.US);

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

    private void updateRestaurantDetails() {
        String restaurantName = Objects.requireNonNull(binding.restNameEditText.getText()).toString();
        String restaurantContact = Objects.requireNonNull(binding.restContactEditText.getText()).toString();
        String restaurantAddress = Objects.requireNonNull(binding.restAddressEditText.getText()).toString();
        String restaurantCuisine = Objects.requireNonNull(binding.cuisineAutoCompleteTextView.getText()).toString();
        int restaurantPricing = Integer.parseInt(Objects.requireNonNull(binding.restPricingEditText.getText()).toString());
        float restaurantRating = Float.parseFloat(Objects.requireNonNull(binding.restRatingEditText.getText()).toString());
        boolean isHalal = binding.halalSwitch.isChecked();

        // Get business hours
        String businessHours = formatBusinessHoursOutput(businessHoursItems);


        // if current imageview is not empty, then update image to storage and get url
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("restaurant_images/" + restaurantId + ".jpg");
        if (restaurantImageUri != null) {
            storageReference.putFile(restaurantImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String restaurantImageUrl = uri.toString();
                            restaurantRepository.updateRestaurant(restaurantId, restaurantName, restaurantAddress, restaurantImageUrl, restaurantRating, restaurantPricing, restaurantStatus, businessHours, restaurantCuisine, restaurantContact, isHalal ? "Yes" : "No")
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(requireContext(), "Restaurant updated successfully", Toast.LENGTH_SHORT).show();
                                            NavHostFragment.findNavController(EditRestaurantFragment.this)
                                                    .navigate(R.id.action_editRestaurantFragment_to_restaurantOwnerFragment2);
                                        } else {
                                            Toast.makeText(requireContext(), "Failed to update restaurant", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });

        } else {
            Toast.makeText(getContext(), "Please upload an image for your restaurant", Toast.LENGTH_SHORT).show();
            }
    }



}