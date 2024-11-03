package com.example.mealcompass.Register;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.resetPasswordButton.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.emailEditText.getText()).toString();
            if (email.isEmpty()) {
                binding.emailEditText.setError("Email is required");
                binding.emailEditText.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditText.setError("Invalid email format");
                binding.emailEditText.requestFocus();
            } else {
                sendResetPasswordEmail(email);
            }
        });
        binding.backToLogInButton.setOnClickListener(v -> NavHostFragment.findNavController(ForgotPasswordFragment.this)
                .navigate(R.id.action_forgotPasswordFragment_to_loginFragment));

        binding.signupText.setOnClickListener(v -> NavHostFragment.findNavController(ForgotPasswordFragment.this)
                .navigate(R.id.action_forgotPasswordFragment_to_registerFragment));
    }

    public void sendResetPasswordEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to send email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
