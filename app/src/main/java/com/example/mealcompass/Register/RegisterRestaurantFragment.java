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

import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentRegisterRestaurantBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterRestaurantFragment extends Fragment {
    private FragmentRegisterRestaurantBinding binding;
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
        binding = FragmentRegisterRestaurantBinding.inflate(inflater, container, false);
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

        binding.progressIndicator.setProgress(35);

        setupRoleCard(binding.claimRestCard, "Selected claim restaurant");
        setupRoleCard(binding.addRestCard, "Selected add restaurant");

        binding.prevButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_registerRestaurantFragment_to_selectRoleFragment2);

        });

        binding.nextButton.setOnClickListener(v -> {
            if (!binding.claimRestCard.isChecked() && !binding.addRestCard.isChecked()) {
                Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_SHORT).show();
            } else {
                if (binding.claimRestCard.isChecked()) {
                    NavHostFragment.findNavController(this).navigate(R.id.action_registerRestaurantFragment_to_claimRestaurantFragment);
                } else {
                    NavHostFragment.findNavController(this).navigate(R.id.action_registerRestaurantFragment_to_fillInRestDetailsFragment);
                }
            }

        });


    }

    // function for making sure only one role is selected
    private void setupRoleCard(MaterialCardView roleCard, String message) {
        roleCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            roleCard.setChecked(true);
        });

        roleCard.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (roleCard != binding.claimRestCard) binding.claimRestCard.setChecked(false);
                if (roleCard != binding.addRestCard) binding.addRestCard.setChecked(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}