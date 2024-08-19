package com.example.mealcompass.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_loginFragment_to_homeFragment);
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
                } else if (password.length() < 6) {
                    binding.passwordEditText.setError("Password must be at least 6 characters");
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
            } else if (password.length() < 6) {
                binding.passwordEditText.setError("Password must be at least 6 characters long");
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
                        binding.loginButton.setBackgroundColor(getResources().getColor(R.color.gray));
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigate(R.id.action_loginFragment_to_homeFragment);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}