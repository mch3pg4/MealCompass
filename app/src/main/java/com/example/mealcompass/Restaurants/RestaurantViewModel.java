package com.example.mealcompass.Restaurants;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.List;

public class RestaurantViewModel extends ViewModel {
   private final MutableLiveData<List<Restaurant>> restaurantListLiveData = new MutableLiveData<>();
   private final RestaurantRepository restaurantRepository;

    public RestaurantViewModel() {
         restaurantRepository = new RestaurantRepository();
    }

    public LiveData<List<Restaurant>> getRestaurantListLiveData() {
        return restaurantListLiveData;
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


}
