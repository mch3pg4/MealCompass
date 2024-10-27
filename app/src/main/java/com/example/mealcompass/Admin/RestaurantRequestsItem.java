package com.example.mealcompass.Admin;

public class RestaurantRequestsItem {
    private final String restaurantId;
    private final String restaurantImage;
    private final String restaurantName;
    private final String restaurantCuisine;


    public RestaurantRequestsItem(String restaurantId, String restaurantImage, String restaurantName, String restaurantCuisine) {
        this.restaurantId = restaurantId;
        this.restaurantImage = restaurantImage;
        this.restaurantName = restaurantName;
        this.restaurantCuisine = restaurantCuisine;
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

    public String getRestaurantCuisine() {
        return restaurantCuisine;
    }



}
