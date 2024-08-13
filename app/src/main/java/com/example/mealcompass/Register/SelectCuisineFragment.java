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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_cuisine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set next button to not clickable first
        binding.nextButton.setClickable(false);

        binding.prevButton.setOnClickListener(v -> {
            // Navigate to the previous fragment
            NavHostFragment.findNavController(SelectCuisineFragment.this)
                    .navigate(R.id.action_selectCuisineFragment2_to_selectRoleFragment22);
        });

        binding.progressIndicator.setProgress(3);

        //grey color filter to show cuisine is selected
        if (binding.malay.isSelected()){
            binding.malay.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.chinese.isSelected()) {
            binding.chinese.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.indian.isSelected()) {
            binding.indian.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.western.isSelected()) {
            binding.western.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.japanese.isSelected()) {
            binding.japanese.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.korean.isSelected()) {
            binding.korean.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.thai.isSelected()) {
            binding.thai.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.italian.isSelected()) {
            binding.italian.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.nyonya.isSelected()) {
            binding.nyonya.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.middleEastern.isSelected()) {
            binding.middleEastern.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.german.isSelected()) {
            binding.german.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        } else if (binding.others.isSelected()) {
            binding.others.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            binding.nextButton.setClickable(true);
        }


    }
}