package com.example.mealcompass.RestaurantOwner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentRestaurantOwnerBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RestaurantOwnerFragment extends Fragment {
    private FragmentRestaurantOwnerBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantOwnerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.profileImageButton.setOnClickListener(v-> {
            NavHostFragment.findNavController(this).navigate(R.id.action_restaurantOwnerFragment_to_profileFragment);
        });


        binding.editButton.setOnClickListener(v-> {
            Toast.makeText(getContext(), "Edit Restaurant Details", Toast.LENGTH_SHORT).show();
        });

        binding.restaurantMapView.setVisibility(View.GONE);
        // show map when clicked on restaurant address field
        binding.restAddressEditText.setOnClickListener(v -> {
            binding.restaurantMapView.setVisibility(View.VISIBLE);

            // Initialize map


        });

        // adapter for list of cuisines
        List<String> items = Arrays.asList(getResources().getStringArray(R.array.cuisine_list));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.cuisine_list_item, items);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) binding.cuisineSelectMenu.getEditText();
        if (autoCompleteTextView != null) {
            autoCompleteTextView.setAdapter(adapter);
        }

        // Initialize layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.businessHoursRecyclerView.setLayoutManager(layoutManager);

        // adapter for business hours recycler view
        List<FillInBusinessHoursItem> businessHoursItems = new ArrayList<>();
        businessHoursItems.add(new FillInBusinessHoursItem("Mon", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Tues", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Wed", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Thurs", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Fri", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Sat", "9:00 AM", "6:00 PM", "", "", false, false));
        businessHoursItems.add(new FillInBusinessHoursItem("Sun", "9:00 AM", "6:00 PM", "", "", false, false));

        FillInBusinessHoursAdapter adapter1 = new FillInBusinessHoursAdapter(businessHoursItems, getChildFragmentManager());
        binding.businessHoursRecyclerView.setAdapter(adapter1);

        binding.splitHoursCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (int i = 0; i < businessHoursItems.size(); i++) {
                FillInBusinessHoursItem item = businessHoursItems.get(i);
                if (item.isSplitHours() != isChecked) {
                    item.setSplitHours(isChecked);
                    adapter1.notifyItemChanged(i);
                }
            }
        });



    }
}