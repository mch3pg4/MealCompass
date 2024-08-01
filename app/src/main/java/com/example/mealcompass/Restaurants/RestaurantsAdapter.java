package com.example.mealcompass.Restaurants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.R;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>{

    private final List<RestaurantItem> mRestaurantItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantName, restaurantAddress, restaurantCuisine, restaurantRating, restaurantPrice, restaurantOpenOrClose;
        public ImageView restaurantImage, restaurantFavourite;

        public ViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantAddress = view.findViewById(R.id.restaurantAddress);
            restaurantCuisine = view.findViewById(R.id.restaurantCuisine);
            restaurantRating = view.findViewById(R.id.restaurantRating);
            restaurantPrice = view.findViewById(R.id.restaurantPrice);
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
        holder.restaurantAddress.setText(restaurantItem.getRestaurantAddress());
        holder.restaurantCuisine.setText(restaurantItem.getRestaurantCuisine());
        holder.restaurantRating.setText(restaurantItem.getRestaurantRating());
        holder.restaurantPrice.setText(restaurantItem.getRestaurantPrice());
        holder.restaurantOpenOrClose.setText(restaurantItem.getRestaurantOpenOrClose());
        holder.restaurantImage.setImageResource(restaurantItem.getRestaurantImage());
        holder.restaurantFavourite.setImageResource(restaurantItem.getRestaurantFavourite());
    }

    @Override
    public int getItemCount() {
        return mRestaurantItems.size();
    }
}
