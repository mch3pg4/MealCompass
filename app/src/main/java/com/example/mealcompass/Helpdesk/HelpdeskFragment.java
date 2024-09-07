package com.example.mealcompass.Helpdesk;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mealcompass.R;
import com.example.mealcompass.User.UserRepository;
import com.example.mealcompass.databinding.FragmentHelpdeskBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class HelpdeskFragment extends Fragment {
    private FragmentHelpdeskBinding binding;
    private HelpdeskRepository helpdeskRepository;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private final List<Helpdesk> helpdeskList = new ArrayList<>();
    private HelpdeskAdapter helpdeskAdapter;
    private String chatId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helpdeskRepository = new HelpdeskRepository();
        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository(mAuth, db);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHelpdeskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = mAuth.getCurrentUser();
        String userId;
        if (user != null) {
            userId = user.getUid();
        } else {
            userId = null;
        }

        // get user type
        userRepository.getUserType(userId).addOnSuccessListener(type -> {
            if (type.equals("user") || type.equals("owner")) {
                binding.receiverName.setText(R.string.admin);
            }
        });

        // send button is disabled by default and greyed out
        binding.sendButton.setEnabled(false);
        binding.sendButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_gray));

        // show chat messages
        RecyclerView chatRecyclerView = binding.chatRecyclerview;
        helpdeskAdapter = new HelpdeskAdapter(helpdeskList, userId);
        chatRecyclerView.setAdapter(helpdeskAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // if user is admin, then show the chat pressed from previous fragment
        if (getArguments() != null) {
            chatId = getArguments().getString("chatId");
            listenForMessages(chatId);
            binding.receiverName.setText(getArguments().getString("chatName"));
        } else {
            // if user is not admin, then fetch chat id
            fetchChatId(userId);
        }

        // only when text is entered in the message box, the send button will be enabled
        binding.messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.sendButton.setEnabled(!s.toString().isEmpty());
                if (s.toString().isEmpty()) {
                    binding.sendButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_gray));
                } else {
                    //remove grey background
                    binding.sendButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_purple));
                }
            }
        });

        // Handle send button click
        binding.sendButton.setOnClickListener(v -> {
            if (chatId == null) {
                Toast.makeText(getContext(), "Cannot send message, missing chat ID", Toast.LENGTH_SHORT).show();
                return;
            }

            String messageText = Objects.requireNonNull(binding.messageEditText.getText()).toString();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", new Locale("en", "US"));
            String timestamp = sdf.format(calendar.getTime());

            Helpdesk message = new Helpdesk(userId, chatId, messageText, timestamp);
            helpdeskRepository.sendMessage(chatId, message);
            binding.messageEditText.setText("");
            Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchChatId(String userId) {
        helpdeskRepository.checkChatId(userId, new HelpdeskRepository.OnChatIdResultListener() {
            @Override
            public void onChatIdRetrieved(String retrievedChatId) {
                chatId = retrievedChatId;
                listenForMessages(chatId); // Moved here for once fetching
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Failed to get chat id", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listenForMessages(String chatId) {
        helpdeskRepository.fetchMessages(chatId);
        helpdeskRepository.getHelpdeskLiveData().observe(getViewLifecycleOwner(), messages -> {
            // update the UI with the messages
            helpdeskAdapter.updateMessages(messages);
        });
    }
}