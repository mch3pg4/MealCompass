package com.example.mealcompass.Helpdesk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HelpdeskViewModel extends ViewModel {
    private final HelpdeskRepository helpdeskRepository;
    private final MutableLiveData<List<Helpdesk>> helpdeskChatListLiveData = new MutableLiveData<>();


    public HelpdeskViewModel() {
        helpdeskRepository = new HelpdeskRepository();
    }

    public LiveData<List<Helpdesk>> getHelpdeskChatListLiveData() {
        return helpdeskChatListLiveData;
    }

    // fetch all chats
    public void fetchAllChats() {
        helpdeskRepository.getAllChats(new HelpdeskRepository.HelpdeskChatListCallback() {
            @Override
            public void onSuccess(List<Helpdesk> helpdeskChatList) {
                helpdeskChatListLiveData.setValue(helpdeskChatList);
            }

            @Override
            public void onFailure(Exception e) {
                // Handle the error appropriately

            }
        });
    }

    public void fetchMessages(String chatId) {
        helpdeskRepository.fetchMessages(chatId);
    }

    public void sendMessage(String chatId, Helpdesk message) {
        helpdeskRepository.sendMessage(chatId, message);
    }



}
