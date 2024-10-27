package com.example.mealcompass.Register;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.Restaurant;
import com.example.mealcompass.Restaurants.RestaurantItem;
import com.example.mealcompass.User.UserViewModel;

import java.util.List;

public class ClaimRestaurantAdapter extends RecyclerView.Adapter<ClaimRestaurantAdapter.ViewHolder> {

    private final List<RestaurantItem> mRestaurantItems;
    private UserViewModel userViewModel;
    private OnRestaurantSelectedListener listener;

    public interface OnRestaurantSelectedListener {
        void onRestaurantSelected(String restaurantId, String restaurantName);
    }

    public void setOnRestaurantSelectedListener(OnRestaurantSelectedListener listener) {
        this.listener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantName, restaurantAddress, restaurantCuisine;
        public ImageView restaurantImage;

        public ViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.itemName);
            restaurantAddress = view.findViewById(R.id.itemPrice);
            restaurantCuisine = view.findViewById(R.id.itemCategory);
            restaurantImage = view.findViewById(R.id.menuItemImage);
        }
    }

    public ClaimRestaurantAdapter(List<RestaurantItem> restaurantItems) {
        this.mRestaurantItems = restaurantItems;
    }

    @NonNull
    @Override
    public ClaimRestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_menu_items_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimRestaurantAdapter.ViewHolder holder, int position) {
        RestaurantItem restaurantItem = mRestaurantItems.get(position);
        holder.restaurantName.setText(restaurantItem.getRestaurantName());
        holder.restaurantAddress.setText(String.format("Address: %s", restaurantItem.getRestaurantAddress()));
        holder.restaurantCuisine.setText(String.format("Cuisine: %s", restaurantItem.getRestaurantCuisine()));
        Glide.with(holder.restaurantImage.getContext())
                .load(restaurantItem.getRestaurantImage())
                .into(holder.restaurantImage);

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Selected " + restaurantItem.getRestaurantName(), Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onRestaurantSelected(restaurantItem.getRestaurantId(), restaurantItem.getRestaurantName());
            }
            notifyDataSetChanged();

        });
    }

    @Override
    public int getItemCount() {
        return mRestaurantItems.size();
    }
}
