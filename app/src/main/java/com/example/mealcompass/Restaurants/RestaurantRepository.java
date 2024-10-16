package com.example.mealcompass.Restaurants;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

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
                                    e.printStackTrace();
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
