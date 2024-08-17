package com.example.mealcompass.Admin;

public class UserListItem {

    private final String userName;
    private final int userProfileImage;

    public UserListItem(String userName, int userProfileImage) {
        this.userName = userName;
        this.userProfileImage = userProfileImage;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserProfileImage() {
        return userProfileImage;
    }

}
