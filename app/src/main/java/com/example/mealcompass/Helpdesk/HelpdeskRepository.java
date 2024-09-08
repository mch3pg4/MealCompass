package com.example.mealcompass.Helpdesk;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.mealcompass.Discover.Discover;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HelpdeskRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Helpdesk>> helpdeskLiveData;

    public HelpdeskRepository() {
        helpdeskLiveData = new MutableLiveData<>();
    }

    public interface OnChatIdResultListener {
        void onChatIdRetrieved(String chatId);
        void onError(Exception e);
    }

    public interface HelpdeskChatListCallback {
        void onSuccess(List<Helpdesk> helpdeskList);
        void onFailure(Exception e);
    }

    public void getAllChats(HelpdeskChatListCallback callback) {
        db.collection("helpdesk")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Helpdesk> helpdeskList = new ArrayList<>();

                        // Loop through each chat document (representing different users)
                        for (DocumentSnapshot chatDocument : task.getResult()) {
                            String chatId = chatDocument.getId();

                            // Access the senderId field (if needed)
                            String senderId = chatDocument.getString("senderId");

                            // Access the messages collection inside each chat document and get the latest message
                            db.collection("helpdesk")
                                    .document(chatId)
                                    .collection("messages")
                                    .orderBy("sendTime", Query.Direction.DESCENDING) // Get latest message by time
                                    .limit(1) // Fetch only the most recent message
                                    .get()
                                    .addOnCompleteListener(messageTask -> {
                                        if (messageTask.isSuccessful()) {
                                            for (DocumentSnapshot messageDocument : messageTask.getResult()) {
                                                Helpdesk helpdesk = messageDocument.toObject(Helpdesk.class);
                                                if (helpdesk != null) {
                                                    helpdesk.setChatId(chatId); // Add chatId to the helpdesk object
                                                    helpdesk.setSenderId(senderId); // Add senderId to the helpdesk object
                                                    helpdesk.setMessage(messageDocument.getString("message"));
                                                    // Get the sendTime and convert it
                                                    Timestamp timestamp = messageDocument.getTimestamp("sendTime");
                                                    if (timestamp != null) {
                                                        Date sendTime = timestamp.toDate();
                                                        helpdesk.setSendTime(sendTime);
                                                    }

                                                    // Add to the helpdesk list
                                                    helpdeskList.add(helpdesk);
                                                }
                                            }

                                            // Send the results via the callback after fetching the latest message for each chat
                                            if (!helpdeskList.isEmpty()) {
                                                callback.onSuccess(helpdeskList);
                                                Log.d("HelpdeskRepository", "onSuccess called with " + helpdeskList.size() + " items.");
                                            }
                                        } else {
                                            callback.onFailure(messageTask.getException());
                                            Log.d("HelpdeskRepository", "onFailure called.", messageTask.getException());
                                        }
                                    });
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }


    // fetch messages from firebase
    public void fetchMessages(String chatId) {
        db.collection("helpdesk")
                .document(chatId)
                .collection("messages")
                .orderBy("sendTime", Query.Direction.ASCENDING)
                .addSnapshotListener((queryDocumentSnapshots, error) -> {
                    if (error != null) {
                        return;
                    }
                    List<Helpdesk> messages = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Helpdesk message = document.toObject(Helpdesk.class);
                        messages.add(message);
                    }
                    helpdeskLiveData.setValue(messages);
                });
    }

    // check if chatId exists
    public void checkChatId(String userId, OnChatIdResultListener listener) {
        db.collection("helpdesk")
                .whereEqualTo("senderId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // chat exists
                        listener.onChatIdRetrieved(task.getResult().getDocuments().get(0).getId());
                    } else {
                        createChat(userId, listener);
                    }
                })
                .addOnFailureListener(listener::onError);
    }

    // create new chat to get chatId
    public void createChat(String userId, OnChatIdResultListener listener) {
        db.collection("helpdesk")
                .add(new Helpdesk(userId))
                .addOnSuccessListener(documentReference -> listener.onChatIdRetrieved(documentReference.getId()))
                .addOnFailureListener(listener::onError);
    }

    // send message
    public void sendMessage(String chatId, Helpdesk message) {
        db.collection("helpdesk")
                .document(chatId)
                .collection("messages")
                .add(message)
                .addOnSuccessListener(
                        documentReference -> {
                            // Log.d("HelpdeskRepository", "DocumentSnapshot added with ID: " + documentReference.getId());
                        })
                .addOnFailureListener(e -> {
                    // Log.d("HelpdeskRepository", "Error adding document", e);
                });

    }


    public MutableLiveData<List<Helpdesk>> getHelpdeskLiveData() {
        return helpdeskLiveData;
    }


}
