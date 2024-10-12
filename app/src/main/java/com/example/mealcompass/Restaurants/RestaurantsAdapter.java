package com.example.mealcompass.Restaurants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>{

    private final List<RestaurantItem> mRestaurantItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantName, restaurantAddress, restaurantCuisine, restaurantRating, restaurantPricing, restaurantOpenOrClose;
        public ImageView restaurantImage;
        public CheckBox restaurantFavourite;

        public ViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantAddress = view.findViewById(R.id.restaurantAddress);
            restaurantCuisine = view.findViewById(R.id.restaurantCuisine);
            restaurantRating = view.findViewById(R.id.restaurantRating);
            restaurantPricing = view.findViewById(R.id.restaurantPrice);
            restaurantOpenOrClose = view.findViewById(R.id.restaurantOpenOrClose);
            restaurantImage = view.findViewById(R.id.restaurantImage);
            restaurantFavourite = view.findViewById(R.id.restaurantFavourite);
        }
    }

    public RestaurantsAdapter(List<RestaurantItem> restaurantItems) {
        this.mRestaurantItems = restaurantItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantItem restaurantItem = mRestaurantItems.get(position);
        holder.restaurantName.setText(restaurantItem.getRestaurantName());
        holder.restaurantAddress.setText(String.format("Address: %s", restaurantItem.getRestaurantAddress()));
        holder.restaurantCuisine.setText(String.format("Cuisine: %s", restaurantItem.getRestaurantCuisine()));
        holder.restaurantRating.setText(String.format("Rating: %s/5.0", restaurantItem.getRestaurantRating()));
        holder.restaurantPricing.setText(String.format("Pricing: %s", restaurantPricing(restaurantItem.getRestaurantPricing())));
        holder.restaurantOpenOrClose.setText(restaurantItem.getRestaurantOpenOrClose());
        Glide.with(holder.restaurantImage.getContext())
                .load(restaurantItem.getRestaurantImage())
                .into(holder.restaurantImage);
        // Set initial state of the CheckBox
        holder.restaurantFavourite.setChecked(restaurantItem.isRestaurantFavourite());

        // Handle the CheckBox state change
        holder.restaurantFavourite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update your data model if needed
            restaurantItem.setRestaurantFavourite(isChecked);
        });
    }

   public String restaurantPricing(float price) {
    if (price == 1) {
        return "$";
    } else if (price == 2) {
        return "$$";
    } else if (price == 3) {
        return "$$$";
    } else if (price == 4) {
        return "$$$$";
    } else {
        return "N/A";
    }
}

    @Override
    public int getItemCount() {
        return mRestaurantItems.size();
    }
}
