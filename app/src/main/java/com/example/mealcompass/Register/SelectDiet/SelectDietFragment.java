package com.example.mealcompass.Register.SelectDiet;

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
import com.example.mealcompass.databinding.FragmentSelectDietBinding;

import java.util.ArrayList;


public class SelectDietFragment extends Fragment {

    private FragmentSelectDietBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_diet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set next button to not clickable first
        binding.nextButton.setClickable(false);

        //create list of items for the listview
        ArrayList<SelectDietItem> dietItemArrayList = new ArrayList<>();
        dietItemArrayList.add(new SelectDietItem("Vegetarian", false));
        dietItemArrayList.add(new SelectDietItem("Halal", false));
        dietItemArrayList.add(new SelectDietItem("Low carb", false));
        dietItemArrayList.add(new SelectDietItem("Gluten-free", false));
        dietItemArrayList.add(new SelectDietItem("Dairy-free", false));
        dietItemArrayList.add(new SelectDietItem("Keto", false));

        // find listview
        binding.dietList.setAdapter(new SelectDietAdapter(requireContext(), dietItemArrayList));

        // if an item is clicked, change the state of the next button
        binding.dietList.setOnItemClickListener((parent, view1, position, id) -> {
            SelectDietItem currentItem = dietItemArrayList.get(position);
            currentItem.setChecked(!currentItem.isChecked());
            binding.dietList.invalidateViews();

            // check if any item is selected
            boolean isAnyItemSelected = false;
            for (SelectDietItem item : dietItemArrayList) {
                if (item.isChecked()) {
                    isAnyItemSelected = true;
                    break;
                }
            }

            // if any item is selected, make the next button clickable
            binding.nextButton.setClickable(isAnyItemSelected);
        });



        binding.prevButton.setOnClickListener(v -> {
            // Navigate to the previous fragment
            NavHostFragment.findNavController(SelectDietFragment.this)
                    .navigate(R.id.action_selectDietFragment_to_selectCuisineFragment2);
        });

        binding.progressIndicator.setProgress(6);


    }
}