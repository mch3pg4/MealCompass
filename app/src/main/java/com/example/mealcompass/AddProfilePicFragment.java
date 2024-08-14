package com.example.mealcompass;

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

import com.example.mealcompass.databinding.FragmentAddProfilePicBinding;

public class AddProfilePicFragment extends Fragment {
    private FragmentAddProfilePicBinding binding;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ActivityResultLauncher
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), result -> {
            if (result != null) {
                // Get the URI of the selected image
                Log.d("AddProfilePicFragment", "Image URI: " + result);

                binding.itemImagePreview.setImageURI(result);
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

        binding.prevButton.setOnClickListener(v -> NavHostFragment.findNavController(AddProfilePicFragment.this)
                .navigate(R.id.action_addProfilePicFragment_to_registerFragment));

        binding.nextButton.setOnClickListener(v -> {
            // Navigate to the next fragment
            NavHostFragment.findNavController(AddProfilePicFragment.this)
                    .navigate(R.id.action_addProfilePicFragment_to_selectRoleFragment2);
        });

        binding.addProfilePicButton.setOnClickListener(v -> {

            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        }

    }
