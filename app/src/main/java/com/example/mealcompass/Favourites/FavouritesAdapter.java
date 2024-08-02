package com.example.mealcompass.Favourites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.mealcompass.R;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>{
    private final List<FavouritesItem> mFavouritesItems;


    public static class FavouritesViewHolder extends RecyclerView.ViewHolder {
        public ImageView restaurantImage, restaurantFavourite;
        public TextView restaurantName;
        public TextView restaurantAddress;
        public TextView restaurantCuisine;
        public TextView restaurantRating;
        public TextView restaurantPrice;
        public TextView restaurantOpenOrClose;

        public FavouritesViewHolder(View view) {
            super(view);
            restaurantImage = view.findViewById(R.id.restaurantImage);
            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantAddress = view.findViewById(R.id.restaurantAddress);
            restaurantCuisine = view.findViewById(R.id.restaurantCuisine);
            restaurantRating = view.findViewById(R.id.restaurantRating);
            restaurantPrice = view.findViewById(R.id.restaurantPrice);
            restaurantOpenOrClose = view.findViewById(R.id.restaurantOpenOrClose);
            restaurantFavourite = view.findViewById(R.id.restaurantFavourite);

        }
    }


    public FavouritesAdapter(List<FavouritesItem> favouritesItems) {
        this.mFavouritesItems = favouritesItems;
    }


    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        FavouritesItem currentItem = mFavouritesItems.get(position);
        holder.restaurantImage.setImageResource(currentItem.getRestaurantImage());
        holder.restaurantName.setText(currentItem.getRestaurantName());
        holder.restaurantAddress.setText(currentItem.getRestaurantAddress());
        holder.restaurantCuisine.setText(currentItem.getRestaurantCuisine());
        holder.restaurantRating.setText(currentItem.getRestaurantRating());
        holder.restaurantPrice.setText(currentItem.getRestaurantPrice());
        holder.restaurantOpenOrClose.setText(currentItem.getRestaurantOpenOrClose());
    }

    @Override
    public int getItemCount() {
        return mFavouritesItems.size();
    }
}
