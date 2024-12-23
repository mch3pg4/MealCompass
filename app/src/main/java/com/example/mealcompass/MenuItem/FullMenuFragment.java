package com.example.mealcompass.MenuItem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.databinding.FragmentFullMenuBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FullMenuFragment extends Fragment {
    private FragmentFullMenuBinding binding;
    private UserRepository userRepository;
    private FirebaseAuth mAuth;
    private RestaurantViewModel restaurantViewModel;
    private String restaurantId, restaurantName;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFullMenuBinding.inflate(inflater, container, false);
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

        // if usertype is not owner, dont show the add fab
        if (userId != null) {
            userRepository.getUserType(userId).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String userType = task.getResult();
                    if (userType != null && userType.equals("owner")) {
                        binding.addFab.setVisibility(View.VISIBLE);
                    } else {
                        binding.addFab.setVisibility(View.GONE);
                    }
                }
            });
        }

        binding.addFab.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Add menu item", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigate(R.id.action_fullMenuFragment_to_editMenuItemFragment);
        });

        // Set up RecyclerView
        RecyclerView menuItemsRecyclerView = binding.menuItemsRecyclerView;
        menuItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // scroll to top button
        // if the user scrolls down, the button will appear
        menuItemsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        binding.scrollToTopFab.setOnClickListener(v -> menuItemsRecyclerView.smoothScrollToPosition(0));

        // Handle restaurant data
        String finalUserId = userId;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            restaurantId = bundle.getString("restaurantId");
            restaurantName = bundle.getString("restaurantName");
            setupRestaurantData();
        } else {
            // get restaurantid from user id
            restaurantViewModel.fetchRestaurantByOwnerId(finalUserId);
            restaurantViewModel.getRestaurantListLiveData().observe(getViewLifecycleOwner(), restaurantList -> {
                if (restaurantList != null && !restaurantList.isEmpty()) {
                    restaurantId = restaurantList.get(0).getRestaurantId();
                    restaurantName = restaurantList.get(0).getRestaurantName();
                    setupRestaurantData();
                } else {
                    Toast.makeText(requireContext(), "No restaurant found", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupRestaurantData() {
        // Set restaurant name
        binding.restaurantNameMenuTitle.setText(restaurantName);
        // observe menu items
        restaurantViewModel.getMenuItemsLiveData().observe(getViewLifecycleOwner(), fullMenu -> {
            if (fullMenu != null) {
                // prepare the list to display
                List<MenuItem> menuItems = new ArrayList<>();
                for (MenuItem menuItem : fullMenu) {
                    menuItems.add(new MenuItem(
                            menuItem.getMenuItemId(),
                            menuItem.getMenuItemImage(),
                            menuItem.getMenuItemName(),
                            menuItem.getMenuItemDescription(),
                            menuItem.getMenuItemPrice(),
                            menuItem.getMenuItemNutritionalValue(),
                            menuItem.getMenuItemCategory(),
                            menuItem.getMenuItemAllergens(),
                            menuItem.isItemBestSeller()
                    ));
                }
                // set the adapter
                FullMenuAdapter menuItemAdapter = new FullMenuAdapter(menuItems);
                binding.menuItemsRecyclerView.setAdapter(menuItemAdapter);
            } else {
                // show a textview if no menu items are found
                binding.noMenuItemsTextView.setVisibility(View.VISIBLE);
            }
        });

        // Fetch the menu only after we have the restaurant ID
        restaurantViewModel.fetchRestaurantMenu(restaurantId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}