package com.example.mealcompass.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentForgotPasswordBinding;

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
            // Reset the password
            Toast.makeText(getContext(), "Reset password button pressed", Toast.LENGTH_SHORT).show();
        });
        binding.backToLogInButton.setOnClickListener(v -> NavHostFragment.findNavController(ForgotPasswordFragment.this)
                .navigate(R.id.action_forgotPasswordFragment_to_loginFragment));

        binding.signupText.setOnClickListener(v -> NavHostFragment.findNavController(ForgotPasswordFragment.this)
                .navigate(R.id.action_forgotPasswordFragment_to_registerFragment));
    }
}
