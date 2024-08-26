package com.example.mealcompass.User;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mealcompass.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

public class UserRepository {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    public UserRepository(FirebaseAuth mAuth, FirebaseFirestore db) {
        this.mAuth = mAuth;
        this.db = db;
    }

    public void loadUserProfileImage(String userId, ImageView imageView, Context context) {
        // load image from firebase storage using Glide
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profilePicRef = storageReference.child("users/" + userId + ".jpg");

        // Fetch the download URL and load image into button
        profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Use Glide to load the image using the URL
            Glide.with(context)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)// Use the download URL instead of the StorageReference
                    .into(imageView);
        }).addOnFailureListener(exception -> {
            // Handle the error, e.g., image not found
            Toast.makeText(context, "Failed to load profile picture", Toast.LENGTH_SHORT).show();
        });
    }

    //get user type
    public Task<String> getUserType(String userId) {
        return db.collection("users")
                .document(userId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            return document.getString("userType");
                        } else {
                            throw new Exception("User not found");
                        }
                    } else {
                        throw Objects.requireNonNull(task.getException());
                    }
                });
    }

    public Task<AuthResult> signIn(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<String> getUserName(String userId) {
        return db.collection("users")
                .document(userId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            return document.getString("userName");
                        } else {
                            throw new Exception("User not found");
                        }
                    } else {
                        throw Objects.requireNonNull(task.getException());
                    }
                });
    }

    public Task<Task<AuthResult>> createUser(String name, String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password)
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            User user = new User(name, email, "", "", null, null, null, null, null);
                            return db.collection("users")
                                    .document(firebaseUser.getUid())
                                    .set(user)
                                    .continueWith(t -> task);
                        }
                    }
                    throw Objects.requireNonNull(task.getException());
                });
    }

    public Task<Void> updateUserImageUrl(String userId, String imageUrl) {
        return db.collection("users")
                .document(userId)
                .update("userImageUrl", imageUrl);
    }

    public Task<Void> updateUserType(String userId, String userType) {
        return db.collection("users")
                .document(userId)
                .update("userType", userType);
    }

    public Task<Void> updateUserCuisines(String userId, List<String> cuisine) {
        return db.collection("users")
                .document(userId)
                .update("userCuisines", cuisine);
    }

    public Task<Void> updateUserDiets(String userId, List<String> diet) {
        return db.collection("users")
                .document(userId)
                .update("userDiets", diet);
    }

    public Task<Void> updateUserAllergens(String userId, List<String> allergen) {
        return db.collection("users")
                .document(userId)
                .update("userAllergens", allergen);

    }
}



