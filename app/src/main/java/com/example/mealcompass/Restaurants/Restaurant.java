package com.example.mealcompass.Restaurants;

public class Restaurant {
    private  String restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantImageUrl;
    private float restaurantRating;
    private int restaurantPricing;
    private String restaurantStatus;
    private String restaurantBusinessHours;
    private String restaurantCuisine;
    private String restaurantContact;

    public Restaurant() {
    }

    public Restaurant(String restaurantId, String restaurantName, String restaurantAddress, String restaurantImageUrl, float restaurantRating, int restaurantPricing, String restaurantStatus, String restaurantBusinessHours, String restaurantCuisine, String restaurantContact) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantImageUrl = restaurantImageUrl;
        this.restaurantRating = restaurantRating;
        this.restaurantPricing = restaurantPricing;
        this.restaurantStatus = restaurantStatus;
        this.restaurantBusinessHours = restaurantBusinessHours;
        this.restaurantCuisine = restaurantCuisine;
        this.restaurantContact = restaurantContact;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getRestaurantImageUrl() {
        return restaurantImageUrl;
    }

    public float getRestaurantRating() {
        return restaurantRating;
    }

    public int getRestaurantPricing() {
        return restaurantPricing;
    }

    public String getRestaurantStatus() {
        return restaurantStatus;
    }

    public String getRestaurantBusinessHours() {
        return restaurantBusinessHours;
    }

    public String getRestaurantCuisine() {
        return restaurantCuisine;
    }

    public String getRestaurantContact() {
        return restaurantContact;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public void setRestaurantImageUrl(String restaurantImageUrl) {
        this.restaurantImageUrl = restaurantImageUrl;
    }

    public void setRestaurantRating(float restaurantRating) {
        this.restaurantRating = restaurantRating;
    }

    public void setRestaurantPricing(int restaurantPricing) {
        this.restaurantPricing = restaurantPricing;
    }

    public void setRestaurantStatus(String restaurantStatus) {
        this.restaurantStatus = restaurantStatus;
    }

    public void setRestaurantBusinessHours(String restaurantBusinessHours) {
        this.restaurantBusinessHours = restaurantBusinessHours;
    }

    public void setRestaurantCuisine(String restaurantCuisine) {
        this.restaurantCuisine = restaurantCuisine;
    }

    public void setRestaurantContact(String restaurantContact) {
        this.restaurantContact = restaurantContact;
    }



}
