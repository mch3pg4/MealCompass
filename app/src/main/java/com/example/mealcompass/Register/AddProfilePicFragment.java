package com.example.mealcompass.Register;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentAddProfilePicBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddProfilePicFragment extends Fragment {
    private FragmentAddProfilePicBinding binding;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private UserViewModel userViewModel;
    private Uri profilePicUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Initialize the ActivityResultLauncher
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), result -> {
            if (result != null) {
                // Get the URI of the selected image
                Log.d("AddProfilePicFragment", "Image URI: " + result);

                profilePicUri = result;
                binding.itemImagePreview.setImageURI(profilePicUri);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddProfilePicBinding.inflate(inflater, container, false);
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

        binding.prevButton.setOnClickListener(v -> NavHostFragment.findNavController(AddProfilePicFragment.this)
                .navigate(R.id.action_addProfilePicFragment_to_registerFragment));

        binding.nextButton.setOnClickListener(v -> {
            if (profilePicUri != null) {
                uploadProfilePicture(profilePicUri);
                //grey out the button to prevent multiple clicks
                binding.nextButton.setEnabled(false);
                binding.nextButton.setAlpha(0.5f);
            } else {
                // upload the default profile picture
                uploadProfilePicture(Uri.parse("android.resource://com.example.mealcompass/" + R.drawable.baseline_account_circle_24));
            }
        });

        binding.addFromGalleryButton.setOnClickListener(v -> {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    private void uploadProfilePicture(Uri profilePicUri) {
        String userId = userViewModel.getUserId().getValue();
        if (userId != null && profilePicUri != null) {
            // Reference to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference profilePicRef = storageRef.child("users/" + userId + ".jpg");

            // Upload image to Firebase Storage
            profilePicRef.putFile(profilePicUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // get url of uploaded image
                        profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            // update profile picture url in firestore database
                            userRepository.updateUserImageUrl(userId, downloadUrl)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Profile picture uploaded", Toast.LENGTH_SHORT).show();
                                            // Navigate to the next fragment
                                            NavHostFragment.findNavController(AddProfilePicFragment.this)
                                                    .navigate(R.id.action_addProfilePicFragment_to_selectRoleFragment2);
                                        } else {
                                            Toast.makeText(getContext(), "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        });
                    }).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload image to Firebase Storage", Toast.LENGTH_SHORT).show());
        }
    }
}