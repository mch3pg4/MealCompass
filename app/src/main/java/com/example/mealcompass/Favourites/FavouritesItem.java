package com.example.mealcompass.Favourites;

public class FavouritesItem {
    private final int restaurantImage;
    private final String restaurantName, restaurantAddress, restaurantCuisine, restaurantRating, restaurantPrice, restaurantOpenOrClose;
    private boolean isRestaurantFavourite;

    public FavouritesItem(int restaurantImage, boolean isRestaurantFavourite, String restaurantName, String restaurantAddress, String restaurantCuisine, String restaurantRating, String restaurantPrice, String restaurantOpenOrClose) {
        this.isRestaurantFavourite = isRestaurantFavourite;
        this.restaurantImage = restaurantImage;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantCuisine = restaurantCuisine;
        this.restaurantRating = restaurantRating;
        this.restaurantPrice = restaurantPrice;
        this.restaurantOpenOrClose = restaurantOpenOrClose;
    }

    public boolean isRestaurantFavourite() {
        return isRestaurantFavourite;
    }

    public void setRestaurantFavourite(boolean restaurantFavourite) {
        isRestaurantFavourite = restaurantFavourite;
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





}
