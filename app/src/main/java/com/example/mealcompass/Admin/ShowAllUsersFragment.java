package com.example.mealcompass.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.User.User;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentShowAllUsersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class ShowAllUsersFragment extends Fragment {
    private FragmentShowAllUsersBinding binding;
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
        binding = FragmentShowAllUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = null;
        if (user != null) {
            user.getUid();
        }

        // users list recycler view
        RecyclerView usersListRecyclerView = binding.showAllUsersRecyclerView;
        usersListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialize viewmodel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // get all users
        userViewModel.getAllUsers();

        // Observe the LiveData for changes
        userViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), users -> {
            if (users != null && !users.isEmpty()) {
                // Populate the RecyclerView with the fetched users
                List<UserListItem> usersListItems = new ArrayList<>();
                for (User userList : users) {
                    usersListItems.add(new UserListItem(
                            userList.getUserName(),
                            userList.getUserId()));
                }
                UserListAdapter usersListAdapter = new UserListAdapter(usersListItems, userRepository, getContext());
                usersListRecyclerView.setAdapter(usersListAdapter);
            } else {
                Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}