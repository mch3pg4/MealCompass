package com.example.mealcompass.Helpdesk;

public class HelpdeskChatsItem {
    private final String senderId;
    private final String chatImage;
    private final String chatName, chatMessage;
    private final String chatTime;
    private final String chatId;

    public HelpdeskChatsItem(String senderId, String chatImage, String chatName, String chatMessage, String chatTime, String chatId) {
        this.senderId = senderId;
        this.chatImage = chatImage;
        this.chatName = chatName;
        this.chatMessage = chatMessage;
        this.chatTime = chatTime;
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public String getChatTime() {
        return chatTime;
    }

    public String getSenderId() {
        return senderId;

    }

    public String getChatImage() {
            return chatImage;
        }

    public String getChatName() {
            return chatName;
        }

    public String getChatMessage() {
            return chatMessage;
        }



}
