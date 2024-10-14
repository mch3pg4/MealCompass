package com.example.mealcompass.Restaurants;

public class RestaurantItem {
    private final String restaurantId;
    private final String restaurantImage;
    private final String restaurantName;
    private final String restaurantAddress;
    private final String restaurantCuisine;
    private final float restaurantRating;
    private final int restaurantPricing;
    private final String restaurantOpenOrClose;
    private boolean isRestaurantFavourite;

    public RestaurantItem(String restaurantId, String restaurantImage, String restaurantName, String restaurantAddress, String restaurantCuisine, float restaurantRating, int restaurantPricing, String restaurantOpenOrClose, boolean isRestaurantFavourite) {
        this.restaurantId = restaurantId;
        this.restaurantImage = restaurantImage;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantCuisine = restaurantCuisine;
        this.restaurantRating = restaurantRating;
        this.restaurantPricing = restaurantPricing;
        this.restaurantOpenOrClose = restaurantOpenOrClose;
        this.isRestaurantFavourite = isRestaurantFavourite;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getRestaurantCuisine() {
        return restaurantCuisine;
    }

    public float getRestaurantRating() {
        return restaurantRating;
    }

    public int getRestaurantPricing() {
        return restaurantPricing;
    }

    public String getRestaurantOpenOrClose() {
        return restaurantOpenOrClose;
    }

    public boolean isRestaurantFavourite() {
        return isRestaurantFavourite;
    }

    public void setRestaurantFavourite(boolean restaurantFavourite) {
        isRestaurantFavourite = restaurantFavourite;
    }


}
