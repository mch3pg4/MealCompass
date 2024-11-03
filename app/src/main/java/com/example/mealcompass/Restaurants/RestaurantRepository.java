package com.example.mealcompass.Restaurants;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.mealcompass.MenuItem.MenuItem;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RestaurantRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //add restaurant
    public Task<DocumentReference> addRestaurant(String name, String businessHours, String cuisine, String contact, int pricing, float rating, String isHalal) {
        Map<String, Object> restaurant = new HashMap<>();
        restaurant.put("restaurantName", name);
        restaurant.put("restaurantAddress", "");
        restaurant.put("restaurantImageUrl", "");
        restaurant.put("restaurantRating", rating);
        restaurant.put("restaurantPricing", pricing);
        restaurant.put("restaurantStatus", "Pending");
        restaurant.put("restaurantBusinessHours", businessHours);
        restaurant.put("restaurantCuisine", cuisine);
        restaurant.put("restaurantContact", contact);
        restaurant.put("isHalal", isHalal);

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
                .update("restaurantAddress", address);
    }

    // update restaurant image
    public Task<Void> updateRestaurantImage(String documentId, String imageUrl) {
        return db.collection("restaurant").document(documentId)
                .update("restaurantImageUrl", imageUrl);
    }

    // add menu item
    public Task<DocumentReference> addMenuItem(String restaurantId, String name, String description, String imageUrl, float price, int nutritionalValue, String category, List<String> allergen) {
        Map<String, Object> menuItem = new HashMap<>();
        menuItem.put("itemName", name);
        menuItem.put("itemDescription", description);
        menuItem.put("itemImageUrl", imageUrl);
        menuItem.put("itemPrice", price);
        menuItem.put("itemNutritionalValue", nutritionalValue);
        menuItem.put("itemCategory", category);
        menuItem.put("itemAllergens", allergen);

        return db.collection("restaurant")
                .document(restaurantId)
                .collection("menuItems")
                .add(menuItem);
    }

    // update menu item
    public Task<Void> updateMenuItem(String restaurantId, String menuItemId, String name, String description, String imageUrl, float price, int nutritionalValue, String category, List<String> allergen) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("itemName", name);
        updates.put("itemDescription", description);
        updates.put("itemImageUrl", imageUrl);
        updates.put("itemPrice", price);
        updates.put("itemNutritionalValue", nutritionalValue);
        updates.put("itemCategory", category);
        updates.put("itemAllergens", allergen);

        return db.collection("restaurant")
                .document(restaurantId)
                .collection("menuItems")
                .document(menuItemId)
                .update(updates)
                .addOnSuccessListener(aVoid ->
                        Log.d("RestaurantRepository", "Document updated successfully"))
                .addOnFailureListener(e ->
                        Log.d("RestaurantRepository", "Error updating document", e));
    }

    // delete menu item
    public Task<Void> deleteMenuItem(String restaurantId, String menuItemId) {
        return db.collection("restaurant")
                .document(restaurantId)
                .collection("menuItems")
                .document(menuItemId)
                .delete()
                .addOnSuccessListener(aVoid ->
                        Log.d("RestaurantRepository", "Document deleted successfully"))
                .addOnFailureListener(e ->
                        Log.d("RestaurantRepository", "Error deleting document", e));
    }

    // update restaurant
    public Task<Void> updateRestaurant(String documentId, String name, String address, String imageUrl, float rating, int price, String status, String businessHours, String cuisine, String contact, String isHalal) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("restaurantName", name);
        updates.put("restaurantAddress", address);
        updates.put("restaurantImageUrl", imageUrl);
        updates.put("restaurantRating", rating);
        updates.put("restaurantPricing", price);
        updates.put("restaurantStatus", status);
        updates.put("restaurantBusinessHours", businessHours);
        updates.put("restaurantCuisine", cuisine);
        updates.put("restaurantContact", contact);
        updates.put("isHalal", isHalal);

        return db.collection("restaurant").document(documentId)
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

    // update restaurant status
    public Task<Void> updateRestaurantStatus(String restaurantId, String status) {
        return db.collection("restaurant")
                .document(restaurantId)
                .update("restaurantStatus", status)
                .addOnSuccessListener(aVoid ->
                        Log.d("RestaurantRepository", "Document updated successfully"))
                .addOnFailureListener(e ->
                        Log.d("RestaurantRepository", "Error updating document", e));
    }



    // restaurant list callback
    public interface RestaurantListCallback {
        void onSuccess(List<Restaurant> restaurantList);
        void onFailure(Exception e);
    }

    // restaurant menu callback
    public interface RestaurantMenuCallback {
        void onSuccess(List<MenuItem> menuItems);
        void onFailure(Exception e);
    }

    // get restaurant by owner id
public void getRestaurantByOwnerId(String ownerId, RestaurantListCallback callback) {
    db.collection("users")
            .document(ownerId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> restaurantIds = (List<String>) document.get("ownerRestaurants");
                        if (restaurantIds != null && !restaurantIds.isEmpty()) {
                            List<Restaurant> restaurantList = new ArrayList<>();
                            for (String restaurantId : restaurantIds) {
                                db.collection("restaurant")
                                        .document(restaurantId)
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                DocumentSnapshot document1 = task1.getResult();
                                                Restaurant restaurant = document1.toObject(Restaurant.class);
                                                if (restaurant != null) {
                                                    restaurant.setRestaurantId(document1.getId());
                                                    restaurant.setRestaurantName(document1.getString("restaurantName"));
                                                    restaurant.setRestaurantImageUrl(document1.getString("restaurantImageUrl"));
                                                    restaurant.setRestaurantContact(document1.getString("restaurantContact"));
                                                    restaurant.setRestaurantBusinessHours(document1.getString("restaurantBusinessHours"));
                                                    restaurant.setRestaurantCuisine(document1.getString("restaurantCuisine"));
                                                    restaurant.setRestaurantPricing(Objects.requireNonNull(document1.getDouble("restaurantPricing")).intValue());
                                                    restaurant.setRestaurantRating(Objects.requireNonNull(document1.getDouble("restaurantRating")).floatValue());
                                                    restaurant.setRestaurantAddress(document1.getString("restaurantAddress"));
                                                    restaurant.setRestaurantStatus(document1.getString("restaurantStatus"));
                                                    restaurantList.add(restaurant);
                                                }
                                            } else {
                                                callback.onFailure(task1.getException());
                                            }
                                            if (restaurantList.size() == restaurantIds.size()) {
                                                callback.onSuccess(restaurantList);
                                            }
                                        });
                            }
                        } else {
                            callback.onSuccess(new ArrayList<>());
                        }
                    } else {
                        callback.onFailure(new Exception("User document does not exist"));
                    }
                } else {
                    callback.onFailure(task.getException());
                }
            });
}

    // get restaurant by name
    public void getRestaurantByName(String restaurantName, RestaurantListCallback callback) {
        db.collection("restaurant")
                .whereEqualTo("restaurantName", restaurantName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Restaurant restaurant = document.toObject(Restaurant.class);
                            if (restaurant != null) {
                                restaurantList.add(restaurant);
                                restaurant.setRestaurantId(document.getId());
                                restaurant.setRestaurantName(document.getString("restaurantName"));
                                restaurant.setRestaurantImageUrl(document.getString("restaurantImageUrl"));
                                restaurant.setRestaurantContact(document.getString("restaurantContact"));
                                restaurant.setRestaurantBusinessHours(document.getString("restaurantBusinessHours"));
                                restaurant.setRestaurantCuisine(document.getString("restaurantCuisine"));
                                restaurant.setRestaurantPricing(Objects.requireNonNull(document.getDouble("restaurantPricing")).intValue());
                                restaurant.setRestaurantRating(Objects.requireNonNull(document.getDouble("restaurantRating")).floatValue());
                                restaurant.setRestaurantAddress(document.getString("restaurantAddress"));
                                restaurant.setRestaurantStatus(document.getString("restaurantStatus"));
                            }
                        }
                        callback.onSuccess(restaurantList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // get menu item from name and restaurantId
    public void getMenuItemByName(String restaurantId, String menuItemName, RestaurantMenuCallback callback) {
        db.collection("restaurant")
                .document(restaurantId)
                .collection("menuItems")
                .whereEqualTo("itemName", menuItemName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<MenuItem> menuItems = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            MenuItem menuItem = document.toObject(MenuItem.class);
                            if (menuItem != null) {
                                menuItems.add(menuItem);
                                menuItem.setMenuItemId(document.getId());
                                menuItem.setMenuItemCategory(document.getString("itemCategory"));
                                menuItem.setMenuItemDescription(document.getString("itemDescription"));
                                menuItem.setMenuItemImage(document.getString("itemImageUrl"));
                                menuItem.setMenuItemName(document.getString("itemName"));
                                menuItem.setMenuItemPrice(Objects.requireNonNull(document.getDouble("itemPrice")).intValue());
                                menuItem.setMenuItemNutritionalValue(Objects.requireNonNull(document.getDouble("itemNutritionalValue")).intValue());
                                menuItem.setMenuItemAllergens((List<String>) document.get("itemAllergens"));
                            }
                        }
                        callback.onSuccess(menuItems);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }


    // get restaurant menu from restaurant id
    public void getRestaurantMenu(String restaurantId, RestaurantMenuCallback callback) {
        db.collection("restaurant")
                .document(restaurantId)
                .collection("menuItems")
                .orderBy("itemCategory", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<MenuItem> menuItems = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            MenuItem menuItem = document.toObject(MenuItem.class);
                            if (menuItem != null) {
                                menuItems.add(menuItem);
                                menuItem.setMenuItemId(document.getId());
                                menuItem.setMenuItemCategory(document.getString("itemCategory"));
                                menuItem.setMenuItemDescription(document.getString("itemDescription"));
                                menuItem.setMenuItemImage(document.getString("itemImageUrl"));
                                menuItem.setMenuItemName(document.getString("itemName"));
                                menuItem.setMenuItemPrice(Objects.requireNonNull(document.getDouble("itemPrice")).intValue());
                                menuItem.setMenuItemNutritionalValue(Objects.requireNonNull(document.getDouble("itemNutritionalValue")).intValue());
                                menuItem.setMenuItemAllergens((List<String>) document.get("itemAllergens"));
                            }
                        }
                        callback.onSuccess(menuItems);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
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
                                restaurant.setRestaurantId(document.getId());
                            }
                        }
                        callback.onSuccess(restaurantList);
                    } else {
                        callback.onFailure(task.getException());

                    }
                });
    }

    // get all restaurants by "Pending" status
    public void getPendingRestaurants(RestaurantListCallback callback) {
        db.collection("restaurant")
                .whereEqualTo("restaurantStatus", "Pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Restaurant restaurant = document.toObject(Restaurant.class);
                            if (restaurant != null) {
                                restaurantList.add(restaurant);
                                restaurant.setRestaurantId(document.getId());
                            }
                        }
                        callback.onSuccess(restaurantList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // search restaurants
    public void searchRestaurants(String query, RestaurantListCallback callback) {
        db.collection("restaurant")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Restaurant restaurant = document.toObject(Restaurant.class);
                            if (restaurant != null) {
                                if (restaurant.getRestaurantName().toLowerCase().contains(query.toLowerCase())) {
                                    restaurantList.add(restaurant);
                                    restaurant.setRestaurantId(document.getId());
                                }
                            }
                        }
                        callback.onSuccess(restaurantList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // sort restaurants by open now
    public void sortRestaurantsByOpenNow(RestaurantListCallback callback) {
        db.collection("restaurant")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Restaurant restaurant = document.toObject(Restaurant.class);
                            if (restaurant != null) {
                                // check if restaurant is open now
                                if (isRestaurantOpen(restaurant.getRestaurantBusinessHours())) {
                                    restaurantList.add(restaurant);
                                    restaurant.setRestaurantId(document.getId());
                                }
                            }
                            callback.onSuccess(restaurantList);
                        }
                    }
                        else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // sort restaurants by pricing (asc or desc)
    public void sortRestaurantsByPricing(String order,RestaurantListCallback callback) {
        db.collection("restaurant")
                .orderBy("restaurantPricing", order.equals("asc") ? Query.Direction.ASCENDING : Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Restaurant restaurant = document.toObject(Restaurant.class);
                            if (restaurant != null) {
                                restaurantList.add(restaurant);
                                restaurant.setRestaurantId(document.getId());
                            }
                        }
                        callback.onSuccess(restaurantList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // sort restaurants by rating (high or low)
    public void sortRestaurantsByRating(String order, RestaurantListCallback callback) {
        db.collection("restaurant")
                .orderBy("restaurantRating", order.equals("asc") ? Query.Direction.ASCENDING : Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Restaurant restaurant = document.toObject(Restaurant.class);
                            if (restaurant != null) {
                                restaurantList.add(restaurant);
                                restaurant.setRestaurantId(document.getId());
                            }
                        }
                        callback.onSuccess(restaurantList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // sort restaurants within 5km to user based on restaurant address
    public void sortRestaurantsByDistance(double userLat, double userLong, Context context, RestaurantListCallback callback) {
        db.collection("restaurant")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Restaurant restaurant = document.toObject(Restaurant.class);
                            if (restaurant != null) {
                                String restaurantAddress = restaurant.getRestaurantAddress();
                                try {
                                    Geocoder geocoder = new Geocoder(context);
                                    List<Address> addresses = geocoder.getFromLocationName(restaurantAddress, 1);
                                    assert addresses != null;
                                    if (addresses.isEmpty()) {
                                        continue;
                                    }
                                    Address address = addresses.get(0);
                                    double restaurantLat = address.getLatitude();
                                    double restaurantLong = address.getLongitude();

                                // Calculate distance using Haversine formula
                                double distance = calculateHaversineDistance(userLat, userLong, restaurantLat, restaurantLong);

                                // Add restaurant to list if within 5km and list size < 10
                                if (distance < 5000) {
                                    restaurant.setDistanceFromUser(distance);
                                    restaurantList.add(restaurant);
                                    restaurant.setRestaurantId(document.getId());
                                }
                                } catch (IOException e) {
                                   Log.e("RestaurantRepository", "sortRestaurantsByDistance: " + e.getMessage());
                                }
                            }
                        }
                        // Sort the restaurants by distance
                        restaurantList.sort(Comparator.comparingDouble(Restaurant::getDistanceFromUser));
                        callback.onSuccess(restaurantList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // function to calculate distance
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // earth radius in m
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    // function to determine if restaurant is open now
    private boolean isRestaurantOpen(String businessHours) {
        try {
            JSONObject businessHoursJson = new JSONObject(businessHours);

            // get current day of the week
            Calendar calendar = Calendar.getInstance();
            String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime());
            String time = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(calendar.getTime());

            if (!businessHoursJson.has(day) || "Closed".equalsIgnoreCase(businessHoursJson.getString(day))) {
                return false;
            }

            for (String timeSlot : businessHoursJson.getString(day).split(", ")) {
                String[] times = timeSlot.split(" to ");
                if (times.length == 2 && isTimeInRange(time, times[0], times[1])) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    private boolean isTimeInRange(String currentTime, String startTime, String endTime) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date current = timeFormat.parse(currentTime);
            Date start = parseTime(startTime);
            Date end = parseTime(endTime);

            // Handle time range that might cross midnight
            if (start.after(end)) {
                return current.after(start) || current.before(end);
            } else {
                return !current.before(start) && !current.after(end);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Date parseTime(String timeStr) throws ParseException {
        SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        timeStr = timeStr.toLowerCase().replaceAll("\\s", "");
        String time = timeStr.replaceAll("[apm]", "");
        String period = timeStr.replaceAll("[^apm]", "");

        if (!time.contains(":")) {
            time = time + ":00";
        }

        if (period.isEmpty()) {
            // If period (am/pm) is missing, assume it's PM for hours less than 12
            period = Integer.parseInt(time.split(":")[0]) < 12 ? "pm" : "am";
        }

        return TIME_FORMAT.parse(time + " " + period);
    }

}
