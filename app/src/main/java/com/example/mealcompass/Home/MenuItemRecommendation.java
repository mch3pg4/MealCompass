package com.example.mealcompass.Home;

import com.google.gson.annotations.SerializedName;

public class MenuItemRecommendation {
    @SerializedName("itemName")
    private String itemName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

