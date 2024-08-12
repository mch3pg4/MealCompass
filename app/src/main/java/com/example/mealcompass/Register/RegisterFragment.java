package com.example.mealcompass.Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Make "Terms and Conditions" clickable and underlined
        String fullText = "I accept the Terms and Conditions";
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("Terms and Conditions");
        int endIndex = startIndex + "Terms and Conditions".length();

        // Apply underline to "Terms and Conditions"
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Make "Terms and Conditions" clickable
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Navigate to the Terms and Conditions page
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_registerFragment_to_termsAndConditionsFragment);
            }
        };
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.acceptTermsText.setText(spannableString);
        binding.acceptTermsText.setMovementMethod(LinkMovementMethod.getInstance());

        binding.acceptTermsCheckBox.setOnClickListener(v -> binding.registerButton.setEnabled(binding.acceptTermsCheckBox.isChecked()));

        // if register button is disabled and tried to pressed, show toast message
        binding.registerButton.setOnClickListener(v -> {
            if (!binding.registerButton.isEnabled()) {
                Toast.makeText(getContext(), "Please accept the Terms and Conditions first", Toast.LENGTH_SHORT).show();
            } else {
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_registerFragment_to_selectRoleFragment2);
            }
        });
    }
}