package com.example.mealcompass.Register.SelectDiet;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentSelectDietBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class SelectDietFragment extends Fragment {

    private FragmentSelectDietBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserRepository userRepository;
    private UserViewModel userViewModel;

    private final List<String> selectedDiets = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSelectDietBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = null;
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
        }

        userViewModel.setUserId(userId);

        binding.progressIndicator.setProgress(70);

        //create list of items for the listview
        ArrayList<SelectDietItem> dietItemArrayList = new ArrayList<>();
        dietItemArrayList.add(new SelectDietItem("Vegetarian", false));
        dietItemArrayList.add(new SelectDietItem("Halal", false));
        dietItemArrayList.add(new SelectDietItem("Low carb", false));
        dietItemArrayList.add(new SelectDietItem("Gluten-free", false));
        dietItemArrayList.add(new SelectDietItem("Dairy-free", false));
        dietItemArrayList.add(new SelectDietItem("Keto", false));

        // find listview
        SelectDietAdapter adapter = new SelectDietAdapter(requireContext(), dietItemArrayList);
        binding.dietList.setAdapter(adapter);

        binding.nextButton.setOnClickListener(v -> {
            for (SelectDietItem item : dietItemArrayList) {
                if (item.isChecked()) {
                    selectedDiets.add(item.getDietName());
                }
            }
            // Update the user's diets
            updateUserDiet(selectedDiets);
        });

        binding.prevButton.setOnClickListener(v -> {
            // Navigate to the previous fragment
            NavHostFragment.findNavController(SelectDietFragment.this)
                    .navigate(R.id.action_selectDietFragment_to_selectCuisineFragment2);
        });

    }

    private void updateUserDiet(List<String> diets) {

        String userId = userViewModel.getUserId().getValue();
        if (userId != null) {
            userRepository.updateUserDiets(userId, diets)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Diets selection unsuccessful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Diets selection successful", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(SelectDietFragment.this)
                                    .navigate(R.id.action_selectDietFragment_to_selectAllergyFragment);
                        }
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}