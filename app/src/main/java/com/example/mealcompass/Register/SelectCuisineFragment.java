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
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentSelectCuisineBinding;


public class SelectCuisineFragment extends Fragment {
    private FragmentSelectCuisineBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSelectCuisineBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.progressIndicator.setProgress(50);

        binding.prevButton.setOnClickListener(v -> {
            // Navigate to the previous fragment
            NavHostFragment.findNavController(SelectCuisineFragment.this)
                    .navigate(R.id.action_selectCuisineFragment2_to_selectRoleFragment22);
        });

        binding.nextButton.setOnClickListener(v-> {
            NavHostFragment.findNavController(SelectCuisineFragment.this)
                    .navigate(R.id.action_selectCuisineFragment2_to_selectDietFragment);
        });


//        //grey color filter to show cuisine is selected
//        if (binding.malay.isSelected()){
//            binding.malay.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.chinese.isSelected()) {
//            binding.chinese.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.indian.isSelected()) {
//            binding.indian.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.western.isSelected()) {
//            binding.western.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.japanese.isSelected()) {
//            binding.japanese.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.korean.isSelected()) {
//            binding.korean.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.thai.isSelected()) {
//            binding.thai.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.italian.isSelected()) {
//            binding.italian.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.nyonya.isSelected()) {
//            binding.nyonya.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.middleEastern.isSelected()) {
//            binding.middleEastern.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.german.isSelected()) {
//            binding.german.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        } else if (binding.others.isSelected()) {
//            binding.others.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
//            binding.nextButton.setClickable(true);
//        }


    }
}