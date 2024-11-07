package com.example.mealcompass.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.Restaurants.Restaurant;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentShowAllRequestsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class ShowAllRequestsFragment extends Fragment {
    private FragmentShowAllRequestsBinding binding;
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
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowAllRequestsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        String userId;
        if (user != null) {
            user.getUid();
        } else {
        }

        // restaurant requests recyclerview
        RecyclerView restaurantRequestsRecyclerView = binding.showAllRequestsRecyclerView;
        restaurantRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // scroll to top button
        // if the user scrolls down, the button will appear
        restaurantRequestsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // Check if recyclerview is at the top
                if (!recyclerView.canScrollVertically(-1)) {
                    // if at the top then hide fab
                    binding.scrollToTopFab.setVisibility(View.GONE);
                } else if (dy > 0) {
                    // if user scroll down, show fab
                    binding.scrollToTopFab.setVisibility(View.VISIBLE);
                }
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        // scroll to top when fab is clicked
        binding.scrollToTopFab.setOnClickListener(v -> restaurantRequestsRecyclerView.smoothScrollToPosition(0));

        // get all restaurant requests
        restaurantViewModel.fetchRestaurantByPendingStatus();

        // observe restaurant requests
        restaurantViewModel.getRestaurantRequestsListLiveData().observe(getViewLifecycleOwner(), requests -> {
            if (requests != null && !requests.isEmpty()) {
                // populate recyclerview
                List<RestaurantRequestsItem> restaurantRequestsItems = new ArrayList<>();
                for (Restaurant restaurant : requests) {
                    restaurantRequestsItems.add(new RestaurantRequestsItem(
                            restaurant.getRestaurantId(),
                            restaurant.getRestaurantImageUrl(),
                            restaurant.getRestaurantName(),
                            restaurant.getRestaurantCuisine()
                    ));
                }

                RestaurantRequestsAdapter adapter = new RestaurantRequestsAdapter(restaurantRequestsItems);
                restaurantRequestsRecyclerView.setAdapter(adapter);

            } else {
                // show empty state
                Toast.makeText(getContext(), "No restaurant requests found", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}