package com.example.mealcompass.Discover;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;


public class DiscoverViewModel extends ViewModel {
    private MutableLiveData<List<Discover>> discoverListLiveData = new MutableLiveData<>();
    private DiscoverRepository discoverRepository;

    public DiscoverViewModel() {
        discoverRepository = new DiscoverRepository();
    }

    // LiveData getter
    public LiveData<List<Discover>> getDiscoverListLiveData() {
        return discoverListLiveData;
    }

    // Fetch all discover articles
    public void fetchAllDiscover() {
        discoverRepository.getAllDiscover(new DiscoverRepository.DiscoverListCallback() {
            @Override
            public void onSuccess(List<Discover> discoverList) {
                // Set the data to the LiveData object
                discoverListLiveData.setValue(discoverList);
            }

            @Override
            public void onFailure(Exception e) {
                // Handle the error appropriately
                Log.e("DiscoverViewModel", "Error fetching discover articles", e);
            }
        });
    }
}


