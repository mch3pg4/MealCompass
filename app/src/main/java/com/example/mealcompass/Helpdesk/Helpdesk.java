package com.example.mealcompass.Helpdesk;

public class Helpdesk {
    private String chatId;
    private String senderId;
    private String receiverId;
    private String message;
    private String sendTime;

    public Helpdesk() {
    }

    public Helpdesk(String senderId) {
        this.senderId = senderId;
    }

    public Helpdesk(String senderId, String receiverId, String message, String sendTime) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.sendTime = sendTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getChatId() {
        return chatId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void setChatId(String id) {
    }
}
