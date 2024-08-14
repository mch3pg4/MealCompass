package com.example.mealcompass.RestaurantOwner;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentAddMenuItemsBinding;

import java.util.Arrays;
import java.util.List;


public class AddMenuItemsFragment extends Fragment {

    private FragmentAddMenuItemsBinding binding;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ActivityResultLauncher
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), result -> {
            if (result != null) {
                // Get the URI of the selected image
                Log.d("AddMenuItemsFragment", "Image URI: " + result);

                binding.itemImagePreview.setImageURI(result);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddMenuItemsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> items = Arrays.asList(getResources().getStringArray(R.array.menu_item_category_list));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.cuisine_list_item, items);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) binding.categorySelectMenu.getEditText();
        if (autoCompleteTextView != null) {
            autoCompleteTextView.setAdapter(adapter);
        }

        binding.addItemImageButton.setOnClickListener(v -> {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        binding.prevButton.setOnClickListener(v -> NavHostFragment.findNavController(AddMenuItemsFragment.this)
                .navigate(R.id.action_addMenuItemsFragment_to_addRestImageFragment));

        binding.nextButton.setOnClickListener(v -> {
            // Navigate to the next fragment
            NavHostFragment.findNavController(AddMenuItemsFragment.this)
                    .navigate(R.id.action_addMenuItemsFragment_to_restaurantOwnerFragment);
        });

        binding.addMoreButton.setOnClickListener(v -> {
            // Navigate to the next fragment
            NavHostFragment.findNavController(AddMenuItemsFragment.this)
                    .navigate(R.id.action_addMenuItemsFragment_self);
        });
        }

    }
