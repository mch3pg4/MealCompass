package com.example.mealcompass.Home;

public class RecommendHistoryItem {
    private final int restaurantImage;
    private final String restaurantName, restaurantRating;

    public RecommendHistoryItem(int restaurantImage, String restaurantName, String restaurantRating) {
        this.restaurantImage = restaurantImage;
        this.restaurantName = restaurantName;
        this.restaurantRating = restaurantRating;
    }

    public int getRestaurantImage() {
        return restaurantImage;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantRating() {
        return restaurantRating;
    }
}
