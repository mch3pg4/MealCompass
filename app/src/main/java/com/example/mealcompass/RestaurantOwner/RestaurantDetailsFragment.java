package com.example.mealcompass.RestaurantOwner;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.databinding.FragmentRestaurantDetailsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RestaurantDetailsFragment extends Fragment {
    private FragmentRestaurantDetailsBinding binding;
    private UserRepository userRepository;
    private FirebaseAuth mAuth;
    private GoogleMap googleMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantDetailsBinding.inflate(inflater, container, false);
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

        // retrieve bundle data from previous fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String restaurantId = bundle.getString("restaurantId");
            String restaurantName = bundle.getString("restaurantName");
            String restaurantAddress = bundle.getString("restaurantAddress");
            String restaurantCuisine = bundle.getString("restaurantCuisine");
            float restaurantRating = bundle.getFloat("restaurantRating");
            int restaurantPricing = bundle.getInt("restaurantPricing");
            String restaurantImage = bundle.getString("restaurantImage");
            String restaurantContact = bundle.getString("restaurantContact");
            String restaurantBusinessHours = bundle.getString("restaurantOpenOrClose");

            // set textviews with bundle data
            binding.restaurantNameTitle.setText(restaurantName);
            binding.restaurantAddress.setText(String.format(restaurantAddress));
            binding.restaurantCuisine.setText(String.format("Cuisine: %s", restaurantCuisine));
            binding.restaurantRating.setRating(restaurantRating);
            binding.restaurantContact.setText(String.format(restaurantContact));
            binding.restaurantPricing.setText(String.format("Pricing: %s", restaurantPricingCount(restaurantPricing)));
            // Convert the business hours Map to a formatted string
            try {
                // Replace single quotes with double quotes to become JSON string
                assert restaurantBusinessHours != null;
                String jsonString = restaurantBusinessHours.replace("'", "\"");

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
                binding.restaurantBusinessHours.setText(String.format("Business Hours:\n%s", formattedHours));

            } catch (JSONException e) {
                Log.e("RestaurantDetailsFragment", "onViewCreated: " + e.getMessage());
                binding.restaurantBusinessHours.setText(R.string.business_hours_not_available);
            }

            // load image with glide
            Glide.with(requireContext())
                    .load(restaurantImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.restaurantImage);

            // get restaurant address and show on mapview
            binding.restaurantMapView.getMapAsync(map -> {
                googleMap = map;
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                MapsInitializer.initialize(requireContext());
                addMarker(restaurantAddress);
            });

            binding.restaurantMapView.onCreate(savedInstanceState);

            binding.showRestaurantMenuButton.setOnClickListener(v -> {
                Bundle menuBundle = new Bundle();
                menuBundle.putString("restaurantId", restaurantId);
                menuBundle.putString("restaurantName", restaurantName);
                Navigation.findNavController(v).navigate(R.id.action_restaurantDetailsFragment_to_fullMenuFragment, menuBundle);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void addMarker(String address) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        } catch (IOException e) {
            Log.e("RestaurantDetailsFragment", "addMarker: " + e.getMessage());
        }
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
}