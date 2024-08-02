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
        holder.restaurantImage.setImageResource(currentItem.getRestaurantImage());
        holder.restaurantName.setText(currentItem.getRestaurantName());
        holder.restaurantRating.setText(currentItem.getRestaurantRating());
    }

    @Override
    public int getItemCount() {
        return mRecommendHistoryItems.size();
    }


}
