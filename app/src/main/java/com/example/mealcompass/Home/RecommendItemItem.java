package com.example.mealcompass.Home;

import java.util.List;

public class RecommendItemItem {
    private final String itemImage;
    private final String itemName;
    private final int itemPrice;
    private final String itemCategory;
    private final String itemDescription;
    private final List<String> itemAllergens;
    private final int itemNutritionalValue;



    public RecommendItemItem(String itemImage, String itemName, int itemPrice, String itemCategory, String itemDescription, List<String> itemAllergens, int itemNutritionalValue) {
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;
        this.itemAllergens = itemAllergens;
        this.itemNutritionalValue = itemNutritionalValue;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public List<String> getItemAllergens() {
        return itemAllergens;
    }

    public int getItemNutritionalValue() {
        return itemNutritionalValue;
    }



}
