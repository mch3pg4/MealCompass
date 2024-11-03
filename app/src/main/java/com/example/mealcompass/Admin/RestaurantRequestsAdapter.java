package com.example.mealcompass.Admin;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantRepository;
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

    @NonNull
    @Override
    public RestaurantRequestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_request_card, parent, false);
        return new RestaurantRequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantRequestsViewHolder holder, int position) {
        RestaurantRequestsItem restaurantRequestsItem = mRestaurantRequestsItems.get(position);
        holder.restaurantName.setText(restaurantRequestsItem.getRestaurantName());
        holder.restaurantCuisine.setText(String.format("Cuisine: %s", restaurantRequestsItem.getRestaurantCuisine()));
        Glide.with(holder.restaurantImage.getContext())
                .load(restaurantRequestsItem.getRestaurantImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.restaurantImage);
        holder.acceptButton.setOnClickListener(v -> {
            // show alert dialog to confirm acceptance
            AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                    .setTitle("Accept Restaurant Request")
                    .setMessage("Are you sure you want to accept this restaurant request?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // accept button logic
                        RestaurantRepository restaurantRepository = new RestaurantRepository();
                        restaurantRepository.updateRestaurantStatus(restaurantRequestsItem.getRestaurantId(), "Accepted")
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(v.getContext(), "Accepted " + restaurantRequestsItem.getRestaurantName(), Toast.LENGTH_SHORT).show();
                                        // remove the accepted restaurant from the adapter
                                        mRestaurantRequestsItems.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, mRestaurantRequestsItems.size());
                                    } else {
                                        Toast.makeText(v.getContext(), "Failed to accept " + restaurantRequestsItem.getRestaurantName(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create();
            alertDialog.show();

        });
        holder.rejectButton.setOnClickListener(v -> {
            // show alert dialog to confirm deletion
            AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                    .setTitle("Reject Restaurant Request")
                    .setMessage("Are you sure you want to reject this restaurant request?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // reject button logic
                        RestaurantRepository restaurantRepository = new RestaurantRepository();
                        restaurantRepository.updateRestaurantStatus(restaurantRequestsItem.getRestaurantId(), "Pending")
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(v.getContext(), "Rejected " + restaurantRequestsItem.getRestaurantName() +". Restaurant status will be set to 'Pending'.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(v.getContext(), "Failed to reject " + restaurantRequestsItem.getRestaurantName(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurantRequestsItems.size();
    }
}
