package com.example.mealcompass.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.Register.RegisterFragment;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentCreateAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class CreateAdminFragment extends Fragment {
    private FragmentCreateAdminBinding binding;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private UserViewModel userViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Make "Terms and Conditions" clickable and underlined
        String fullText = "I accept the Terms and Conditions";
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("Terms and Conditions");
        int endIndex = startIndex + "Terms and Conditions".length();

        // Apply underline to "Terms and Conditions"
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make "Terms and Conditions" clickable
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Navigate to the Terms and Conditions page
                NavHostFragment.findNavController(CreateAdminFragment.this)
                        .navigate(R.id.action_registerFragment_to_termsAndConditionsFragment);
            }
        };
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.acceptTermsText.setText(spannableString);
        binding.acceptTermsText.setMovementMethod(LinkMovementMethod.getInstance());

        // validate user name
        binding.nameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String name = Objects.requireNonNull(binding.nameEditText.getText()).toString();
                if (name.isEmpty()) {
                    binding.nameEditText.setError("Please enter your name");
                }
            }
        });

        // validate user email
        binding.emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = Objects.requireNonNull(binding.emailEditText.getText()).toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailEditText.setError("Please enter a valid email address");
                }
            }
        });

        // validate user password
        binding.passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String password = Objects.requireNonNull(binding.passwordEditText.getText()).toString();
                if (password.length() < 6 || !password.matches(".*[A-Z].*") || !password.matches(".*[!@#$%^&*()].*")) {
                    binding.passwordEditText.setError("Password must be at least 6 characters, contain an uppercase letter, and a special character");
                }
            }
        });


        // if register button is disabled and tried to pressed, show toast message
        binding.registerAdminButton.setOnClickListener(v -> {
            boolean isValid = true;

            // Validate name
            String name = Objects.requireNonNull(binding.nameEditText.getText()).toString().trim();
            if (name.isEmpty()) {
                binding.nameEditText.setError("Please enter your name");
                isValid = false;
            } else {
                binding.nameEditText.setError(null); // Clear previous error
            }

            // Validate email
            String email = Objects.requireNonNull(binding.emailEditText.getText()).toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditText.setError("Please enter a valid email address");
                isValid = false;
            } else {
                binding.emailEditText.setError(null);
            }

            // Validate password
            String password = Objects.requireNonNull(binding.passwordEditText.getText()).toString().trim();
            if (password.length() < 6 || !password.matches(".*[A-Z].*") || !password.matches(".*[!@#$%^&*()].*")) {
                binding.passwordEditText.setError("Password must be at least 6 characters, contain an uppercase letter, and a special character");
                isValid = false;
            } else {
                binding.passwordEditText.setError(null);
            }



            // Check if terms and conditions are accepted
            if (!binding.acceptTermsCheckBox.isChecked()) {
                Toast.makeText(getContext(), "Please accept the Terms and Conditions", Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            // If all validations are passed
            if (isValid) {
                userViewModel.setUserEmail(email);
                userViewModel.setUserPassword(password);
                userViewModel.setUserName(name);
                createAccount();
            } else {
                Toast.makeText(getContext(), "Please correct the errors above", Toast.LENGTH_SHORT).show();
            }
        });

    }


private void createAccount() {
    String name = userViewModel.getUserName().getValue();
    String email = userViewModel.getUserEmail().getValue();
    String password = userViewModel.getUserPassword().getValue();

    userRepository.createAdmin(name, email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // account creation success
                    Toast.makeText(getContext(), "Admin account created successfully", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(CreateAdminFragment.this)
                            .navigate(R.id.action_createAdminFragment_to_adminFragment);

                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getContext(), "Email is already registered", Toast.LENGTH_SHORT).show();
                }
                else {
                    // If account creation fails, display a message to the user.
                    Toast.makeText(getContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }
            });
}

@Override
public void onDestroyView() {
    super.onDestroyView();
    binding = null;
}
}