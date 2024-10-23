package com.example.mealcompass.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
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
import com.example.mealcompass.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

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
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        String userId;
        if (user != null) {
            userId = user.getUid();
        } else {
            userId = null;
        }

        // load user name and profile image
        // set up profile image
        userRepository.loadUserProfileImage(userId, binding.profileImage, requireContext());
        userRepository.getUserName(userId).addOnSuccessListener(name -> binding.profileText.setText(name));


        RecyclerView profileSettingsRecyclerView = view.findViewById(R.id.profileSettingsRecyclerView);
        profileSettingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ProfileSettingsItem> profileItems = new ArrayList<>();
        profileItems.add(new ProfileSettingsItem(R.drawable.baseline_account_circle_24, "Edit Profile"));

        // if user role is admin or owner, then don't need to show reselect preferences
        userRepository.getUserType(userId).addOnSuccessListener(userType -> {
            if (!userType.equals("admin") && !userType.equals("owner")) {
        profileItems.add(new ProfileSettingsItem(R.drawable.baseline_tune_24, "Reselect Preferences"));
            }
        });

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

        appItems.add(new AppSettingsItem(R.drawable.baseline_dark_mode_24, "Dark Mode"));
        appItems.add(new AppSettingsItem(R.drawable.baseline_format_align_justify_24, "Terms & Conditions"));
        appItems.add(new AppSettingsItem(R.drawable.appicon, "About MealCompass"));
        // if user role is not admin, then don't need to show contact helpdesk
        userRepository.getUserType(userId).addOnSuccessListener(userType -> {
            if (userType.equals("admin")) {
                appItems.add(new AppSettingsItem(R.drawable.baseline_person_add_24, "Add Admin"));
            } else {
                appItems.add(new AppSettingsItem(R.drawable.baseline_comment_24, "Contact Helpdesk"));
            }
        });

        AppSettingsAdapter appAdapter = new AppSettingsAdapter(appItems);

        //on click listener for app settings recyclerview
        appAdapter.setOnItemClickListener(position -> {
            if (position == 3) {
                // Contact Helpdesk
                // Navigate to Contact Helpdesk Fragment only if user is not admin
                userRepository.getUserType(userId).addOnSuccessListener(userType -> {
                    if (!userType.equals("admin")) {
                        NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_helpdeskFragment);
                    } else {
                        // navigate to add admin fragment
                        NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_createAdminFragment);
                    }
                });

            } else if (position == 0) {
                // Dark Mode
                // Toggle Dark Mode
                showDarkModeAlertDialog();
            } else if (position == 1) {
                // Terms & Conditions
                // Navigate to Terms & Conditions Fragment
                NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_termsAndConditionsFragment);
            } else if (position == 2) {
                // About MealCompass
                // Navigate to About MealCompass Fragment
                NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_aboutFragment);
            }
        });


        appSettingsRecyclerView.setAdapter(appAdapter);
    }

    private void showDarkModeAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Dark Mode");

        // options for enabling and disabling dark mode
        String[] options = {"Enable Dark Mode", "Disable Dark Mode"};

        builder.setSingleChoiceItems(options, -1, (dialog, selected) -> {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (selected == 0) {
                // Enable Dark Mode
                editor.putBoolean("isDarkModeEnabled", true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(requireContext(), "Dark Mode Enabled", Toast.LENGTH_SHORT).show();
            } else if (selected == 1) {
                // Disable Dark Mode
                editor.putBoolean("isDarkModeEnabled", false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(requireContext(), "Dark Mode Disabled", Toast.LENGTH_SHORT).show();
            }
            editor.apply();
            dialog.dismiss();
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}