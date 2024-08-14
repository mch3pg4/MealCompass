package com.example.mealcompass.Register;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentSelectAllergyBinding;

import java.util.ArrayList;


public class SelectAllergyFragment extends Fragment {
    private FragmentSelectAllergyBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSelectAllergyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressIndicator.setProgress(90);


        //create list of items for the listview
        ArrayList<SelectAllergyItem> allergyItemArrayList = new ArrayList<>();
        allergyItemArrayList.add(new SelectAllergyItem("Dairy", R.drawable.dairy, false));
        allergyItemArrayList.add(new SelectAllergyItem("Soybean", R.drawable.soybean, false));
        allergyItemArrayList.add(new SelectAllergyItem("Nut", R.drawable.nut, false));
        allergyItemArrayList.add(new SelectAllergyItem("Seafood", R.drawable.seafood, false));
        allergyItemArrayList.add(new SelectAllergyItem("Wheat", R.drawable.wheat, false));

        binding.allergyList.setAdapter(new SelectAllergyAdapter(requireContext(), allergyItemArrayList));


        binding.doneButton.setOnClickListener(v -> NavHostFragment.findNavController(SelectAllergyFragment.this)
                .navigate(R.id.action_selectAllergyFragment_to_onboardingFragment));


        binding.prevButton.setOnClickListener(v -> {
            // Navigate to the previous fragment
            NavHostFragment.findNavController(SelectAllergyFragment.this)
                    .navigate(R.id.action_selectAllergyFragment_to_selectDietFragment);
        });




    }
}