package com.example.mealcompass.Restaurants;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantRepository {
private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //add restaurant
    public Task<DocumentReference> addRestaurant(String name, String businessHours, String cuisine, String contact) {
        Map<String, Object> restaurant = new HashMap<>();
        restaurant.put("name", name);
        restaurant.put("address", "");
        restaurant.put("imageUrl", "");
        restaurant.put("rating", 0);
        restaurant.put("price", 0);
        restaurant.put("status", "Pending");
        restaurant.put("businessHours", businessHours);
        restaurant.put("cuisine", cuisine);
        restaurant.put("contact", contact);

        return db.collection("restaurant")
                .add(restaurant)
                .addOnSuccessListener(documentReference ->
                        Log.d("RestaurantRepository", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.d("RestaurantRepository", "Error adding document", e));
    }

    // get restaurant by id
    public Task<DocumentSnapshot> getRestaurantById(String documentId) {
        return db.collection("restaurant").document(documentId).get();
    }

    // update restaurant address
    public Task<Void> updateRestaurantAddress(String documentId, String address) {
        return db.collection("restaurant").document(documentId)
                .update("address", address);
    }

    // update restaurant image
    public Task<Void> updateRestaurantImage(String documentId, String imageUrl) {
        return db.collection("restaurant").document(documentId)
                .update("imageUrl", imageUrl);
    }

    // add menu item
    public Task<DocumentReference> addMenuItem(String restaurantId, String name, String description, String imageUrl, float price, int nutritionalValue, String category, List<String> allergen) {
        Map<String, Object> menuItem = new HashMap<>();
        menuItem.put("name", name);
        menuItem.put("description", description);
        menuItem.put("imageUrl", imageUrl);
        menuItem.put("price", price);
        menuItem.put("nutritionalValue", nutritionalValue);
        menuItem.put("category", category);
        menuItem.put("allergens", allergen);


        return db.collection("restaurant")
                .document(restaurantId)
                .collection("menuItem")
                .add(menuItem);
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
