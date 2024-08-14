package com.example.mealcompass.RestaurantOwner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentFillInRestDetailsBinding;

import java.util.Arrays;
import java.util.List;


public class FillInRestDetailsFragment extends Fragment {
    private FragmentFillInRestDetailsBinding binding;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentFillInRestDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> items = Arrays.asList(getResources().getStringArray(R.array.cuisine_list));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.cuisine_list_item, items);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) binding.cuisineSelectMenu.getEditText();
        if (autoCompleteTextView != null) {
            autoCompleteTextView.setAdapter(adapter);
        }


        binding.prevButton.setOnClickListener(v -> NavHostFragment.findNavController(FillInRestDetailsFragment.this)
                .navigate(R.id.action_fillInRestDetailsFragment_to_selectRoleFragment2));

        binding.nextButton.setOnClickListener(v -> {
            // Navigate to the next fragment
            NavHostFragment.findNavController(FillInRestDetailsFragment.this)
                    .navigate(R.id.action_fillInRestDetailsFragment_to_addRestImageFragment);
        });
        }


    }
