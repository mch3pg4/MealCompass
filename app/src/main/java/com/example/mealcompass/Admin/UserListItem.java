package com.example.mealcompass.Admin;

public class UserListItem {

    private final String userName;
    private final String userId;

    public UserListItem(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

}
