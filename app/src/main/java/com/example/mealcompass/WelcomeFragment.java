package com.example.mealcompass;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.databinding.FragmentWelcomeBinding;


public class WelcomeFragment extends Fragment {
    private FragmentWelcomeBinding binding;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginButton.setOnClickListener(v -> NavHostFragment.findNavController(WelcomeFragment.this)
                .navigate(R.id.action_welcomeFragment_to_loginFragment));

        binding.signupButton.setOnClickListener(v -> NavHostFragment.findNavController(WelcomeFragment.this)
                .navigate(R.id.action_welcomeFragment_to_registerFragment));


    }
}

