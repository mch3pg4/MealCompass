package com.example.mealcompass.Register.SelectAllergy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.User.UserViewModel;
import com.example.mealcompass.databinding.FragmentSelectAllergyBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class SelectAllergyFragment extends Fragment {
    private FragmentSelectAllergyBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserRepository userRepository;
    private UserViewModel userViewModel;

    private final List<String> selectedAllergens = new ArrayList<>();

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
        binding = FragmentSelectAllergyBinding.inflate(inflater, container, false);
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

        binding.progressIndicator.setProgress(90);


        //create list of items for the listview
        ArrayList<SelectAllergyItem> allergyItemArrayList = new ArrayList<>();
        allergyItemArrayList.add(new SelectAllergyItem("Dairy", R.drawable.dairy, false));
        allergyItemArrayList.add(new SelectAllergyItem("Soybean", R.drawable.soybean, false));
        allergyItemArrayList.add(new SelectAllergyItem("Nut", R.drawable.nut, false));
        allergyItemArrayList.add(new SelectAllergyItem("Seafood", R.drawable.seafood, false));
        allergyItemArrayList.add(new SelectAllergyItem("Wheat", R.drawable.wheat, false));

        SelectAllergyAdapter adapter = new SelectAllergyAdapter(requireContext(), allergyItemArrayList);
        binding.allergyList.setAdapter(adapter);


        binding.doneButton.setOnClickListener(v -> {
            for (SelectAllergyItem item : allergyItemArrayList) {
                if (item.isChecked()) {
                    selectedAllergens.add(item.getAllergyName());
                }
            }

                updateUserAllergies(selectedAllergens);
        });


        binding.prevButton.setOnClickListener(v -> {
            // Navigate to the previous fragment
            NavHostFragment.findNavController(SelectAllergyFragment.this)
                    .navigate(R.id.action_selectAllergyFragment_to_selectDietFragment);
        });

    }

    private void updateUserAllergies(List<String> allergens) {
        String userId = userViewModel.getUserId().getValue();
        if (userId != null) {
            userRepository.updateUserAllergens(userId, allergens)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Allergies selection successful", Toast.LENGTH_SHORT).show();
                            // Navigate to the next fragment
                            NavHostFragment.findNavController(SelectAllergyFragment.this)
                                    .navigate(R.id.action_selectAllergyFragment_to_onboardingFragment);
                        } else {
                            // Show an error message
                            Toast.makeText(requireContext(), "Allergies selection failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        }


    }

}