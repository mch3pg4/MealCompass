package com.example.mealcompass.Discover;

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

        // initialize viewmodel
        DiscoverViewModel discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);

        // Observe the LiveData for changes
        discoverViewModel.getDiscoverListLiveData().observe(getViewLifecycleOwner(), discovers -> {
            if (discovers != null && !discovers.isEmpty()) {
                // Populate the RecyclerView with the fetched articles
                List<DiscoverItem> discoverItems = new ArrayList<>();
                for (Discover discover : discovers) {
                    discoverItems.add(new DiscoverItem(
                            discover.getArticleImageUrl(),
                            discover.getArticleAuthor(),
                            discover.getArticleTime(),
                            discover.getArticleTitle(),
                            discover.getArticleContent()));
                }
                DiscoverAdapter discoverAdapter = new DiscoverAdapter(discoverItems);
                discoverRecyclerView.setAdapter(discoverAdapter);
            }
        });

        // Fetch all discover articles
        discoverViewModel.fetchAllDiscover();


        binding.addFab.setOnClickListener(
                v-> Toast.makeText(getContext(), "Add a new article", Toast.LENGTH_SHORT).show()
        );



        binding.profileImageButton.setOnClickListener(
                v -> NavHostFragment.findNavController(DiscoverFragment.this)
                        .navigate(R.id.action_discoverFragment_to_profileFragment));

    }
}