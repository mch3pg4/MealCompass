package com.example.mealcompass.Discover;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.databinding.FragmentAddDiscoverArticleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class AddDiscoverArticleFragment extends Fragment {
    private FragmentAddDiscoverArticleBinding binding;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private DiscoverRepository discoverRepository;
    private Uri discoverImageUri;

    private FirebaseAuth mAuth;
    private UserRepository userRepository;

    private String authorName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        discoverRepository = new DiscoverRepository();

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);


        // Initialize the ActivityResultLauncher
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), result -> {
            if (result != null) {
                // Get the URI of the selected image
                Log.d("AddDiscoverArticleFragment", "Image URI: " + result);

                discoverImageUri = result;
                binding.itemImagePreview.setImageURI(discoverImageUri);
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddDiscoverArticleBinding.inflate(inflater, container, false);
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

        // get author name
        userRepository.getUserName(userId).addOnSuccessListener(name->
                authorName = name);

        // Check if articleId received from  the previous fragment
        String articleId;
        if (getArguments() != null) {
            articleId = getArguments().getString("articleId");
        } else {
            articleId = null;
        }

        // If articleId is not null, admin is editing article
        if (articleId != null) {
            // Load the existing article data
            loadArticleData(articleId);
            // change the titles to "Update"
            binding.addArticleTitle.setText(R.string.update_article);
            binding.postButton.setText(R.string.update);
        }

        // open gallery
        binding.addArticleImageButton.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        binding.postButton.setOnClickListener(v -> {
            String title = Objects.requireNonNull(binding.articleTitleEditText.getText()).toString();
            String content = Objects.requireNonNull(binding.articleContentEditText.getText()).toString();
            String author = "Admin - " + authorName ;
            // get current date and time
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", new Locale("en", "US"));
            String time = sdf.format(calendar.getTime());

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (articleId != null) {
                    // Update the existing article
                    updateDiscoverArticle(articleId, title, content, discoverImageUri, author, time);
                    return;
                } else { uploadDiscoverArticle(discoverImageUri, title, content, time, author);}

                //grey out the button to prevent multiple clicks
                binding.postButton.setEnabled(false);
                binding.postButton.setAlpha(0.5f);
            }
        });
    }

    private void loadArticleData(String articleId) {
        discoverRepository.getDiscoverById(articleId, new DiscoverRepository.DiscoverCallback() {
            @Override
            public void onSuccess(Discover discover) {
                // Populate the fields with the existing article data
                binding.articleTitleEditText.setText(discover.getArticleTitle());
                binding.articleContentEditText.setText(discover.getArticleContent());
                // Load the image using Glide or another image loading library
                String imageUrl = discover.getArticleImageUrl();
                Glide.with(requireContext()).load(imageUrl).into(binding.itemImagePreview);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Failed to load article", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void updateDiscoverArticle(String articleId, String title, String content, Uri discoverImageUri, String author, String time) {
        if (discoverImageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("discover_images/" + title + ".jpg");
            storageReference.putFile(discoverImageUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        discoverRepository.updateDiscover(articleId, title, content, downloadUrl, author, time)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(requireContext(), "Article updated successfully", Toast.LENGTH_SHORT).show();
                                        NavHostFragment.findNavController(AddDiscoverArticleFragment.this)
                                                .navigate(R.id.action_addDiscoverArticleFragment_to_discoverFragment);
                                    } else {
                                        Toast.makeText(requireContext(), "Failed to update article", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }))
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show());
        } else {
            // Owner must upload a restaurant image
            Toast.makeText(getContext(), "Please upload an image for your article", Toast.LENGTH_SHORT).show();
        }
    }



    private void uploadDiscoverArticle(Uri discoverImageUri, String title, String content, String time, String author) {
        if (discoverImageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("discover_images/" + title + ".jpg");
            storageReference.putFile(discoverImageUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        discoverRepository.addDiscover(title, content, downloadUrl, author, time)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(requireContext(), "Article posted successfully", Toast.LENGTH_SHORT).show();
                                        NavHostFragment.findNavController(AddDiscoverArticleFragment.this)
                                                .navigate(R.id.action_addDiscoverArticleFragment_to_discoverFragment);
                                    } else {
                                        Toast.makeText(requireContext(), "Failed to post article", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }));
        } else {
            // owner must upload a restaurant image
            Toast.makeText(getContext(), "Please upload an image for your article", Toast.LENGTH_SHORT).show();
        }
    }
}