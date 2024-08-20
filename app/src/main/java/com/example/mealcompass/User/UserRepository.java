package com.example.mealcompass.User;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class UserRepository {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    public UserRepository(FirebaseAuth mAuth, FirebaseFirestore db) {
        this.mAuth = mAuth;
        this.db = db;
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



