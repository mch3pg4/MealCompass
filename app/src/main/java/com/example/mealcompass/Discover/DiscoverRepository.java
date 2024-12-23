package com.example.mealcompass.Discover;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoverRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //add discover article
    public Task<DocumentReference> addDiscover(String title, String content, String imageUrl, String author, String time) {
        Map<String, Object> discover = new HashMap<>();
        discover.put("articleTitle", title);
        discover.put("articleContent", content);
        discover.put("articleImageUrl", imageUrl);
        discover.put("articleAuthor", author);
        discover.put("articleTime", time);

        return db.collection("discover")
                .add(discover)
                .addOnSuccessListener(documentReference ->
                        Log.d("DiscoverRepository", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.d("DiscoverRepository", "Error adding document", e));
    }

    // update discover article
    public Task<Void> updateDiscover(String documentId, String title, String content, String imageUrl, String author, String time) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("articleTitle", title);
        updates.put("articleContent", content);
        updates.put("articleImageUrl", imageUrl);
        updates.put("articleAuthor", author);
        updates.put("articleTime", time);

        return db.collection("discover").document(documentId)
                .update(updates)
                .addOnSuccessListener(aVoid ->
                        Log.d("DiscoverRepository", "Document updated successfully"))
                .addOnFailureListener(e ->
                        Log.d("DiscoverRepository", "Error updating document", e));
    }

    //delete discover article
    public void deleteDiscover(String documentId) {
        db.collection("discover").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid ->
                        Log.d("DiscoverRepository", "Document deleted successfully"))
                .addOnFailureListener(e ->
                        Log.d("DiscoverRepository", "Error deleting document", e));
    }


    // Callback interface
    public interface DiscoverListCallback {
        void onSuccess(List<Discover> discoverList);
        void onFailure(Exception e);
    }

    public interface DiscoverCallback {
        void onSuccess(Discover discover);
        void onFailure(Exception e);
    }


    // get discover article by id
    public void getDiscoverById(String documentId, DiscoverCallback callback) {
        db.collection("discover").document(documentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            Discover discover = document.toObject(Discover.class);
                            if (discover != null) {
                                discover.setArticleId(document.getId());
                                callback.onSuccess(discover);
                            }
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Fetch all discover articles
    public void getAllDiscover(DiscoverListCallback callback) {
    db.collection("discover")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Discover> discoverList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Discover discover = document.toObject(Discover.class);
                        if (discover != null) {
                            discover.setArticleId(document.getId());
                            discoverList.add(discover);
                            Log.d("DiscoverRepository", "Discover object: " + discover);
                        }
                    }
                    callback.onSuccess(discoverList);
                    Log.d("DiscoverRepository", "onSuccess called with " + discoverList.size() + " items.");
                } else {
                    callback.onFailure(task.getException());
                    Log.d("DiscoverRepository", "onFailure called.", task.getException());
                }
            });
}
}


