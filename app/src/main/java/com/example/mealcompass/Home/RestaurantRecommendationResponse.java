package com.example.mealcompass.Home;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantRecommendationResponse {
    private String Restaurant;
    private double Score;

    @SerializedName("Top Menu Items")
    private List<MenuItemRecommendation> TopMenuItems;

    // Add getters and setters
    public String getRestaurant() {
        return Restaurant;
    }

    public void setRestaurant(String restaurant) {
        Restaurant = restaurant;
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }

    public List<MenuItemRecommendation> getTopMenuItems() {
        return TopMenuItems;
    }

    public void setTopMenuItems(List<MenuItemRecommendation> topMenuItems) {
        TopMenuItems = topMenuItems;
    }
}

