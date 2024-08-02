package com.example.mealcompass.Home;

public class RecommendItemItem {
    private final int itemImage;
    private final String itemName;

    public RecommendItemItem(int itemImage, String itemName) {
        this.itemImage = itemImage;
        this.itemName = itemName;
    }

    public int getItemImage() {
        return itemImage;
    }

    public String getItemName() {
        return itemName;
    }

}
