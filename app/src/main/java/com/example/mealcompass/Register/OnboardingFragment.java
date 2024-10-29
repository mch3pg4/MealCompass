package com.example.mealcompass.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.MainActivity;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentOnboardingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class OnboardingFragment extends Fragment {
    private FragmentOnboardingBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserViewModel userViewModel;
    private UserRepository userRepository;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new UserViewModel();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();


            userRepository.getUserType(userId).addOnSuccessListener(userType -> {
                if (userType != null) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.updateBottomNavView(userType);
                    }
                }
            });
        }

        binding.proceedButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_onboardingFragment_to_homeFragment));

        }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
        }
}