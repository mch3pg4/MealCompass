package com.example.mealcompass.MenuItem;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantRepository;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.databinding.FragmentEditMenuItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EditMenuItemFragment extends Fragment {
    private FragmentEditMenuItemBinding binding;
    private UserRepository userRepository;
    private FirebaseAuth mAuth;
    private RestaurantViewModel restaurantViewModel;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Uri menuItemUri;
    private RestaurantRepository restaurantRepository;
    private String restaurantId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        restaurantRepository = new RestaurantRepository();

        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        // Initialize the ActivityResultLauncher
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), result -> {
            if (result != null) {
                // Get the URI of the selected image
                Log.d("EditMenuItemFragment", "Image URI: " + result);

                menuItemUri = result;
                binding.itemImagePreview.setImageURI(menuItemUri);
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditMenuItemBinding.inflate(inflater, container, false);
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

        // get restaurant id from user id
        restaurantViewModel.fetchRestaurantByOwnerId(userId);
        restaurantViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurant -> {
            if (restaurant != null) {
                restaurantId = restaurant.get(0).getRestaurantId();
            } else {
                Toast.makeText(requireContext(), "Failed to get restaurant ID", Toast.LENGTH_SHORT).show();
            }
        });

        List<String> items = Arrays.asList(getResources().getStringArray(R.array.menu_item_category_list));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.cuisine_list_item, items);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) binding.categorySelectMenu.getEditText();
        if (autoCompleteTextView != null) {
            autoCompleteTextView.setAdapter(adapter);
        }

        // if bundle is empty, then we are adding a new menu item
        if (getArguments() == null) {
            binding.editMenuItemsTitle.setText(R.string.add_menu_items);
            binding.doneButton.setText(R.string.add);
        } else {
            // if bundle is not empty, then we are editing an existing menu item
            // set edittext to bundle data
            binding.addItemImageButton.setText(R.string.edit_item_image);
            binding.doneButton.setText(R.string.edit);
            binding.itemNameEditText.setText(getArguments().getString("menuItemName"));
            binding.itemDescEditText.setText(getArguments().getString("menuItemDescription"));
            binding.categoryAutoCompleteTextView.setText(getArguments().getString("menuItemCategory"));
            binding.itemPriceEditText.setText(String.valueOf(getArguments().getInt("menuItemPrice")));
            binding.itemNutrEditText.setText(String.valueOf(getArguments().getInt("menuItemNutritionalValue")));
            ArrayList<String> allergens = getArguments().getStringArrayList("menuItemAllergens");
            binding.allergyEditText.setText(allergens != null ? TextUtils.join(", ", allergens) : "");
            Glide.with(requireContext())
                    .load(getArguments().getString("menuItemImage"))
                    .placeholder(R.drawable.placeholder)
                    .into(binding.itemImagePreview);
        }

        binding.addItemImageButton.setOnClickListener(v -> {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        binding.doneButton.setOnClickListener(v -> {
            // get all the data from the edittext
            String itemName = binding.itemNameEditText.getText().toString();
            String itemDesc = binding.itemDescEditText.getText().toString();
            String itemCategory = binding.categoryAutoCompleteTextView.getText().toString();
            String itemPrice = binding.itemPriceEditText.getText().toString();
            String itemNutr = binding.itemNutrEditText.getText().toString();
            String itemAllergens = binding.allergyEditText.getText().toString();


            // check if any of the fields are empty
            if (TextUtils.isEmpty(itemName) || TextUtils.isEmpty(itemDesc) || TextUtils.isEmpty(itemCategory) || TextUtils.isEmpty(itemPrice) || TextUtils.isEmpty(itemNutr) || TextUtils.isEmpty(itemAllergens)) {
                Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            } else {
                // if bundle is empty, then we are adding a new menu item
                if (getArguments() == null) {
                    // add new menu item
                    Toast.makeText(requireContext(), "Adding new menu item", Toast.LENGTH_SHORT).show();
                    uploadMenuItem(menuItemUri, itemName, Float.parseFloat(itemPrice), itemCategory, itemDesc, Integer.parseInt(itemNutr), Arrays.asList(itemAllergens.split(", ")));
                } else {
                    // if bundle is not empty, then we are editing an existing menu item
                    // edit existing menu item
                    Toast.makeText(requireContext(), "Editing menu item", Toast.LENGTH_SHORT).show();
                    editMenuItem(menuItemUri, itemName, Float.parseFloat(itemPrice), itemCategory, itemDesc, Integer.parseInt(itemNutr), Arrays.asList(itemAllergens.split(", ")));

                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // for checking if there is a menu item image to upload
    private void editMenuItem(Uri menuItemUri, String itemName, float itemPrice, String itemCategory, String itemDescription, int itemNutritionValue, List<String> allergen) {
        if (getArguments() != null) {
            String menuItemId = getArguments().getString("menuItemId");
            String existingImageUrl = getArguments().getString("menuItemImage");

            if (restaurantId != null) {
                if (menuItemUri != null) {
                    // Upload the new menu item image to storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("menu_items/" + restaurantId + "/" + itemName + ".jpg");
                    storageReference.putFile(menuItemUri)
                            .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                updateMenuItem(menuItemId, itemName, itemDescription, imageUrl, itemPrice, itemCategory, itemNutritionValue, allergen);
                            })).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload menu item image to Firebase Storage", Toast.LENGTH_SHORT).show());
                } else {
                    // Use the existing image URL
                    updateMenuItem(menuItemId, itemName, itemDescription, existingImageUrl, itemPrice, itemCategory, itemNutritionValue, allergen);
                }
            }
        }
    }

    // for updating an existing menu item
    private void updateMenuItem(String menuItemId, String itemName, String itemDescription, String imageUrl, float itemPrice, String itemCategory, int itemNutritionValue, List<String> allergen) {
        restaurantRepository.updateMenuItem(restaurantId, menuItemId, itemName, itemDescription, imageUrl, itemPrice, itemNutritionValue, itemCategory, allergen)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Menu item edited successfully", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(EditMenuItemFragment.this)
                                .navigate(R.id.action_editMenuItemFragment_to_fullMenuFragment);
                    } else {
                        Toast.makeText(getContext(), "Failed to edit menu item", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // for adding a new menu item
    private void uploadMenuItem(Uri menuItemUri, String itemName, float itemPrice, String itemCategory, String itemDescription, int itemNutritionValue, List<String> allergen) {
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
                                                NavHostFragment.findNavController(EditMenuItemFragment.this)
                                                        .navigate(R.id.action_editMenuItemFragment_to_fullMenuFragment);
                                            } else {
                                                Toast.makeText(getContext(), "Failed to add menu item", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            });
                        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload menu item image to Firebase Storage", Toast.LENGTH_SHORT).show());

            } else if (menuItemUri == null) {
                Toast.makeText(getContext(), "Please upload an image for your menu item", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to get restaurant ID", Toast.LENGTH_SHORT).show();
            }
        }
}