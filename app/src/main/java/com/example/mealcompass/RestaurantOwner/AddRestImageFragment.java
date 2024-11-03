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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantRepository;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.databinding.FragmentAddRestImageBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AddRestImageFragment extends Fragment {
    private FragmentAddRestImageBinding binding;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private RestaurantViewModel restaurantViewModel;
    private RestaurantRepository restaurantRepository;
    private Uri restaurantImageUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restaurantRepository = new RestaurantRepository();
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        // Initialize the ActivityResultLauncher
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), result -> {
            if (result != null) {
                // Get the URI of the selected image
                Log.d("AddRestImageFragment", "Image URI: " + result);

                restaurantImageUri = result;
                binding.itemImagePreview.setImageURI(restaurantImageUri);
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
            if (restaurantImageUri != null) {
                uploadRestaurantImage(restaurantImageUri);
                //grey out the button to prevent multiple clicks
                binding.nextButton.setEnabled(false);
                binding.nextButton.setAlpha(0.5f);
            } else {
                // owner must upload a restaurant image
                Toast.makeText(getContext(), "Please upload a restaurant image", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void uploadRestaurantImage(Uri restaurantImageUri) {
//        String restaurantId = restaurantViewModel.getRestaurantId().getValue();
        if (getArguments() != null) {
            String restaurantId = getArguments().getString("restaurantId");
            if (restaurantId != null && restaurantImageUri != null) {
                // reference to firebase storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("restaurant_images/" + restaurantId + ".jpg");

                storageReference.putFile(restaurantImageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // get url of uploaded image
                            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String downloadUrl = uri.toString();
                                // update restaurant image url in firestore database
                                restaurantRepository.updateRestaurantImage(restaurantId, downloadUrl)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("restaurantId", restaurantId);
                                                Toast.makeText(getContext(), "Restaurant image uploaded", Toast.LENGTH_SHORT).show();
                                                NavHostFragment.findNavController(this)
                                                        .navigate(R.id.action_addRestImageFragment_to_addMenuItemsFragment, bundle);
                                            } else {
                                                Toast.makeText(getContext(), "Error uploading restaurant image", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            });
                        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload image to Firebase Storage", Toast.LENGTH_SHORT).show());
            }
        }
    }
}