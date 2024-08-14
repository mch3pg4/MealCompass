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
import com.example.mealcompass.databinding.FragmentSelectRoleBinding;
import com.google.android.material.card.MaterialCardView;


public class SelectRoleFragment extends Fragment {
    private FragmentSelectRoleBinding binding;


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
                // Navigate to the next fragment
                NavHostFragment.findNavController(SelectRoleFragment.this)
                        .navigate(R.id.action_selectRoleFragment2_to_selectCuisineFragment2);
            } else if (binding.adminRoleCard.isChecked()) {
                // Navigate to the next fragment
                NavHostFragment.findNavController(SelectRoleFragment.this)
                        .navigate(R.id.action_selectRoleFragment2_to_adminFragment);
            } else if (binding.ownerRoleCard.isChecked()) {
                // Navigate to the next fragment
                NavHostFragment.findNavController(SelectRoleFragment.this)
                        .navigate(R.id.action_selectRoleFragment2_to_fillInRestDetailsFragment);
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
                if (roleCard != binding.userRoleCard) binding.userRoleCard.setChecked(false);
                if (roleCard != binding.adminRoleCard) binding.adminRoleCard.setChecked(false);
                if (roleCard != binding.ownerRoleCard) binding.ownerRoleCard.setChecked(false);
            }
        });
    }
}