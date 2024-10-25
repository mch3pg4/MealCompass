package com.example.mealcompass.MenuItem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentMenuItemBinding;

import java.util.List;

public class MenuItemFragment extends Fragment {
    private FragmentMenuItemBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMenuItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
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
            binding.itemAllergens.setText(itemAllergens.isEmpty() ? "No allergens" : String.join(", ", itemAllergens));
            binding.itemCalories.setText(String.format("Calories: %s kcal", itemNutritionalValue));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}