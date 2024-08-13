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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_allergy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set next button to not clickable first
        binding.doneButton.setClickable(false);

        //create list of items for the listview
        ArrayList<SelectAllergyItem> allergyItemArrayList = new ArrayList<>();
        allergyItemArrayList.add(new SelectAllergyItem("Dairy", R.drawable.dairy, false));
        allergyItemArrayList.add(new SelectAllergyItem("Soybean", R.drawable.soybean, false));
        allergyItemArrayList.add(new SelectAllergyItem("Nut", R.drawable.nut, false));
        allergyItemArrayList.add(new SelectAllergyItem("Seafood", R.drawable.seafood, false));
        allergyItemArrayList.add(new SelectAllergyItem("Wheat", R.drawable.wheat, false));

        binding.allergyList.setAdapter(new SelectAllergyAdapter(requireContext(), allergyItemArrayList));

        binding.allergyList.setOnItemClickListener((parent, view1, position, id) -> {
            SelectAllergyItem currentItem = allergyItemArrayList.get(position);
            currentItem.setChecked(!currentItem.isChecked());
            binding.allergyList.invalidateViews();

            //check if any item is checked
            boolean anyChecked = false;
            for (SelectAllergyItem item : allergyItemArrayList) {
                if (item.isChecked()) {
                    anyChecked = true;
                    break;
                }
            }

            //if any item is checked, set done button to clickable and proceed to next fragment
            binding.doneButton.setClickable(anyChecked);
            binding.doneButton.setOnClickListener(v -> NavHostFragment.findNavController(SelectAllergyFragment.this)
                    .navigate(R.id.action_selectAllergyFragment_to_onboardingFragment));
        });


        binding.prevButton.setOnClickListener(v -> {
            // Navigate to the previous fragment
            NavHostFragment.findNavController(SelectAllergyFragment.this)
                    .navigate(R.id.action_selectAllergyFragment_to_selectDietFragment);
        });

        binding.progressIndicator.setProgress(9);


    }
}