package com.example.mealcompass.Restaurants;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentRestaurantsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class RestaurantsFragment extends Fragment {

    private FragmentRestaurantsBinding binding;

    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private UserViewModel userViewModel;
    private RestaurantViewModel restaurantViewModel;

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
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false);
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

        // restaurants recycler view
        RecyclerView restaurantsRecyclerView = view.findViewById(R.id.restaurantsRecyclerView);
        restaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        // initialize viewmodel
        restaurantViewModel  = new ViewModelProvider(this).get(RestaurantViewModel.class);

        // observe restaurant items
        restaurantViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurants -> {
            if (restaurants != null && !restaurants.isEmpty()) {
                // populate recyclerview with restaurants
                List<RestaurantItem> restaurantItems = new ArrayList<>();
                for (Restaurant restaurant : restaurants) {
                    restaurantItems.add(new RestaurantItem(
                            restaurant.getRestaurantImageUrl(),
                            restaurant.getRestaurantName(),
                            restaurant.getRestaurantAddress(),
                            restaurant.getRestaurantCuisine(),
                            restaurant.getRestaurantRating(),
                            restaurant.getRestaurantPricing(),
                            restaurant.getRestaurantBusinessHours(),
                            false));
                }
                RestaurantsAdapter restaurantsAdapter = new RestaurantsAdapter(restaurantItems);
                restaurantsRecyclerView.setAdapter(restaurantsAdapter);
            } else {
                // show error message
                Toast.makeText(getContext(), "No restaurants found", Toast.LENGTH_SHORT).show();
            }
        });

        // get all restaurants
        restaurantViewModel.fetchAllRestaurants();

        // toggle between list and grid view
        binding.restaurantsViewGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.listButton) {
                    restaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else if (checkedId == R.id.gridButton) {
                    restaurantsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                }
            }
        });

        // load profile image
        binding.profileImageButton.setOnClickListener(
                v -> NavHostFragment.findNavController(RestaurantsFragment.this)
                        .navigate(R.id.action_restaurantsFragment_to_profileFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


