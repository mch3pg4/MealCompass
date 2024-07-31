package com.example.mealcompass;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.databinding.FragmentHomeBinding;
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


        RecyclerView recyclerView = view.findViewById(R.id.profileRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ProfileItem> profileItems = new ArrayList<>();
        profileItems.add(new ProfileItem(R.drawable.baseline_account_circle_24, "Edit Profile"));
        profileItems.add(new ProfileItem(R.drawable.baseline_tune_24, "Reselect Preferences"));
        profileItems.add(new ProfileItem(R.drawable.baseline_comment_24, "Contact Helpdesk"));
        profileItems.add(new ProfileItem(R.drawable.baseline_dark_mode_24, "Dark Mode"));
        profileItems.add(new ProfileItem(R.drawable.baseline_format_align_justify_24, "Terms & Conditions"));
        profileItems.add(new ProfileItem(R.drawable.applogo, "About MealCompass"));

        ProfileAdapter profileAdapter = new ProfileAdapter(profileItems);
        recyclerView.setAdapter(profileAdapter);


    }
}