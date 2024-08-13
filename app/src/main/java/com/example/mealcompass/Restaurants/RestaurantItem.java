package com.example.mealcompass.Restaurants;

public class RestaurantItem {
    private final int restaurantImage;
    private final String restaurantName;
    private final String restaurantAddress;
    private final String restaurantCuisine;
    private final String restaurantRating;
    private final String restaurantPrice;
    private final String restaurantOpenOrClose;
    private boolean isRestaurantFavourite;

    public RestaurantItem(int restaurantImage, String restaurantName, String restaurantAddress, String restaurantCuisine, String restaurantRating, String restaurantPrice, String restaurantOpenOrClose, boolean isRestaurantFavourite) {
        this.restaurantImage = restaurantImage;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantCuisine = restaurantCuisine;
        this.restaurantRating = restaurantRating;
        this.restaurantPrice = restaurantPrice;
        this.restaurantOpenOrClose = restaurantOpenOrClose;
        this.isRestaurantFavourite = isRestaurantFavourite;
    }

    public int getRestaurantImage() {
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

    public String getRestaurantRating() {
        return restaurantRating;
    }

    public String getRestaurantPrice() {
        return restaurantPrice;
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
