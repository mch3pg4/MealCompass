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
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentDiscoverBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class DiscoverFragment extends Fragment {

    private FragmentDiscoverBinding binding;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private UserViewModel userViewModel;
    private boolean isAdmin;

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
        binding = FragmentDiscoverBinding.inflate(inflater, container, false);
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

        // check if user is admin
        userRepository.getUserType(userId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userType = task.getResult();
                isAdmin = userType.equals("admin");
                // only if user type is admin show add article button
                if (isAdmin) {
                    binding.addFab.setVisibility(View.VISIBLE);
                } else {
                    binding.addFab.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(getContext(), "Failed to get user type", Toast.LENGTH_SHORT).show();
            }


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
                            discover.getArticleId(),
                            discover.getArticleImageUrl(),
                            discover.getArticleAuthor(),
                            discover.getArticleTime(),
                            discover.getArticleTitle(),
                            discover.getArticleContent()));
                }
                DiscoverAdapter discoverAdapter = new DiscoverAdapter(discoverItems, isAdmin);
                discoverRecyclerView.setAdapter(discoverAdapter);
            } else {
                Toast.makeText(getContext(), "No articles found", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch all discover articles
        discoverViewModel.fetchAllDiscover();

        });



        binding.addFab.setOnClickListener(v -> {
            NavHostFragment.findNavController(DiscoverFragment.this)
                    .navigate(R.id.action_discoverFragment_to_addDiscoverArticleFragment);
            Toast.makeText(getContext(), "Add a new article", Toast.LENGTH_SHORT).show();
        });

        binding.profileImageButton.setOnClickListener(
                v -> NavHostFragment.findNavController(DiscoverFragment.this)
                        .navigate(R.id.action_discoverFragment_to_profileFragment));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}