package com.example.mealcompass.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealcompass.R;

import java.util.List;

public class RecommendRestaurantsAdapter extends RecyclerView.Adapter<RecommendRestaurantsAdapter.HomeViewHolder> {
    private final List<RecommendRestaurantsItem> mRecommendRestaurantsItems;




    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        public TextView restaurantName, restaurantRating;
        public ImageView restaurantImage;

        public HomeViewHolder(View view) {
            super(view);
            restaurantImage = view.findViewById(R.id.restaurantImage);
            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantRating = view.findViewById(R.id.restaurantRating);
        }

    }

    public RecommendRestaurantsAdapter(List<RecommendRestaurantsItem> recommendRestaurantsItems) {
        this.mRecommendRestaurantsItems = recommendRestaurantsItems;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_restaurants_item, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        RecommendRestaurantsItem currentItem = mRecommendRestaurantsItems.get(position);
        Glide.with(holder.restaurantImage.getContext())
                .load(currentItem.getRestaurantImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.restaurantImage);
        holder.restaurantName.setText(currentItem.getRestaurantName());
        String restaurantRating = String.valueOf(currentItem.getRestaurantRating());
        holder.restaurantRating.setText("Rating: " + restaurantRating);
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("restaurantId", currentItem.getRestaurantId());
            bundle.putString("restaurantName", currentItem.getRestaurantName());
            bundle.putString("restaurantImage", currentItem.getRestaurantImage());
            bundle.putFloat("restaurantRating", currentItem.getRestaurantRating());
            bundle.putString("restaurantCuisine", currentItem.getRestaurantCuisine());
            bundle.putString("restaurantAddress", currentItem.getRestaurantAddress());
            bundle.putString("restaurantContact", currentItem.getRestaurantContact());
            bundle.putString("restaurantOpenOrClose", currentItem.getRestaurantBusinessHours());


            int actionId = R.id.action_homeFragment_to_restaurantDetailsFragment;
            if (Navigation.findNavController(v).getCurrentDestination().getId() == R.id.adminFragment) {
                actionId = R.id.action_adminFragment_to_restaurantDetailsFragment;
            }
            Navigation.findNavController(v).navigate(actionId, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return mRecommendRestaurantsItems.size();
    }

}
