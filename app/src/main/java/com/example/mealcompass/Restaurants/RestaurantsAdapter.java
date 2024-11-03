package com.example.mealcompass.Restaurants;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private final List<RestaurantItem> mRestaurantItems;
    private UserViewModel userViewModel;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantName, restaurantAddress, restaurantCuisine, restaurantRating, restaurantPricing, restaurantOpenOrClose;
        public ImageView restaurantImage;
        public CheckBox restaurantFavourite;

        public ViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantAddress = view.findViewById(R.id.restaurantAddress);
            restaurantCuisine = view.findViewById(R.id.restaurantCuisine);
            restaurantRating = view.findViewById(R.id.restaurantRating);
            restaurantPricing = view.findViewById(R.id.restaurantPrice);
            restaurantOpenOrClose = view.findViewById(R.id.restaurantOpenOrClose);
            restaurantImage = view.findViewById(R.id.restaurantImage);
            restaurantFavourite = view.findViewById(R.id.restaurantFavourite);
        }
    }

    public RestaurantsAdapter(List<RestaurantItem> restaurantItems) {
        this.mRestaurantItems = restaurantItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        userViewModel = new UserViewModel();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantItem restaurantItem = mRestaurantItems.get(position);
        holder.restaurantName.setText(restaurantItem.getRestaurantName());
        holder.restaurantAddress.setText(String.format("Address: %s", restaurantItem.getRestaurantAddress()));
        holder.restaurantCuisine.setText(String.format("Cuisine: %s", restaurantItem.getRestaurantCuisine()));
        holder.restaurantRating.setText(String.format("Rating: %s/5.0", restaurantItem.getRestaurantRating()));
        holder.restaurantPricing.setText(String.format("Pricing: %s", restaurantPricing(restaurantItem.getRestaurantPricing())));
        if (isRestaurantOpen(restaurantItem.getRestaurantOpenOrClose())) {
            holder.restaurantOpenOrClose.setTextColor(holder.restaurantOpenOrClose.getContext().getResources().getColor(R.color.green));
            holder.restaurantOpenOrClose.setText(R.string.open);
        } else {
            holder.restaurantOpenOrClose.setTextColor(holder.restaurantOpenOrClose.getContext().getResources().getColor(R.color.red));
            holder.restaurantOpenOrClose.setText(R.string.closed);
        }
        holder.restaurantOpenOrClose.setText(isRestaurantOpen(restaurantItem.getRestaurantOpenOrClose()) ? "OPEN" : "CLOSED");
        Glide.with(holder.restaurantImage.getContext())
                .load(restaurantItem.getRestaurantImage())
                .into(holder.restaurantImage);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("restaurantId", restaurantItem.getRestaurantId());
            bundle.putString("restaurantName", restaurantItem.getRestaurantName());
            bundle.putString("restaurantAddress", restaurantItem.getRestaurantAddress());
            bundle.putString("restaurantCuisine", restaurantItem.getRestaurantCuisine());
            bundle.putFloat("restaurantRating", restaurantItem.getRestaurantRating());
            bundle.putInt("restaurantPricing", restaurantItem.getRestaurantPricing());
            bundle.putString("restaurantOpenOrClose", restaurantItem.getRestaurantOpenOrClose());
            bundle.putString("restaurantImage", restaurantItem.getRestaurantImage());
            bundle.putBoolean("isRestaurantFavourite", restaurantItem.isRestaurantFavourite());
            bundle.putString("restaurantContact", restaurantItem.getRestaurantContact());
            // Navigate to restaurant details
            Navigation.findNavController(holder.itemView).navigate(R.id.action_restaurantsFragment_to_restaurantDetailsFragment, bundle);
        });

        // Set the CheckBox state
        userViewModel.isInFavourites(restaurantItem.getRestaurantId(), new UserRepository.FavoriteRestaurantCallback() {
            @Override
            public void onSuccess(boolean isFavourite) {
                holder.restaurantFavourite.setOnCheckedChangeListener(null); // Remove listener before setting state
                holder.restaurantFavourite.setChecked(isFavourite);
                holder.restaurantFavourite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    // handle checkbox state change
                    if (isChecked) {
                        restaurantItem.setRestaurantFavourite(true);
                        Toast.makeText(buttonView.getContext(), "Added " + restaurantItem.getRestaurantName() + " to favourites", Toast.LENGTH_SHORT).show();
                        // add to favourites
                        userViewModel.addFavouriteRestaurant(restaurantItem.getRestaurantId());
                    } else {
                        restaurantItem.setRestaurantFavourite(false);
                        Toast.makeText(buttonView.getContext(), "Removed " + restaurantItem.getRestaurantName() + " from favourites", Toast.LENGTH_SHORT).show();
                        // remove from favourites
                        userViewModel.removeFavouriteRestaurant(restaurantItem.getRestaurantId());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                holder.restaurantFavourite.setChecked(false);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurantItems.size();
    }

    public String restaurantPricing(float price) {
        if (price == 1) {
            return "$";
        } else if (price == 2) {
            return "$$";
        } else if (price == 3) {
            return "$$$";
        } else if (price == 4) {
            return "$$$$";
        } else {
            return "N/A";
        }
    }

    private boolean isRestaurantOpen(String businessHours) {
        try {
            JSONObject businessHoursJson = new JSONObject(businessHours);

            // get current day of the week
            Calendar calendar = Calendar.getInstance();
            String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime());
            String time = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(calendar.getTime());

            if (!businessHoursJson.has(day) || "Closed".equalsIgnoreCase(businessHoursJson.getString(day))) {
                return false;
            }

            for (String timeSlot : businessHoursJson.getString(day).split(", ")) {
                String[] times = timeSlot.split(" to ");
                if (times.length == 2 && isTimeInRange(time, times[0], times[1])) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e("RestaurantsAdapter", "isRestaurantOpen: " + e.getMessage());
        }
        return false;

    }

    private boolean isTimeInRange(String currentTime, String startTime, String endTime) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date current = timeFormat.parse(currentTime);
            Date start = parseTime(startTime);
            Date end = parseTime(endTime);

            // Handle time range that might cross midnight
            if (start.after(end)) {
                return current.after(start) || current.before(end);
            } else {
                return !current.before(start) && !current.after(end);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Date parseTime(String timeStr) throws ParseException {
        SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        timeStr = timeStr.toLowerCase().replaceAll("\\s", "");
        String time = timeStr.replaceAll("[apm]", "");
        String period = timeStr.replaceAll("[^apm]", "");

        if (!time.contains(":")) {
            time = time + ":00";
        }

        if (period.isEmpty()) {
            // If period (am/pm) is missing, assume it's PM for hours less than 12
            period = Integer.parseInt(time.split(":")[0]) < 12 ? "pm" : "am";
        }

        return TIME_FORMAT.parse(time + " " + period);
    }
}

