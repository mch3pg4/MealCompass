package com.example.mealcompass.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentEditProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        String userId;
        if (user != null) {
            userId = user.getUid();
        } else {
            userId = null;
        }

        userViewModel.getUserById(userId);
        binding.nameTextInputLayout.setPlaceholderText(userViewModel.getUserName().getValue());

        binding.editProfileButton.setOnClickListener(
                // if user changed the name, update the name
                v -> {
                    String newName = binding.nameTextInputLayout.getEditText().getText().toString();
                    if (!newName.equals(userViewModel.getUserName().getValue())) {
                        userViewModel.updateUserName(userId, newName);
                        NavHostFragment.findNavController(this).navigate(R.id.action_editProfileFragment_to_profileFragment);
                    } else if (newName.equals("")) {
                        binding.nameTextInputLayout.setError("Name cannot be empty");
                    }

                }

        );



    }
}