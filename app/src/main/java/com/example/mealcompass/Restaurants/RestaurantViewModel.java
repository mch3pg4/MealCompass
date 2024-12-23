package com.example.mealcompass.Restaurants;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.mealcompass.MenuItem.MenuItem;

import java.util.List;

public class RestaurantViewModel extends ViewModel {
   private final MutableLiveData<String> restaurantId = new MutableLiveData<>();
    private final MutableLiveData<String> restaurantName = new MutableLiveData<>();
    private final MutableLiveData<String> restaurantAddress = new MutableLiveData<>();
    private final MutableLiveData<String> restaurantImageUrl = new MutableLiveData<>();
    private final MutableLiveData<Float> restaurantRating = new MutableLiveData<>();
    private final MutableLiveData<Integer> restaurantPricing = new MutableLiveData<>();
    private final MutableLiveData<String> restaurantStatus = new MutableLiveData<>();
    private final MutableLiveData<String> restaurantBusinessHours = new MutableLiveData<>();
    private final MutableLiveData<String> restaurantCuisine = new MutableLiveData<>();
    private final MutableLiveData<String> restaurantContact = new MutableLiveData<>();
    private final MutableLiveData<String> isHalal = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurant>> restaurantListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Restaurant>> restaurantRequestsListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MenuItem>> menuItemsLiveData = new MutableLiveData<>();
    private final RestaurantRepository restaurantRepository;

    public RestaurantViewModel() {
         restaurantRepository = new RestaurantRepository();
    }

    public LiveData<String> getRestaurantId() {
        return restaurantId;
    }

    public LiveData<String> getRestaurantName() {
        return restaurantName;
    }

    public LiveData<String> getRestaurantAddress() {
        return restaurantAddress;
    }

    public LiveData<String> getRestaurantImageUrl() {
        return restaurantImageUrl;
    }

    public LiveData<Float> getRestaurantRating() {
        return restaurantRating;
    }

    public LiveData<Integer> getRestaurantPricing() {
        return restaurantPricing;
    }

    public LiveData<String> getRestaurantStatus() {
        return restaurantStatus;
    }

    public LiveData<String> getRestaurantBusinessHours() {
        return restaurantBusinessHours;
    }

    public LiveData<String> getRestaurantCuisine() {
        return restaurantCuisine;
    }

    public LiveData<String> getRestaurantContact() {
        return restaurantContact;
    }

    public LiveData<String> getIsHalal() {
        return isHalal;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId.setValue(restaurantId);
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName.setValue(restaurantName);
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress.setValue(restaurantAddress);
    }

    public void setRestaurantImageUrl(String restaurantImageUrl) {
        this.restaurantImageUrl.setValue(restaurantImageUrl);
    }

    public void setRestaurantRating(Float restaurantRating) {
        this.restaurantRating.setValue(restaurantRating);
    }

    public void setRestaurantPricing(Integer restaurantPricing) {
        this.restaurantPricing.setValue(restaurantPricing);
    }

    public void setRestaurantStatus(String restaurantStatus) {
        this.restaurantStatus.setValue(restaurantStatus);
    }

    public void setRestaurantBusinessHours(String restaurantBusinessHours) {
        this.restaurantBusinessHours.setValue(restaurantBusinessHours);
    }

    public void setRestaurantCuisine(String restaurantCuisine) {
        this.restaurantCuisine.setValue(restaurantCuisine);
    }

    public void setIsHalal(String isHalal) {
        this.isHalal.setValue(isHalal);
    }

    public void setRestaurantContact(String restaurantContact) {
        this.restaurantContact.setValue(restaurantContact);
    }


    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantListLiveData;
    }

    public LiveData<List<com.example.mealcompass.MenuItem.MenuItem>> getMenuItemsLiveData() {
        return menuItemsLiveData;
    }

    public LiveData<List<Restaurant>> getRestaurantRequestsListLiveData() {
        return restaurantRequestsListLiveData;
    }

    public void fetchAllRestaurants() {
        restaurantRepository.getAllRestaurants(new RestaurantRepository.RestaurantListCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurantList) {
                restaurantListLiveData.setValue(restaurantList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error fetching restaurants", e);
            }
        });
    }

    // get restaurant by owner id
    public void fetchRestaurantByOwnerId(String ownerId) {
        restaurantRepository.getRestaurantByOwnerId(ownerId, new RestaurantRepository.RestaurantListCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurantList) {
                restaurantListLiveData.setValue(restaurantList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error fetching restaurant", e);
            }
        });
    }

    // get restaurant by name
    public void fetchRestaurantByName(String restaurantName) {
        restaurantRepository.getRestaurantByName(restaurantName, new RestaurantRepository.RestaurantListCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurantList) {
                restaurantListLiveData.setValue(restaurantList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error fetching restaurant", e);
            }
        });
    }

    // get restaurant by status
    public void fetchRestaurantByPendingStatus() {
        restaurantRepository.getPendingRestaurants(new RestaurantRepository.RestaurantListCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurantList) {
                restaurantRequestsListLiveData.setValue(restaurantList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error fetching restaurant", e);
            }
        });
    }

    // get menu item by name
    public void fetchMenuItemByName(String restaurantId, String menuItemName) {
        restaurantRepository.getMenuItemByName(restaurantId, menuItemName, new RestaurantRepository.RestaurantMenuCallback() {
            @Override
            public void onSuccess(List<MenuItem> menuItemList) {
                menuItemsLiveData.setValue(menuItemList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error fetching menu item", e);
            }
        });
    }

    // get restaurant menu by id
    public void fetchRestaurantMenu(String restaurantId) {
        restaurantRepository.getRestaurantMenu(restaurantId, new RestaurantRepository.RestaurantMenuCallback() {
            @Override
            public void onSuccess(List<com.example.mealcompass.MenuItem.MenuItem> menuItems) {
                menuItemsLiveData.setValue(menuItems);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error fetching menu", e);
            }
        });
    }





    public void searchRestaurants(String query) {
        restaurantRepository.searchRestaurants(query, new RestaurantRepository.RestaurantListCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurantList) {
                restaurantListLiveData.setValue(restaurantList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error searching restaurants", e);
            }
        });
    }

    // sort restaurants by open now
    public void sortRestaurantsByOpenNow() {
        restaurantRepository.sortRestaurantsByOpenNow(new RestaurantRepository.RestaurantListCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurantList) {
                restaurantListLiveData.setValue(restaurantList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error sorting restaurants", e);
            }
        });
    }

    public void sortRestaurantsByPricing(String order) {
        restaurantRepository.sortRestaurantsByPricing(order, new RestaurantRepository.RestaurantListCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurantList) {
                restaurantListLiveData.setValue(restaurantList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error sorting restaurants", e);
            }
        });
    }

    // sort restaurants by rating
    public void sortRestaurantsByRating(String order) {
        restaurantRepository.sortRestaurantsByRating(order, new RestaurantRepository.RestaurantListCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurantList) {
                restaurantListLiveData.setValue(restaurantList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error sorting restaurants", e);
            }
        });
    }

    // sort restaurants by distance
    public void sortRestaurantsByDistance(double userLatitude, double userLongitude, Context context) {
        restaurantRepository.sortRestaurantsByDistance(userLatitude, userLongitude, context, new RestaurantRepository.RestaurantListCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurantList) {
                restaurantListLiveData.setValue(restaurantList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("RestaurantViewModel", "Error sorting restaurants", e);
            }
        });
    }



}
