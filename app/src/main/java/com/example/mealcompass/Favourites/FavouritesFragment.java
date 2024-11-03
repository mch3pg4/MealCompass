package com.example.mealcompass.Favourites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.Restaurant;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentFavouritesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
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
        super.onViewCreated(view, savedInstanceState);


        FirebaseUser user = mAuth.getCurrentUser();
        String userId = null;
        if (user != null) {
            userId = user.getUid();
        }

        // load user name and profile image
        // set up profile image
        userRepository.loadUserProfileImage(userId, binding.profileImageButton, requireContext());

        // set up search bar
        String finalUserId = userId;
        binding.favouritesSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }

            private void performSearch(String query) {
                if (query == null || query.trim().isEmpty()) {
                    userViewModel.getUserFavouriteRestaurants(finalUserId);
                } else {
                    userViewModel.searchUserFavouriteRestaurants(finalUserId, query.trim());
                }
            }

        });

        RecyclerView favouritesRecyclerView = binding.favouritesRecyclerView;
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get user's favorite restaurants
        userViewModel.getFavouriteRestaurants().observe(getViewLifecycleOwner(), favouriteRestaurants -> {
            if (favouriteRestaurants == null || favouriteRestaurants.isEmpty()) {
                Toast.makeText(getContext(), "No favourite restaurants found", Toast.LENGTH_SHORT).show();

            }

            List<FavouritesItem> favouritesItems = new ArrayList<>();
            assert favouriteRestaurants != null;
            for (Restaurant restaurant : favouriteRestaurants) {
                FavouritesItem favouritesItem = new FavouritesItem(
                        restaurant.getRestaurantId(),
                        restaurant.getRestaurantImageUrl(),
                        true,
                        restaurant.getRestaurantName(),
                        restaurant.getRestaurantAddress(),
                        restaurant.getRestaurantCuisine(),
                        restaurant.getRestaurantRating(),
                        restaurant.getRestaurantPricing(),
                        restaurant.getRestaurantBusinessHours());
                favouritesItems.add(favouritesItem);
            }
            FavouritesAdapter favouritesAdapter = new FavouritesAdapter(favouritesItems);
            favouritesRecyclerView.setAdapter(favouritesAdapter);

        });

        userViewModel.getUserFavouriteRestaurants(userId);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

