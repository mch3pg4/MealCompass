package com.example.mealcompass.Discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.databinding.FragmentDiscoverArticleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class DiscoverArticleFragment extends Fragment {
    private FragmentDiscoverArticleBinding binding;
    private boolean isAdmin;
    private UserRepository userRepository;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);

       
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDiscoverArticleBinding.inflate(inflater, container, false);
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

        // if usertype is admin then show edit fab
        // check if user is admin
        userRepository.getUserType(userId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userType = task.getResult();
                isAdmin = userType.equals("admin");
                // only if user type is admin show edit fab button
                if (isAdmin) {
                    binding.editFab.setVisibility(View.VISIBLE);
                } else {
                    binding.editFab.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(getContext(), "Failed to get user type", Toast.LENGTH_SHORT).show();
            }
        });

        // Retrieve data from previous fragment
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("discoverArticleTitle");
            String author = args.getString("authorName");
            String content = args.getString("discoverArticleDescription");
            String imageUrl = args.getString("discoverImage");

            //load data into views
            binding.discoverArticleTitle.setText(title);
            binding.discoverArticleContent.setText(content);
            binding.discoverArticleAuthor.setText(String.format("Posted by: %s", author));
            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.discoverArticleImage);

        }

        binding.editFab.setOnClickListener(v -> {
            // pass article id to EditDiscoverArticleFragment
            Bundle bundle = new Bundle();
            assert args != null;
            bundle.putString("articleId", args.getString("discoverId"));
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_discoverArticleFragment_to_addDiscoverArticleFragment, bundle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}