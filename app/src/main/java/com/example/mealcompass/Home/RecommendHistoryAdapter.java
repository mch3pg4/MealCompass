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

public class RecommendHistoryAdapter extends RecyclerView.Adapter<RecommendHistoryAdapter.RecommendHistoryViewHolder> {
    private final List<RecommendHistoryItem> mRecommendHistoryItems;

    public static class RecommendHistoryViewHolder extends RecyclerView.ViewHolder {
        public ImageView restaurantImage;
        public TextView restaurantName, restaurantRating;

        public RecommendHistoryViewHolder(View itemView) {
            super(itemView);

            restaurantImage = itemView.findViewById(R.id.restaurantImage);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            restaurantRating = itemView.findViewById(R.id.restaurantRating);

        }
    }

    public RecommendHistoryAdapter(List<RecommendHistoryItem> recommendHistoryItems) {
        this.mRecommendHistoryItems = recommendHistoryItems;
    }

    @NonNull
    @Override
    public RecommendHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_card, parent, false);
        return new RecommendHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendHistoryViewHolder holder, int position) {
        RecommendHistoryItem currentItem = mRecommendHistoryItems.get(position);
        Glide.with(holder.restaurantImage.getContext())
                .load(currentItem.getRestaurantImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.restaurantImage);
        holder.restaurantName.setText(currentItem.getRestaurantName());
        holder.restaurantRating.setText(String.format("Recommendation rated: %s stars", currentItem.getRestaurantRating()));
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("restaurantId", currentItem.getRestaurantId());
            bundle.putString("restaurantName", currentItem.getRestaurantName());
            bundle.putString("restaurantImage", currentItem.getRestaurantImage());
            bundle.putString("restaurantAddress", currentItem.getRestaurantAddress());
            bundle.putString("restaurantCuisine", currentItem.getRestaurantCuisine());
            bundle.putString("restaurantContact", currentItem.getRestaurantContact());
            bundle.putString("restaurantOpenOrClose", currentItem.getRestaurantBusinessHours());
            bundle.putInt("restaurantPricing", currentItem.getRestaurantPricing());
            bundle.putFloat("restaurantRating", currentItem.getRestaurantRating());
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_restaurantDetailsFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return mRecommendHistoryItems.size();
    }


}
