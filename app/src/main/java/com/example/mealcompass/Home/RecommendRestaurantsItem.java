package com.example.mealcompass.Home;

public class RecommendRestaurantsItem {
    private final String restaurantId;
    private final String restaurantImage;
    private final String restaurantName, restaurantAddress, restaurantCuisine, restaurantContact, restaurantBusinessHours;
    private final float restaurantRating;

    public RecommendRestaurantsItem(String restaurantId, String restaurantImage, String restaurantName, String restaurantAddress, String restaurantCuisine, String restaurantContact, String restaurantBusinessHours, float restaurantRating) {
        this.restaurantId = restaurantId;
        this.restaurantImage = restaurantImage;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantCuisine = restaurantCuisine;
        this.restaurantContact = restaurantContact;
        this.restaurantBusinessHours = restaurantBusinessHours;
        this.restaurantRating = restaurantRating;
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

    public float getRestaurantRating() {
        return restaurantRating;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getRestaurantCuisine() {
        return restaurantCuisine;
    }

    public String getRestaurantContact() {
        return restaurantContact;
    }

    public String getRestaurantBusinessHours() {
        return restaurantBusinessHours;
    }
}
