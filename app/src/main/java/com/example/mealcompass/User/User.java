package com.example.mealcompass.User;

import java.util.List;

public class User {
    private String userName;
    private String userEmail;
    private String userType;
    private String userImageUrl;
    private List <String> userAllergens;
    private List <String> userCuisines;
    private List <String> userDiets;
    private List<String> favouriteRestaurants;
    private List<String> recommendedHistory;

    public User() {
    }

    public User(String userName, String userEmail, String userType, String userImageUrl, List<String> userAllergens, List<String> userCuisines, List<String> userDiets, List<String> favouriteRestaurants, List<String> recommendedHistory) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userType = userType;
        this.userImageUrl = userImageUrl;
        this.userAllergens = userAllergens;
        this.userCuisines = userCuisines;
        this.userDiets = userDiets;
        this.favouriteRestaurants = favouriteRestaurants;
        this.recommendedHistory = recommendedHistory;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public List<String> getUserAllergens() {
        return userAllergens;
    }

    public List<String> getUserCuisines() {
        return userCuisines;
    }

    public List<String> getUserDiets() {
        return userDiets;
    }

    public List<String> getFavouriteRestaurants() {
        return favouriteRestaurants;
    }

    public List<String> getRecommendedHistory() {
        return recommendedHistory;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public void setUserAllergens(List<String> userAllergens) {
        this.userAllergens = userAllergens;
    }

    public void setUserCuisines(List<String> userCuisines) {
        this.userCuisines = userCuisines;
    }

    public void setUserDiets(List<String> userDiets) {
        this.userDiets = userDiets;
    }

    public void setFavouriteRestaurants(List<String> favouriteRestaurants) {
        this.favouriteRestaurants = favouriteRestaurants;
    }

    public void setRecommendedHistory(List<String> recommendedHistory) {
        this.recommendedHistory = recommendedHistory;
    }


}