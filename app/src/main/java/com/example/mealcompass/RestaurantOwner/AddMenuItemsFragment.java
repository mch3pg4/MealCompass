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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantRepository;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.databinding.FragmentAddMenuItemsBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class AddMenuItemsFragment extends Fragment {

    private FragmentAddMenuItemsBinding binding;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private RestaurantViewModel restaurantViewModel;
    private RestaurantRepository restaurantRepository;
    private Uri menuItemUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restaurantRepository = new RestaurantRepository();
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        // Initialize the ActivityResultLauncher
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), result -> {
            if (result != null) {
                // Get the URI of the selected image
                Log.d("AddMenuItemsFragment", "Image URI: " + result);

                menuItemUri = result;
                binding.itemImagePreview.setImageURI(menuItemUri);
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
            // check for empty fields and add menu item and navigate to next fragment
            String itemName = Objects.requireNonNull(binding.itemNameEditText.getText()).toString();
            String itemPrice = Objects.requireNonNull(binding.itemPriceEditText.getText()).toString();
            String itemCategory = Objects.requireNonNull(binding.categorySelectMenu.getEditText()).getText().toString();
            String itemDescription = Objects.requireNonNull(binding.itemDescEditText.getText()).toString();
            String itemNutritionValue = Objects.requireNonNull(binding.itemNutrEditText.getText()).toString();
            String itemAllergens = Objects.requireNonNull(binding.allergyEditText.getText()).toString().trim();

            if (checkEmptyFields(itemName, itemPrice, itemCategory, itemDescription, itemNutritionValue,
                    menuItemUri, itemAllergens)) {
                float price = Float.parseFloat(itemPrice);
                int nutritionValue = Integer.parseInt(itemNutritionValue);
                // Split the allergens by commas and convert it into a List
                List<String> allergiesList = new ArrayList<>(Arrays.asList(itemAllergens.split("\\s*,\\s*")));

                uploadMenuItem(menuItemUri, itemName, price, itemCategory, itemDescription, nutritionValue, allergiesList);
                clearFields();
                // Navigate to the next fragment
                NavHostFragment.findNavController(AddMenuItemsFragment.this)
                        .navigate(R.id.action_addMenuItemsFragment_to_restaurantOwnerFragment2);
            } //all fields are empty then proceed to next fragment
            else if ((itemName.isEmpty() && itemPrice.isEmpty() && itemCategory.isEmpty() && itemDescription.isEmpty() && itemNutritionValue.isEmpty() && itemAllergens.isEmpty())) {
                Toast.makeText(getContext(), "All fields are empty, proceeding to next page", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(AddMenuItemsFragment.this)
                        .navigate(R.id.action_addMenuItemsFragment_to_restaurantOwnerFragment2);
            }
        });

        binding.addMoreButton.setOnClickListener(v -> {
            //check if field is empty then add  menu item and clear the field
            String itemName = Objects.requireNonNull(binding.itemNameEditText.getText()).toString();
            String itemPrice = Objects.requireNonNull(binding.itemPriceEditText.getText()).toString();
            String itemCategory = Objects.requireNonNull(binding.categorySelectMenu.getEditText()).getText().toString();
            String itemDescription = Objects.requireNonNull(binding.itemDescEditText.getText()).toString();
            String itemNutritionValue = Objects.requireNonNull(binding.itemNutrEditText.getText()).toString();
            String itemAllergens = Objects.requireNonNull(binding.allergyEditText.getText()).toString().trim();

            if (checkEmptyFields(itemName, itemPrice, itemCategory, itemDescription, itemNutritionValue,
                    menuItemUri, itemAllergens)) {
                float price = Float.parseFloat(itemPrice);
                int nutritionValue = Integer.parseInt(itemNutritionValue);
                // Split the text by commas and convert it into a List
                List<String> allergiesList = new ArrayList<>(Arrays.asList(itemAllergens.split("\\s*,\\s*")));

                uploadMenuItem(menuItemUri, itemName, price, itemCategory, itemDescription, nutritionValue, allergiesList);
                clearFields();
            }
        });
        }

        // function to clear fields
        private void clearFields() {
            binding.itemNameEditText.setText("");
            binding.itemPriceEditText.setText("");
            Objects.requireNonNull(binding.categorySelectMenu.getEditText()).setText("");
            binding.itemDescEditText.setText("");
            binding.itemNutrEditText.setText("");
            binding.allergyEditText.setText("");
            binding.itemImagePreview.setImageResource(R.drawable.placeholder);
        }

        // function to check for empty fields
        private boolean checkEmptyFields(String itemName, String itemPrice, String itemCategory, String itemDescription, String itemNutritionValue, Uri menuItemUri, String itemAllergens) {
            if (itemName.isEmpty() || itemPrice.isEmpty() || itemCategory.isEmpty() || itemDescription.isEmpty() || itemNutritionValue.isEmpty() || itemAllergens.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return false;
            } else if ( menuItemUri == null) {
                Toast.makeText(getContext(), "Please upload an image for the menu item", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }

        private void uploadMenuItem(Uri menuItemUri, String itemName, float itemPrice, String itemCategory, String itemDescription, int itemNutritionValue, List<String> allergen) {
//            String restaurantId = restaurantViewModel.getRestaurantId().getValue();
            if (getArguments() != null) {
                String restaurantId = getArguments().getString("restaurantId");

                if (restaurantId != null && menuItemUri != null) {
                    //upload the menu item
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("menu_items/" + restaurantId + "/" + itemName + ".jpg");

                    //upload the menu item image to storage
                    storageReference.putFile(menuItemUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                //get the download url of the image
                                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    //upload the menu item details to firestore
                                    restaurantRepository.addMenuItem(restaurantId, itemName, itemDescription, imageUrl, itemPrice, itemNutritionValue, itemCategory, allergen)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Menu item added successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(), "Failed to add menu item", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                });
                            }).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload menu item image to Firebase Storage", Toast.LENGTH_SHORT).show());

                }
            }
        }
    }
