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
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class AdminFragment extends Fragment {
    private FragmentAdminBinding binding;
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

        List<RecommendRestaurantsItem> recommendRestaurantsItems = List.of(
                new RecommendRestaurantsItem(R.drawable.restaurant_img, "The Fat Radish", "4.5"),
                new RecommendRestaurantsItem(R.drawable.restaurant_img, "The Fat Radish", "4.5"),
                new RecommendRestaurantsItem(R.drawable.restaurant_img, "The Fat Radish", "4.5")
        );

        RecommendRestaurantsAdapter recommendRestaurantsAdapter = new RecommendRestaurantsAdapter(recommendRestaurantsItems);
        recommendRestaurantsRecyclerView.setAdapter(recommendRestaurantsAdapter);

        binding.showAllRestaurantText.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Show all restaurants", Toast.LENGTH_SHORT).show();
        });

        // users list recycler view
        RecyclerView usersListRecyclerView = view.findViewById(R.id.restaurantUsersRecyclerView);
        usersListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<UserListItem> usersListItems = List.of(
                new UserListItem("John Doe", R.drawable.placeholder),
                new UserListItem("John Doe", R.drawable.placeholder),
                new UserListItem("John Doe", R.drawable.placeholder)
        );

        UserListAdapter usersListAdapter = new UserListAdapter(usersListItems);
        usersListRecyclerView.setAdapter(usersListAdapter);


        binding.showAllUsersText.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Show all users", Toast.LENGTH_SHORT).show();
        });

    }
}