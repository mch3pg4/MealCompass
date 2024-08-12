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

        //grey color filter for clicked shapeable image view
        //shapeableImageView.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);




    }
}