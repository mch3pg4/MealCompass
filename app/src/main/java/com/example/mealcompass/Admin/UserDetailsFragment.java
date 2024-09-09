package com.example.mealcompass.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.User.User;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentUserDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDetailsFragment extends Fragment {
    private FragmentUserDetailsBinding binding;
    private UserRepository userRepository;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userRepository = new UserRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String userId = args.getString("userId");
            String userName = args.getString("userName");
            // Load data
            binding.profileText.setText(userName);
            userRepository.loadUserProfileImage(userId, binding.profileImage, getContext());
            userRepository.getUserById(userId, new UserRepository.UserCallback() {
                @Override
                public void onSuccess(User user) {
                    binding.userEmail.setText(String.format("User Email: %s", user.getUserEmail()));
                    binding.userRole.setText(String.format("User Role: %s", user.getUserType()));

                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }

        binding.deleteUserButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Delete User");
            builder.setMessage("Are you sure you want to delete this user?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
//                userRepository.deleteUser(args.getString("userId"), new UserRepository.UserCallback() {
//                    @Override
//                    public void onSuccess(User user) {
//                        getActivity().onBackPressed();
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//
//                    }
//                });
                Toast.makeText(getContext(), "User deleted", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
        });

    }
}