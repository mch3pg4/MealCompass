package com.example.mealcompass.Discover;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentDiscoverBinding;

import java.util.ArrayList;
import java.util.List;


public class DiscoverFragment extends Fragment {

    private FragmentDiscoverBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentDiscoverBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        RecyclerView discoverRecyclerView = view.findViewById(R.id.discoverRecyclerView);
        discoverRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<DiscoverItem> discoverItems = new ArrayList<>();
        discoverItems.add(new DiscoverItem(R.drawable.discover_img, "John Doe", "2 hours ago", "The Best Restaurants in New York City", "New York City is home to some of the best restaurants in the world. Here are some of the best restaurants in New York City that you must visit."));
        discoverItems.add(new DiscoverItem(R.drawable.discover_img, "John Doe", "2 hours ago", "The Best Restaurants in New York City", "New York City is home to some of the best restaurants in the world. Here are some of the best restaurants in New York City that you must visit."));
        discoverItems.add(new DiscoverItem(R.drawable.discover_img, "John Doe", "2 hours ago", "The Best Restaurants in New York City", "New York City is home to some of the best restaurants in the world. Here are some of the best restaurants in New York City that you must visit."));

        DiscoverAdapter discoverAdapter = new DiscoverAdapter(discoverItems);
        discoverRecyclerView.setAdapter(discoverAdapter);



        binding.profileImageButton.setOnClickListener(
                v -> NavHostFragment.findNavController(DiscoverFragment.this)
                        .navigate(R.id.action_discoverFragment_to_profileFragment));

    }
}