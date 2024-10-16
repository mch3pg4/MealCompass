package com.example.mealcompass.Favourites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserViewModel;

import org.json.JSONObject;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>{
    private final List<FavouritesItem> mFavouritesItems;
    private UserViewModel userViewModel;


    public static class FavouritesViewHolder extends RecyclerView.ViewHolder {
        public ImageView restaurantImage;
        public TextView restaurantName;
        public TextView restaurantAddress;
        public TextView restaurantCuisine;
        public TextView restaurantRating;
        public TextView restaurantPrice;
        public TextView restaurantOpenOrClose;
        public CheckBox restaurantFavourite;

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
        userViewModel = new UserViewModel();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        FavouritesItem currentItem = mFavouritesItems.get(position);
        Glide.with(holder.restaurantImage.getContext())
                .load(currentItem.getRestaurantImage())
                .into(holder.restaurantImage);
        holder.restaurantName.setText(currentItem.getRestaurantName());
        holder.restaurantAddress.setText(String.format("Address: %s", currentItem.getRestaurantAddress()));
        holder.restaurantCuisine.setText(String.format("Cuisine: %s", currentItem.getRestaurantCuisine()));
        holder.restaurantRating.setText(String.format("Rating: %s/5.0", currentItem.getRestaurantRating()));
        holder.restaurantPrice.setText(String.format("Pricing: %s", restaurantPricing(currentItem.getRestaurantPrice())));
        if (isRestaurantOpen(currentItem.getRestaurantOpenOrClose())) {
            holder.restaurantOpenOrClose.setTextColor(holder.restaurantOpenOrClose.getContext().getResources().getColor(R.color.green));
            holder.restaurantOpenOrClose.setText(R.string.open);
        } else {
            holder.restaurantOpenOrClose.setTextColor(holder.restaurantOpenOrClose.getContext().getResources().getColor(R.color.red));
            holder.restaurantOpenOrClose.setText(R.string.closed);
        }
        holder.restaurantOpenOrClose.setText(isRestaurantOpen(currentItem.getRestaurantOpenOrClose()) ? "OPEN" : "CLOSED");        // Set initial state of the CheckBox
        holder.restaurantFavourite.setChecked(currentItem.isRestaurantFavourite());

        // Handle the CheckBox state change
        holder.restaurantFavourite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                // Remove the item from the list
                userViewModel.removeFavouriteRestaurant(currentItem.getRestaurantId());
                Toast.makeText(holder.restaurantFavourite.getContext(), "Removed " + currentItem.getRestaurantName() + " from favourites", Toast.LENGTH_SHORT).show();
                mFavouritesItems.remove(currentItem);
                notifyItemRemoved(position);


            }
        });
    }

    @Override
    public int getItemCount() {
        return mFavouritesItems.size();
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
            e.printStackTrace();
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
