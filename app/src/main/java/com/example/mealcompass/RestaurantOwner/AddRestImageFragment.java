package com.example.mealcompass.RestaurantOwner;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantRepository;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentAddRestImageBinding;
import com.google.firebase.auth.FirebaseAuth;


public class AddRestImageFragment extends Fragment {
    private FragmentAddRestImageBinding binding;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private FirebaseAuth mAuth;
    private RestaurantRepository userRepository;
    private RestaurantViewModel restaurantViewModel;
    private Uri restaurantImageUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ActivityResultLauncher
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), result -> {
            if (result != null) {
                // Get the URI of the selected image
                Log.d("AddRestImageFragment", "Image URI: " + result);

                restaurantImageUri = result;
                binding.itemImagePreview.setImageURI(result);
            }
        });
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddRestImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // open gallery
        binding.addRestImageButton.setOnClickListener(v -> {

            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });


        binding.prevButton.setOnClickListener(v -> NavHostFragment.findNavController(AddRestImageFragment.this)
                .navigate(R.id.action_addRestImageFragment_to_fillInRestAddressFragment));

        binding.nextButton.setOnClickListener(v -> {
            // Navigate to the next fragment
            NavHostFragment.findNavController(AddRestImageFragment.this)
                    .navigate(R.id.action_addRestImageFragment_to_addMenuItemsFragment);
        });
    }
}