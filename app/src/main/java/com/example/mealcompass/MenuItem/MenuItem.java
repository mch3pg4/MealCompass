package com.example.mealcompass.MenuItem;

import android.view.Menu;

import java.util.List;

public class MenuItem {
    private String menuItemId;
    private  String menuItemImage;
    private  String menuItemName;
    private  String menuItemDescription;
    private  int menuItemPrice;
    private  int menuItemNutritionalValue;
    private  String menuItemCategory;
    private  List<String> menuItemAllergens;
    private boolean isItemBestSeller;

    public MenuItem() {

    }

    public MenuItem(String menuItemId, String menuItemImage, String menuItemName, String menuItemDescription, int menuItemPrice, int menuItemNutritionalValue, String menuItemCategory, List<String> menuItemAllergens, boolean itemBestSeller) {
        this.menuItemId = menuItemId;
        this.menuItemImage = menuItemImage;
        this.menuItemName = menuItemName;
        this.menuItemDescription = menuItemDescription;
        this.menuItemPrice = menuItemPrice;
        this.menuItemNutritionalValue = menuItemNutritionalValue;
        this.menuItemCategory = menuItemCategory;
        this.menuItemAllergens = menuItemAllergens;
        this.isItemBestSeller = itemBestSeller;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemImage() {
        return menuItemImage;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public String getMenuItemDescription() {
        return menuItemDescription;
    }

    public int getMenuItemPrice() {
        return menuItemPrice;
    }

    public int getMenuItemNutritionalValue() {
        return menuItemNutritionalValue;
    }

    public String getMenuItemCategory() {
        return menuItemCategory;
    }

    public List<String> getMenuItemAllergens() {
        return menuItemAllergens;
    }

    public boolean isItemBestSeller() {
        return isItemBestSeller;
    }

    // setters
    public void setMenuItemImage(String menuItemImage) {
        this.menuItemImage = menuItemImage;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public void setMenuItemDescription(String menuItemDescription) {
        this.menuItemDescription = menuItemDescription;
    }

    public void setMenuItemPrice(int menuItemPrice) {
        this.menuItemPrice = menuItemPrice;
    }

    public void setMenuItemNutritionalValue(int menuItemNutritionalValue) {
        this.menuItemNutritionalValue = menuItemNutritionalValue;
    }

    public void setMenuItemCategory(String menuItemCategory) {
        this.menuItemCategory = menuItemCategory;
    }

    public void setMenuItemAllergens(List<String> menuItemAllergens) {
        this.menuItemAllergens = menuItemAllergens;
    }




    public void setItemBestSeller(boolean itemBestSeller) {
        isItemBestSeller = itemBestSeller;
    }


}
