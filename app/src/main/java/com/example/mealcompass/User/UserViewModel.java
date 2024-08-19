package com.example.mealcompass.User;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<String> userId = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> userEmail = new MutableLiveData<>();
    private final MutableLiveData<String> userType = new MutableLiveData<>();
    private final MutableLiveData<String> userPassword = new MutableLiveData<>();
    private final MutableLiveData<String> userImageUrl = new MutableLiveData<>();
    private final MutableLiveData<List<User>> userAllergens = new MutableLiveData<>();
    private final MutableLiveData<List<User>> userCuisines = new MutableLiveData<>();
    private final MutableLiveData<List<User>> userDiets = new MutableLiveData<>();
    private final MutableLiveData<List<User>> favouriteRestaurants = new MutableLiveData<>();

    public LiveData<String> getUserId() {
        return userId;
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }

    public LiveData<String> getUserType() {
        return userType;
    }

    public LiveData<String> getUserPassword() {
        return userPassword;
    }

    public LiveData<String> getUserImageUrl() {
        return userImageUrl;
    }

    public LiveData<List<User>> getUserAllergens() {
        return userAllergens;
    }

    public LiveData<List<User>> getUserCuisines() {
        return userCuisines;
    }

    public LiveData<List<User>> getUserDiets() {
        return userDiets;
    }

    public LiveData<List<User>> getFavouriteRestaurants() {
        return favouriteRestaurants;
    }

    public void setUserId(String userId) {
        this.userId.setValue(userId);
    }

    public void setUserName(String userName) {
        this.userName.setValue(userName);
    }

    public void setUserEmail(String userEmail) {
        this.userEmail.setValue(userEmail);
    }

    public void setUserType(String userType) {
        this.userType.setValue(userType);
    }

    public void setUserPassword(String userPassword) {
        this.userPassword.setValue(userPassword);
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl.setValue(userImageUrl);
    }

    public void setUserAllergens(List<User> userAllergens) {
        this.userAllergens.setValue(userAllergens);
    }

    public void setUserCuisines(List<User> userCuisines) {
        this.userCuisines.setValue(userCuisines);
    }

    public void setUserDiets(List<User> userDiets) {
        this.userDiets.setValue(userDiets);
    }

    public void setFavouriteRestaurants(List<User> favouriteRestaurants) {
        this.favouriteRestaurants.setValue(favouriteRestaurants);
    }

    public void addFavouriteRestaurant(User restaurant) {
        List<User> currentFavourites = favouriteRestaurants.getValue();
        currentFavourites.add(restaurant);
        favouriteRestaurants.setValue(currentFavourites);
    }

    public void removeFavouriteRestaurant(User restaurant) {
        List<User> currentFavourites = favouriteRestaurants.getValue();
        currentFavourites.remove(restaurant);
        favouriteRestaurants.setValue(currentFavourites);
    }






}
