package com.example.mealcompass.Profile;

public class ProfileSettingsItem {
    private final int iconResId;
    private final String title;

    public ProfileSettingsItem(int iconResId, String title) {
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


