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

import java.util.ArrayList;
import java.util.List;

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
                Toast.makeText(getContext(), "Helpdesk chats found", Toast.LENGTH_SHORT).show();
                List<HelpdeskChatsItem> helpdeskChatsItems = new ArrayList<>();
                // Populate recyclerview
                for (Helpdesk helpdesk : helpdeskChatList) {
                    String senderId = helpdesk.getSenderId();
                    // Get user name and profile picture
                    userRepository.getUserName(senderId).addOnSuccessListener(name -> {
                        String senderName = name;

                        helpdeskChatsItems.add(new HelpdeskChatsItem(
                                senderId,
                                senderId,// senderId for profile picture
                                senderName, // senderId name
                                helpdesk.getMessage(),// last message
                                helpdesk.getSendTime(),
                                helpdesk.getChatId()
                        ));

                    });
                }
                HelpdeskChatsAdapter helpdeskChatsAdapter = new HelpdeskChatsAdapter(helpdeskChatsItems, userRepository, requireContext());
                helpdeskChatsRecyclerView.setAdapter(helpdeskChatsAdapter);
            } else {
                Toast.makeText(getContext(), "No helpdesk chats found", Toast.LENGTH_SHORT).show();
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
