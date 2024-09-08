package com.example.mealcompass.Helpdesk;

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

import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.databinding.FragmentHelpdeskAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HelpdeskAdminFragment extends Fragment {
    private FragmentHelpdeskAdminBinding binding;
    private UserRepository userRepository;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHelpdeskAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView helpdeskChatsRecyclerView = binding.helpdeskChatsRecyclerView;
        helpdeskChatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize viewmodel
        HelpdeskViewModel helpdeskViewModel = new ViewModelProvider(this).get(HelpdeskViewModel.class);

        // Observe live data from viewmodel
        helpdeskViewModel.getHelpdeskChatListLiveData().observe(getViewLifecycleOwner(), helpdeskChatList -> {
            if (helpdeskChatList != null && !helpdeskChatList.isEmpty()) {
                List<HelpdeskChatsItem> helpdeskChatsItems = new ArrayList<>();

                // Total number of chats to process
                int totalChats = helpdeskChatList.size();
                final int[] processedChats = {0}; // Track the number of processed chats

                for (Helpdesk helpdesk : helpdeskChatList) {
                    String senderId = helpdesk.getSenderId();

                    // Get user name and profile picture
                    userRepository.getUserName(senderId).addOnSuccessListener(name -> {
                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);

                        // Add the item to the list
                        helpdeskChatsItems.add(new HelpdeskChatsItem(
                                senderId,
                                senderId, // senderId for profile picture
                                name, // senderId name
                                helpdesk.getMessage(), // last message
                                sdf.format(helpdesk.getSendTime()), // last message time
                                helpdesk.getChatId()
                        ));

                        // Increment the processed chats count
                        processedChats[0]++;

                        // Only notify the adapter when all chats have been processed
                        if (processedChats[0] == totalChats) {
                            HelpdeskChatsAdapter helpdeskChatsAdapter = new HelpdeskChatsAdapter(helpdeskChatsItems, userRepository, requireContext());
                            helpdeskChatsRecyclerView.setAdapter(helpdeskChatsAdapter);
                        }

                    }).addOnFailureListener(e -> {
                        processedChats[0]++; // Even on failure, increment the count to avoid locking
                        if (processedChats[0] == totalChats) {
                            HelpdeskChatsAdapter helpdeskChatsAdapter = new HelpdeskChatsAdapter(helpdeskChatsItems, userRepository, requireContext());
                            helpdeskChatsRecyclerView.setAdapter(helpdeskChatsAdapter);
                        }
                    });
                }
            } else {
                Toast.makeText(getContext(), "No chats found", Toast.LENGTH_SHORT).show();
            }
        });

        helpdeskViewModel.fetchAllChats();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
