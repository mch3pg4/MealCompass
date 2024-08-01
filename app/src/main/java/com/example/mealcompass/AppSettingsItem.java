package com.example.mealcompass;

public class AppSettingsItem {
    private final int iconResId;
    private final String title;

    public AppSettingsItem(int iconResId, String title) {
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