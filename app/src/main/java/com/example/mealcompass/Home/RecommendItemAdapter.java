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

public class RecommendItemAdapter extends RecyclerView.Adapter<RecommendItemAdapter.RecommendItemViewHolder> {
    private final List<RecommendItemItem> mRecommendItemItems;

    public static class RecommendItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemName;

        public RecommendItemViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);

        }
    }

    public RecommendItemAdapter(List<RecommendItemItem> recommendItemItems) {
        this.mRecommendItemItems = recommendItemItems;
    }

    @NonNull
    @Override
    public RecommendItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_item, parent, false);
        return new RecommendItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendItemViewHolder holder, int position) {
        RecommendItemItem currentItem = mRecommendItemItems.get(position);
        holder.itemImage.setImageResource(currentItem.getItemImage());
        holder.itemName.setText(currentItem.getItemName());
    }

    @Override
    public int getItemCount() {
        return mRecommendItemItems.size();
    }
}
