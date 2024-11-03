package com.example.mealcompass.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.databinding.FragmentTermsAndConditionsBinding;


public class TermsAndConditionsFragment extends Fragment {
    private FragmentTermsAndConditionsBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTermsAndConditionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.proceedButton.setOnClickListener(
                // go back to last fragment
                v -> NavHostFragment.findNavController(this).popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}