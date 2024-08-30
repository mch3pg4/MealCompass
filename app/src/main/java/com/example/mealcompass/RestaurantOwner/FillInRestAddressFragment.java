package com.example.mealcompass.RestaurantOwner;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mealcompass.BuildConfig;
import com.example.mealcompass.R;
import com.example.mealcompass.Register.PlacesAutoCompleteAdapter;
import com.example.mealcompass.databinding.FragmentFillInRestAddressBinding;
import com.example.mealcompass.databinding.FragmentFillInRestDetailsBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;


public class FillInRestAddressFragment extends Fragment {
    private FragmentFillInRestAddressBinding binding;

    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;

    private ActivityResultLauncher<Intent> autocompleteResultLauncher;

    private ActivityResultLauncher<String> locationPermissionRequest;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFillInRestAddressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize Places API
        if (!Places.isInitialized()) {
            String apiKey = BuildConfig.MAPS_API_KEY;
            Places.initialize(requireContext(), apiKey);
        }
        PlacesClient placesClient = Places.createClient(requireContext());

        // Request location permission
        locationPermissionRequest = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                            if (location != null) {
                                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Location permission is needed to show your location on the map", Toast.LENGTH_SHORT).show();
                }
            }
        );

        binding.restaurantMapView.onCreate(savedInstanceState);

        // Setup the MapView
        binding.restaurantMapView.getMapAsync(gMap -> {
            googleMap = gMap;

            // Enable zoom controls
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            // Check for location permissions and set up the map with the user's location
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15)); // 15 is a zoom level
                    }
                });
            } else {
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });


        // Setup Previous and Next buttons
        binding.prevButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_fillInRestAddressFragment_to_fillInRestDetailsFragment));
        binding.nextButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_fillInRestAddressFragment_to_addRestImageFragment));
    }

//    // Fetch place details from a place ID and update the map
//    private void getPlaceFromPlaceId(String placeId) {
//        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME);
//
//        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
//
//        PlacesClient placesClient = Places.createClient(requireContext());
//        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
//            Place place = response.getPlace();
//            LatLng latLng = place.getLatLng();
//            if (latLng != null) {
//                googleMap.clear(); // Clear any existing markers
//                googleMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//            }
//        }).addOnFailureListener((exception) -> {
//            if (exception instanceof ApiException) {
//                Toast.makeText(requireContext(), "Place not found: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//

    // overrides for map lifecycle
    @Override
    public void onStart() {
        super.onStart();
        binding.restaurantMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.restaurantMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.restaurantMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.restaurantMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.restaurantMapView.onLowMemory();
    }

}