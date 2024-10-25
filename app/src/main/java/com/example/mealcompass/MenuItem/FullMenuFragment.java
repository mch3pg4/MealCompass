package com.example.mealcompass.MenuItem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.Restaurants.RestaurantViewModel;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.databinding.FragmentFullMenuBinding;
import com.example.mealcompass.databinding.FragmentRestaurantDetailsBinding;
import com.google.android.gms.maps.GoogleMap;
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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String restaurantId = bundle.getString("restaurantId");
            String restaurantName = bundle.getString("restaurantName");

            binding.restaurantNameMenuTitle.setText(restaurantName);

            // menu items recycler view
            RecyclerView menuItemsRecyclerView = binding.menuItemsRecyclerView;
            menuItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

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
                }
                else {
                    Toast.makeText(requireContext(), "No menu items found", Toast.LENGTH_SHORT).show();
                }
            });

            // get the full menu of the restaurant from id
            restaurantViewModel.fetchRestaurantMenu(restaurantId);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}