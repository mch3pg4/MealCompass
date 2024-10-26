package com.example.mealcompass.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.Home.RecommendRestaurantsAdapter;
import com.example.mealcompass.Home.RecommendRestaurantsItem;
import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.Restaurant;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.User;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class AdminFragment extends Fragment {
    private FragmentAdminBinding binding;
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
        binding = FragmentAdminBinding.inflate(inflater, container, false);
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
        binding.profileImageButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminFragment_to_profileFragment);
        });

        // restaurant requests recycler view
        RecyclerView restaurantRequestsRecyclerView = view.findViewById(R.id.restaurantRequestsRecyclerView);
        restaurantRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<RestaurantRequestsItem> restaurantRequestsItems = List.of(
                new RestaurantRequestsItem(R.drawable.restaurant_img, "The Fat Radish", "American"),
                new RestaurantRequestsItem(R.drawable.restaurant_img, "The Fat Radish", "American"),
                new RestaurantRequestsItem(R.drawable.restaurant_img, "The Fat Radish", "American")
        );

        RestaurantRequestsAdapter restaurantRequestsAdapter = new RestaurantRequestsAdapter(restaurantRequestsItems);
        restaurantRequestsRecyclerView.setAdapter(restaurantRequestsAdapter);


        binding.showAllRequestsText.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Show all requests", Toast.LENGTH_SHORT).show();
        });

        // restaurants list recycler view
        RecyclerView recommendRestaurantsRecyclerView = view.findViewById(R.id.restaurantListRecyclerView);
        recommendRestaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialize viewmodel
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        // get all restaurants
        restaurantViewModel.fetchAllRestaurants();

        // Observe the LiveData for changes
        restaurantViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurants -> {
            if (restaurants != null && !restaurants.isEmpty()) {
                // Populate the RecyclerView with the fetched restaurants
                List<RecommendRestaurantsItem> restaurantListItems = new ArrayList<>();
                for (Restaurant restaurant : restaurants) {
                    restaurantListItems.add(new RecommendRestaurantsItem(
                            restaurant.getRestaurantId(),
                            restaurant.getRestaurantImageUrl(),
                            restaurant.getRestaurantName(),
                            restaurant.getRestaurantAddress(),
                            restaurant.getRestaurantCuisine(),
                            restaurant.getRestaurantContact(),
                            restaurant.getRestaurantBusinessHours(),
                            restaurant.getRestaurantRating()));
                }
                RecommendRestaurantsAdapter restaurantListAdapter = new RecommendRestaurantsAdapter(restaurantListItems);
                recommendRestaurantsRecyclerView.setAdapter(restaurantListAdapter);
            } else {
                Toast.makeText(getContext(), "No restaurants found", Toast.LENGTH_SHORT).show();
            }
        });



//        RecommendRestaurantsAdapter recommendRestaurantsAdapter = new RecommendRestaurantsAdapter(recommendRestaurantsItems);
//        recommendRestaurantsRecyclerView.setAdapter(recommendRestaurantsAdapter);

        binding.showAllRestaurantText.setOnClickListener(v -> NavHostFragment.findNavController(this)
                .navigate(R.id.action_adminFragment_to_restaurantsFragment));

        // users list recycler view
        RecyclerView usersListRecyclerView = binding.restaurantUsersRecyclerView;
        usersListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialize viewmodel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // get all users
        userViewModel.getAllUsers();

        // Observe the LiveData for changes
        userViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), users -> {
            if (users != null && !users.isEmpty()) {
                // Populate the RecyclerView with the fetched users
                List<UserListItem> usersListItems = new ArrayList<>();
                for (User userList : users) {
                    usersListItems.add(new UserListItem(
                            userList.getUserName(),
                            userList.getUserId()));
                }
                UserListAdapter usersListAdapter = new UserListAdapter(usersListItems, userRepository, getContext());
                usersListRecyclerView.setAdapter(usersListAdapter);
            } else {
                Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
            }
        });

        binding.showAllUsersText.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminFragment_to_showAllUsersFragment);
        });

    }
}