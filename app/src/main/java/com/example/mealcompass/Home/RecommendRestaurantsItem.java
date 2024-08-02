package com.example.mealcompass.Home;

public class RecommendRestaurantsItem {
    private final int restaurantImage;
    private final String restaurantName, restaurantRating;

    public RecommendRestaurantsItem(int restaurantImage, String restaurantName, String restaurantRating) {
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
