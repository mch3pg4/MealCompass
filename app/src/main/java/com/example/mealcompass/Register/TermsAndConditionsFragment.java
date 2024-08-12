package com.example.mealcompass.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentTermsAndConditionsBinding;


public class TermsAndConditionsFragment extends Fragment {
    private FragmentTermsAndConditionsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.proceedButton.setOnClickListener(v-> NavHostFragment.findNavController(TermsAndConditionsFragment.this)
                .navigate(R.id.action_termsAndConditionsFragment_to_registerFragment));
    }
}