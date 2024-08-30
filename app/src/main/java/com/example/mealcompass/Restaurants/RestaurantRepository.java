package com.example.mealcompass.Restaurants;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantRepository {
private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //add restaurant
    public void addRestaurant(String name, String address, String imageUrl, float rating, int price, String status, String businessHours, String cuisine, String contact) {
        Map<String, Object> restaurant = new HashMap<>();
        restaurant.put("name", name);
        restaurant.put("address", address);
        restaurant.put("imageUrl", imageUrl);
        restaurant.put("rating", rating);
        restaurant.put("price", price);
        restaurant.put("status", status);
        restaurant.put("businessHours", businessHours);
        restaurant.put("cuisine", cuisine);
        restaurant.put("contact", contact);

        db.collection("restaurant")
                .add(restaurant)
                .addOnSuccessListener(documentReference ->
                        Log.d("RestaurantRepository", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.d("RestaurantRepository", "Error adding document", e));
    }

    // update restaurant image
    public void updateRestaurantImage(String documentId, String imageUrl) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("imageUrl", imageUrl);

        db.collection("restaurant").document(documentId)
                .update(updates)
                .addOnSuccessListener(aVoid ->
                        Log.d("RestaurantRepository", "Document updated successfully"))
                .addOnFailureListener(e ->
                        Log.d("RestaurantRepository", "Error updating document", e));
    }


    // update restaurant
    public void updateRestaurant(String documentId, String name, String address, String imageUrl, float rating, int price, String status, String businessHours, String cuisine, String contact) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("address", address);
        updates.put("imageUrl", imageUrl);
        updates.put("rating", rating);
        updates.put("price", price);
        updates.put("status", status);
        updates.put("businessHours", businessHours);
        updates.put("cuisine", cuisine);
        updates.put("contact", contact);

        db.collection("restaurant").document(documentId)
                .update(updates)
                .addOnSuccessListener(aVoid ->
                        Log.d("RestaurantRepository", "Document updated successfully"))
                .addOnFailureListener(e ->
                        Log.d("RestaurantRepository", "Error updating document", e));
    }

    //delete restaurant
    public void deleteRestaurant(String documentId) {
        db.collection("restaurant").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid ->
                        Log.d("RestaurantRepository", "Document deleted successfully"))
                .addOnFailureListener(e ->
                        Log.d("RestaurantRepository", "Error deleting document", e));
    }

    // restaurant list callback
    public interface RestaurantListCallback {
        void onSuccess(List<Restaurant> restaurantList);
        void onFailure(Exception e);
    }

    // get all restaurants
    public void getAllRestaurants(RestaurantListCallback callback) {
        db.collection("restaurant")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Restaurant restaurant = document.toObject(Restaurant.class);
                            if (restaurant != null) {
                                restaurantList.add(restaurant);

                            }
                        }
                        callback.onSuccess(restaurantList);
                    } else {
                        callback.onFailure(task.getException());

                    }
                });
    }
}
