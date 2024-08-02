package com.example.mealcompass.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.restaurantImage.setImageResource(currentItem.getRestaurantImage());
        holder.restaurantName.setText(currentItem.getRestaurantName());
        holder.restaurantRating.setText(currentItem.getRestaurantRating());
    }

    @Override
    public int getItemCount() {
        return mRecommendRestaurantsItems.size();
    }

}
