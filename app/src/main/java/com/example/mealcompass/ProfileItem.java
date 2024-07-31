package com.example.mealcompass;

public class ProfileItem {
    private final int iconResId;
    private final String title;

    public ProfileItem(int iconResId, String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    public int getIcon() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }
}
