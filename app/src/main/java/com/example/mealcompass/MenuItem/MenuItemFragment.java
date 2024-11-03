package com.example.mealcompass.MenuItem;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantRepository;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.databinding.FragmentMenuItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MenuItemFragment extends Fragment {
    private FragmentMenuItemBinding binding;
    private UserRepository userRepository;
    private FirebaseAuth mAuth;
    private RestaurantViewModel restaurantViewModel;
    private RestaurantRepository restaurantRepository;
    private String restaurantId;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        restaurantRepository = new RestaurantRepository();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMenuItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        String userId;
        if (user != null) {
            userId = user.getUid();
        } else {
            userId = null;
        }

        // if usertype is not owner, dont show the edit fab
        if (userId != null) {
            userRepository.getUserType(userId).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String userType = task.getResult();
                    if (userType != null && userType.equals("owner")) {
                        binding.editFab.setVisibility(View.VISIBLE);
                        binding.deleteFab.setVisibility(View.VISIBLE);
                        // get restaurant id from user id
                        restaurantViewModel.fetchRestaurantByOwnerId(userId);
                        restaurantViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurant -> {
                            if (restaurant != null) {
                                restaurantId = restaurant.get(0).getRestaurantId();
                            } else {
                                Toast.makeText(requireContext(), "Failed to get restaurant ID", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        binding.editFab.setVisibility(View.GONE);
                        binding.deleteFab.setVisibility(View.GONE);
                    }
                }
            });
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String itemId = bundle.getString("menuItemId");
            String itemName = bundle.getString("menuItemName");
            int itemPrice = bundle.getInt("menuItemPrice");
            String itemCategory = bundle.getString("menuItemCategory");
            String itemImage = bundle.getString("menuItemImage");
            String itemDescription = bundle.getString("menuItemDescription");
            List<String> itemAllergens = bundle.getStringArrayList("menuItemAllergens");
            int itemNutritionalValue = bundle.getInt("menuItemNutritionalValue");

            binding.itemName.setText( itemName);
            binding.itemPrice.setText(String.format("Price: RM %s", itemPrice));
            binding.itemCategory.setText(String.format("Category: %s", itemCategory));
            Glide.with(this)
                .load(itemImage != null && !itemImage.isEmpty() ? itemImage : R.drawable.placeholder)
                .into(binding.itemImage);
            binding.itemDescription.setText(String.format("Description: %s", itemDescription));
            binding.itemAllergens.setText(itemAllergens.isEmpty() ? "No allergens" : "Allergens: " + String.join(", ", itemAllergens));
            binding.itemCalories.setText(String.format("Calories: %s kcal", itemNutritionalValue));
        }

        binding.editFab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_menuItemFragment_to_editMenuItemFragment, bundle));

        binding.deleteFab.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Delete menu item");
            builder.setMessage("Are you sure you want to delete " + bundle.getString("menuItemName") +" ?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // delete menu item
                if (restaurantId != null && bundle.getString("menuItemId") != null) {
                    String menuItemId = bundle.getString("menuItemId");
                    restaurantRepository.deleteMenuItem(restaurantId, menuItemId)
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Failed to delete menu item", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Menu item deleted", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(v).navigate(R.id.action_menuItemFragment_to_fullMenuFragment);
                                }
                            });
                } else {
                    Toast.makeText(requireContext(), "Failed to delete menu item", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}