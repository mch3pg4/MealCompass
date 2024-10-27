package com.example.mealcompass.MenuItem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavOptionsDsl;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class FullMenuAdapter extends RecyclerView.Adapter<FullMenuAdapter.ViewHolder> {

    private final List<MenuItem> mMenuItems;
    private UserViewModel userViewModel;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView menuItemName, menuItemPrice;
        public TextView menuItemCategory;
        public ImageView menuItemImage;

        public ViewHolder(View view) {
            super(view);
            menuItemName = view.findViewById(R.id.itemName);
            menuItemPrice = view.findViewById(R.id.itemPrice);
            menuItemCategory = view.findViewById(R.id.itemCategory);
            menuItemImage = view.findViewById(R.id.menuItemImage);

        }
    }

        public FullMenuAdapter(List<MenuItem> menuItems) {
            this.mMenuItems = menuItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            userViewModel = new UserViewModel();
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_menu_items_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MenuItem menuItem = mMenuItems.get(position);
            holder.menuItemName.setText(menuItem.getMenuItemName());
            holder.menuItemPrice.setText(String.format("RM %s", menuItem.getMenuItemPrice()));
            holder.menuItemCategory.setText(String.format("Category: %s", menuItem.getMenuItemCategory()));
            Glide.with(holder.menuItemImage.getContext())
                    .load(menuItem.getMenuItemImage() != null && !menuItem.getMenuItemImage().isEmpty() ? menuItem.getMenuItemImage() : R.drawable.placeholder)
                    .into(holder.menuItemImage);

            holder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("menuItemId", menuItem.getMenuItemId());
                bundle.putString("menuItemName", menuItem.getMenuItemName());
                bundle.putString("menuItemDescription", menuItem.getMenuItemDescription());
                bundle.putInt("menuItemPrice", menuItem.getMenuItemPrice());
                bundle.putString("menuItemCategory", menuItem.getMenuItemCategory());
                bundle.putStringArrayList("menuItemAllergens", (ArrayList<String>) menuItem.getMenuItemAllergens());
                bundle.putBoolean("isItemBestSeller", menuItem.isItemBestSeller());
                bundle.putString("menuItemImage", menuItem.getMenuItemImage());
                bundle.putInt("menuItemNutritionalValue", menuItem.getMenuItemNutritionalValue());

                Navigation.findNavController(holder.itemView).navigate(R.id.action_fullMenuFragment_to_menuItemFragment, bundle);
            });


        }



    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    }

