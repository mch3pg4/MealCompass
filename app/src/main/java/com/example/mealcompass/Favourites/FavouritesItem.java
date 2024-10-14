package com.example.mealcompass.Favourites;

public class FavouritesItem {
    private final String restaurantId;
    private final String restaurantImage;
    private final String restaurantName, restaurantAddress, restaurantCuisine,  restaurantOpenOrClose;
    public final int restaurantPrice;
    public final float restaurantRating;
    private boolean isRestaurantFavourite;

    public FavouritesItem(String restaurantId, String restaurantImage, boolean isRestaurantFavourite, String restaurantName, String restaurantAddress, String restaurantCuisine, float restaurantRating, int restaurantPrice, String restaurantOpenOrClose) {
        this.restaurantId = restaurantId;
        this.isRestaurantFavourite = isRestaurantFavourite;
        this.restaurantImage = restaurantImage;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantCuisine = restaurantCuisine;
        this.restaurantRating = restaurantRating;
        this.restaurantPrice = restaurantPrice;
        this.restaurantOpenOrClose = restaurantOpenOrClose;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public boolean isRestaurantFavourite() {
        return isRestaurantFavourite;
    }

    public void setRestaurantFavourite(boolean restaurantFavourite) {
        isRestaurantFavourite = restaurantFavourite;
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

    public int getRestaurantPrice() {
        return restaurantPrice;
    }

    public String getRestaurantOpenOrClose() {
        return restaurantOpenOrClose;
    }





}
