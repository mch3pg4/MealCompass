package com.example.mealcompass;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.databinding.FragmentFavouritesBinding;
import com.example.mealcompass.databinding.FragmentHomeBinding;
import com.example.mealcompass.databinding.FragmentRestaurantsBinding;


public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


        binding.profileImageButton.setOnClickListener(
                v -> NavHostFragment.findNavController(FavouritesFragment.this)
                        .navigate(R.id.action_favoruitesFragment_to_profileFragment));

    }
}