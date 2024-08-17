package com.example.mealcompass.Admin;

public class RestaurantRequestsItem {
    private final int restaurantImage;
    private final String restaurantName;
    private final String restaurantCuisine;


    public RestaurantRequestsItem(int restaurantImage, String restaurantName, String restaurantCuisine) {
        this.restaurantImage = restaurantImage;
        this.restaurantName = restaurantName;
        this.restaurantCuisine = restaurantCuisine;
    }

    public int getRestaurantImage() {
        return restaurantImage;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantCuisine() {
        return restaurantCuisine;
    }



}
