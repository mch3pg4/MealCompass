package com.example.mealcompass.Register;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mealcompass.MainActivity;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // get the current user type to determine which bottom navigation view to show
        userRepository = new UserRepository(mAuth, db);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String userId = currentUser.getUid();

            userRepository.getUserType(userId).addOnSuccessListener(userType -> {
                if (userType != null) {
                    switch (userType) {
                        case "user":
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_loginFragment_to_homeFragment);
                            break;
                        case "owner":
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_loginFragment_to_restaurantOwnerFragment2);
                            break;
                        case "admin":
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_loginFragment_to_adminFragment);
                            break;
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //validate user input
        binding.emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = Objects.requireNonNull(binding.emailEditText.getText()).toString();
                if (email.isEmpty()) {
                    binding.emailEditText.setError("Email is required");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailEditText.setError("Invalid email address");
                } else {
                    binding.emailEditText.setError(null);
                }
            }
        });

        // Password validation
        binding.passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String password = Objects.requireNonNull(binding.passwordEditText.getText()).toString();
                if (password.isEmpty()) {
                    binding.passwordEditText.setError("Password is required");
                } else if (password.length() < 6 || !password.matches(".*[A-Z].*") || !password.matches(".*[!@#$%^&*()].*")) {
                    binding.passwordEditText.setError("Password must be at least 6 characters, contain an uppercase letter, and a special character");
                } else {
                    binding.passwordEditText.setError(null);
                }
            }
        });

        binding.loginButton.setOnClickListener(v-> {
            String email = Objects.requireNonNull(binding.emailEditText.getText()).toString();
            String password = Objects.requireNonNull(binding.passwordEditText.getText()).toString();

            if (email.isEmpty()) {
                binding.emailEditText.setError("Email is required");
                binding.emailEditText.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditText.setError("Invalid email format");
                binding.emailEditText.requestFocus();
            } else if (password.isEmpty()) {
                binding.passwordEditText.setError("Password is required");
                binding.passwordEditText.requestFocus();
            } else if (password.length() < 6 ) {
                binding.passwordEditText.setError("Password must be at least 6 characters");
                binding.passwordEditText.requestFocus();
            } else {
                // If all validations pass, sign in the user
                signInUser(email, password);

            }
        });

        binding.signupText.setOnClickListener(v -> NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment_to_registerFragment));

        binding.forgotPasswordText.setOnClickListener(v -> NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment_to_forgotPasswordFragment));
    }

    private void signInUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // make the login button unclickable and greyed out
                        binding.loginButton.setClickable(false);
                        binding.loginButton.setText(R.string.logging_in);
                        binding.loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user != null){
                            String userId = user.getUid();
                            userRepository.getUserType(userId).addOnSuccessListener(userType -> {
                                if (userType != null) {
                                    MainActivity mainActivity = (MainActivity) getActivity();
                                    if (mainActivity != null) {
                                        mainActivity.updateBottomNavView(userType);
                                    }
                                    switch (userType) {
                                        case "user":
                                            NavHostFragment.findNavController(LoginFragment.this)
                                                    .navigate(R.id.action_loginFragment_to_homeFragment);
                                            break;
                                        case "owner":
                                            NavHostFragment.findNavController(LoginFragment.this)
                                                    .navigate(R.id.action_loginFragment_to_restaurantOwnerFragment2);
                                            break;
                                        case "admin":
                                            NavHostFragment.findNavController(LoginFragment.this)
                                                    .navigate(R.id.action_loginFragment_to_adminFragment);
                                            break;
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "User is either not found or has been deleted from the system.", Toast.LENGTH_SHORT).show();
                                        binding.loginButton.setClickable(true);
                                        binding.loginButton.setText(R.string.login);
                                        binding.loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.m3_sys_color_light_primary));
                                    });
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}