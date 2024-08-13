package com.example.mealcompass.Favourites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentFavouritesBinding;

import java.util.ArrayList;
import java.util.List;


public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        RecyclerView favouritesRecyclerView = view.findViewById(R.id.favouritesRecyclerView);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<FavouritesItem> favouritesItem = new ArrayList<>();
        favouritesItem.add(new FavouritesItem(R.drawable.restaurant_img, false, "The Fat Radish", "17 Orchard St, New York, NY 10002", "American", "4.5", "$$", "Open"));
        
        favouritesItem.add(new FavouritesItem(R.drawable.restaurant_img, false, "Georgetown","17 Orchard St, New York, NY 10002", "American", "4.5", "$$", "Open"));


        FavouritesAdapter favouritesAdapter = new FavouritesAdapter(favouritesItem);
        favouritesRecyclerView.setAdapter(favouritesAdapter);

        // Toggle between list and grid view
        binding.viewAsButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.viewAsList) {
                    favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else if (checkedId == R.id.viewAsGrid) {
                    favouritesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                }
            }
        });


        binding.profileImageButton.setOnClickListener(
                v -> NavHostFragment.findNavController(FavouritesFragment.this)
                        .navigate(R.id.action_favoruitesFragment_to_profileFragment));

    }
}