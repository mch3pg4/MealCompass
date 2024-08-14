package com.example.mealcompass.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView profileSettingsRecyclerView = view.findViewById(R.id.profileSettingsRecyclerView);
        profileSettingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ProfileSettingsItem> profileItems = new ArrayList<>();
        profileItems.add(new ProfileSettingsItem(R.drawable.baseline_account_circle_24, "Edit Profile"));
        profileItems.add(new ProfileSettingsItem(R.drawable.baseline_tune_24, "Reselect Preferences"));

        ProfileSettingsAdapter profileAdapter = new ProfileSettingsAdapter(profileItems);
        profileSettingsRecyclerView.setAdapter(profileAdapter);

        // on click listener for profile settings recyclerview
        profileAdapter.setOnItemClickListener(position -> {
            if (position == 0) {
                // Edit Profile
                // Navigate to Edit Profile Fragment
                Toast.makeText(getContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
            } else if (position == 1) {
                // Reselect Preferences
                // Navigate to Reselect Preferences Fragment
                Toast.makeText(getContext(), "Reselect Preferences", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView appSettingsRecyclerView = view.findViewById(R.id.appSettingsRecyclerView);
        appSettingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<AppSettingsItem> appItems = new ArrayList<>();
        appItems.add(new AppSettingsItem(R.drawable.baseline_comment_24, "Contact Helpdesk"));
        appItems.add(new AppSettingsItem(R.drawable.baseline_dark_mode_24, "Dark Mode"));
        appItems.add(new AppSettingsItem(R.drawable.baseline_format_align_justify_24, "Terms & Conditions"));
        appItems.add(new AppSettingsItem(R.drawable.appicon, "About MealCompass"));

        AppSettingsAdapter appAdapter = new AppSettingsAdapter(appItems);

        //on click listener for app settings recyclerview
        appAdapter.setOnItemClickListener(position -> {
            if (position == 0) {
                // Contact Helpdesk
                // Navigate to Contact Helpdesk Fragment
                Toast.makeText(getContext(), "Contact Helpdesk", Toast.LENGTH_SHORT).show();
            } else if (position == 1) {
                // Dark Mode
                // Toggle Dark Mode
                Toast.makeText(getContext(), "Dark Mode", Toast.LENGTH_SHORT).show();
            } else if (position == 2) {
                // Terms & Conditions
                // Navigate to Terms & Conditions Fragment
                NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_termsAndConditionsFragment);

            } else if (position == 3) {
                // About MealCompass
                // Navigate to About MealCompass Fragment
                NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_aboutFragment);
            }
        });


        appSettingsRecyclerView.setAdapter(appAdapter);




    }
}