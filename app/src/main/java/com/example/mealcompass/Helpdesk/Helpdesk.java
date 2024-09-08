package com.example.mealcompass.Helpdesk;

import java.util.Date;

public class Helpdesk {
    private String chatId;
    private String senderId;
    private String receiverId;
    private String message;
    private Date sendTime;

    public Helpdesk() {
    }

    public Helpdesk(String senderId) {
        this.senderId = senderId;
    }

    public Helpdesk(String chatId, String senderId, String receiverId, String message, Date sendTime) {
        this.chatId = chatId;
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

    public Date getSendTime() {
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

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public void setChatId(String id) {
        this.chatId = id;
    }
}
