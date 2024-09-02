package com.example.mealcompass.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.MainActivity;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentSelectRoleBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class SelectRoleFragment extends Fragment {
    private FragmentSelectRoleBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserRepository userRepository;
    private UserViewModel userViewModel;

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
        binding = FragmentSelectRoleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId;
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
        } else {
            userId = null;
        }

        userViewModel.setUserId(userId);

        binding.progressIndicator.setProgress(30);

        setupRoleCard(binding.userRoleCard, "User role selected");
        setupRoleCard(binding.adminRoleCard, "Admin role selected");
        setupRoleCard(binding.ownerRoleCard, "Restaurant Owner role selected");

        binding.prevButton.setOnClickListener(v -> NavHostFragment.findNavController(SelectRoleFragment.this)
                .navigate(R.id.action_selectRoleFragment2_to_addProfilePicFragment));

        binding.nextButton.setOnClickListener(v -> {
            if (!binding.userRoleCard.isChecked() && !binding.adminRoleCard.isChecked() && !binding.ownerRoleCard.isChecked()) {
                // Toast message to say no role selected
                Toast.makeText(getContext(), "Please select a role to proceed", Toast.LENGTH_SHORT).show();
            } else if (binding.userRoleCard.isChecked()) {
                // Update the user type in the database
                updateUserType("user");
                // Navigate to the next fragment
                NavHostFragment.findNavController(SelectRoleFragment.this)
                        .navigate(R.id.action_selectRoleFragment2_to_selectCuisineFragment2);
            } else if (binding.adminRoleCard.isChecked()) {
                // Update the user type in the database
                updateUserType("admin");
                // Navigate to the next fragment
                NavHostFragment.findNavController(SelectRoleFragment.this)
                        .navigate(R.id.action_selectRoleFragment2_to_adminFragment);
            } else if (binding.ownerRoleCard.isChecked()) {
                // Update the user type in the database
                updateUserType("owner");
                // Navigate to the next fragment
                NavHostFragment.findNavController(SelectRoleFragment.this)
                        .navigate(R.id.action_selectRoleFragment2_to_fillInRestDetailsFragment);
            }

            userRepository.getUserType(userId).addOnSuccessListener(userType -> {
                if (userType != null) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.updateBottomNavView(userType);
                    }
                }
            });

        });
    }

    private void updateUserType(String userType) {
        String userId = userViewModel.getUserId().getValue();
        if (userId != null) {
            userRepository.updateUserType(userId, userType)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Error selecting user role", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    // function for making sure only one role is selected
    private void setupRoleCard(MaterialCardView roleCard, String message) {
        roleCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            roleCard.setChecked(true);
        });

        roleCard.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (roleCard != binding.userRoleCard) binding.userRoleCard.setChecked(false);
                if (roleCard != binding.adminRoleCard) binding.adminRoleCard.setChecked(false);
                if (roleCard != binding.ownerRoleCard) binding.ownerRoleCard.setChecked(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}