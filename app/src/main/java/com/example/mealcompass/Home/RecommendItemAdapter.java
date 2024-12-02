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

import java.util.ArrayList;
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
        this.mRecommendItemItems = recommendItemItems != null ? recommendItemItems : new ArrayList<>();
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
        Glide.with(holder.itemImage.getContext())
                .load(currentItem.getItemImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(holder.itemImage);
        holder.itemName.setText(currentItem.getItemName());
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("menuItemName", currentItem.getItemName());
            bundle.putString("menuItemImage", currentItem.getItemImage());
            bundle.putInt("menuItemPrice", currentItem.getItemPrice());
            bundle.putString("menuItemCategory", currentItem.getItemCategory());
            bundle.putString("menuItemDescription", currentItem.getItemDescription());
            bundle.putStringArrayList("menuItemAllergens", (ArrayList<String>) currentItem.getItemAllergens());
            bundle.putInt("menuItemNutritionalValue", currentItem.getItemNutritionalValue());

            // Navigate to the MenuItemFragment
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_menuItemFragment, bundle);

        });
    }

    @Override
    public int getItemCount() {
        return mRecommendItemItems.size();
    }
}
