package com.example.mealcompass.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentSelectRoleBinding;


public class SelectRoleFragment extends Fragment {
    private FragmentSelectRoleBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_role, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set next button to not clickable first
        binding.nextButton.setClickable(false);

        binding.progressIndicator.setProgress(1);

        if (binding.userRoleCard.isChecked()){
            // Toast message to say user role selected
            Toast.makeText(getContext(), "User role selected", Toast.LENGTH_SHORT).show();
            binding.nextButton.setClickable(true);
        } else if (binding.adminRoleCard.isChecked()){
            // Toast message to say chef role selected
            Toast.makeText(getContext(), "Admin  role selected", Toast.LENGTH_SHORT).show();
            binding.nextButton.setClickable(true);
        } else if (binding.ownerRoleCard.isChecked()){
            // Toast message to say chef role selected
            Toast.makeText(getContext(), "Restaurant Owner  role selected", Toast.LENGTH_SHORT).show();
            binding.nextButton.setClickable(true);
        }

        // if roles have been selected navigate to the next fragment
        if (binding.userRoleCard.isChecked()){
            // Navigate to the user registration fragment
            NavHostFragment.findNavController(SelectRoleFragment.this)
                    .navigate(R.id.action_selectRoleFragment2_to_selectCuisineFragment2);
        } else if (binding.adminRoleCard.isChecked()){
            // Navigate to the admin  fragment
            NavHostFragment.findNavController(SelectRoleFragment.this)
                    .navigate(R.id.action_selectRoleFragment2_to_adminFragment);
        } else if (binding.ownerRoleCard.isChecked()){
            // Navigate to the restaurant owner registration fragment
            NavHostFragment.findNavController(SelectRoleFragment.this)
                    .navigate(R.id.action_selectRoleFragment2_to_fillInRestDetailsFragment);
        } else if (!binding.userRoleCard.isChecked() && !binding.adminRoleCard.isChecked() && !binding.ownerRoleCard.isChecked()){
            // Toast message to say no role selected
            Toast.makeText(getContext(), "Please select a role to proceed", Toast.LENGTH_SHORT).show();
        }


    }
}