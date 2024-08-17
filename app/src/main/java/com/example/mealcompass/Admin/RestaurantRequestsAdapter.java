package com.example.mealcompass.Admin;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RestaurantRequestsAdapter extends RecyclerView.Adapter<RestaurantRequestsAdapter.RestaurantRequestsViewHolder> {

    private final List<RestaurantRequestsItem> mRestaurantRequestsItems;

    public static class RestaurantRequestsViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantName, restaurantCuisine;
        public ImageView restaurantImage;
        public MaterialButton acceptButton, rejectButton;

        public RestaurantRequestsViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantCuisine = view.findViewById(R.id.restaurantCuisine);
            restaurantImage = view.findViewById(R.id.restaurantImage);
            acceptButton = view.findViewById(R.id.acceptButton);
            rejectButton = view.findViewById(R.id.rejectButton);
    }
}

    public RestaurantRequestsAdapter(List<RestaurantRequestsItem> restaurantRequestsItems) {
        this.mRestaurantRequestsItems = restaurantRequestsItems;
    }

    @Override
    public RestaurantRequestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_request_card, parent, false);
        return new RestaurantRequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantRequestsViewHolder holder, int position) {
        RestaurantRequestsItem restaurantRequestsItem = mRestaurantRequestsItems.get(position);
        holder.restaurantName.setText(restaurantRequestsItem.getRestaurantName());
        holder.restaurantCuisine.setText(restaurantRequestsItem.getRestaurantCuisine());
        holder.restaurantImage.setImageResource(R.drawable.restaurant_img);
        holder.acceptButton.setOnClickListener(v -> {
            // accept button logic
            Toast.makeText(v.getContext(), "Accepted", Toast.LENGTH_SHORT).show();
        });
        holder.rejectButton.setOnClickListener(v -> {
            // reject button logic
            Toast.makeText(v.getContext(), "Rejected", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurantRequestsItems.size();
    }
}
