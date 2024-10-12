package com.example.mealcompass.Register;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentSelectCuisineBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelectCuisineFragment extends Fragment {
    private FragmentSelectCuisineBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserRepository userRepository;
    private UserViewModel userViewModel;

    //list to store selected cuisines
    private final List<String> selectedCuisines = new ArrayList<>();
    //map cuisines with respective image views
    private final Map<ShapeableImageView, String> cuisineMap = new HashMap<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSelectCuisineBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = null;
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
        }

        userViewModel.setUserId(userId);

        binding.progressIndicator.setProgress(50);

        binding.prevButton.setOnClickListener(v -> {
            // Navigate to the previous fragment
            NavHostFragment.findNavController(SelectCuisineFragment.this)
                    .navigate(R.id.action_selectCuisineFragment2_to_selectRoleFragment22);
        });

        //map cuisines to respective image views
        cuisineMap.put(binding.malay, "Malay");
        cuisineMap.put(binding.chinese, "Chinese");
        cuisineMap.put(binding.indian, "Indian");
        cuisineMap.put(binding.western, "Western");
        cuisineMap.put(binding.japanese, "Japanese");
        cuisineMap.put(binding.korean, "Korean");
        cuisineMap.put(binding.thai, "Thai");
        cuisineMap.put(binding.italian, "Italian");
        cuisineMap.put(binding.middleEastern, "Middle Eastern");
        cuisineMap.put(binding.nyonya, "Nyonya");
        cuisineMap.put(binding.others, "Others");

        //set up click listeners for the image views
        for (Map.Entry<ShapeableImageView, String> entry : cuisineMap.entrySet()) {
            ShapeableImageView imageView = entry.getKey();
            String cuisine = entry.getValue();
            imageView.setOnClickListener(v -> handleCuisineSelection(imageView, cuisine));
        }

        binding.nextButton.setOnClickListener(v -> {
            if (!selectedCuisines.isEmpty()) {
                updateUserCuisines(selectedCuisines);
            } else {
                Toast.makeText(getContext(), "Please select at least one cuisine", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // Method to handle cuisine selection/deselection
    private void handleCuisineSelection(ShapeableImageView imageView, String cuisine) {
        if (selectedCuisines.contains(cuisine)) {
            selectedCuisines.remove(cuisine);
            imageView.clearColorFilter();  // Remove the tint when unselected
        } else {
            selectedCuisines.add(cuisine);
            imageView.setColorFilter(Color.argb(50, 0, 0, 0), PorterDuff.Mode.SRC_IN);  // Apply the tint when selected
        }
    }


    private void updateUserCuisines(List<String> cuisines) {
        String userId = userViewModel.getUserId().getValue();
        if (userId != null) {
            userRepository.updateUserCuisines(userId, cuisines)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Error selecting user cuisines", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cuisines selected", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(SelectCuisineFragment.this)
                                    .navigate(R.id.action_selectCuisineFragment2_to_selectDietFragment);
                        }
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}