package com.example.mealcompass.Register.SelectDiet;

public class SelectDietItem {

    private String dietName;

    private boolean isChecked;

    public SelectDietItem(String dietName, boolean isChecked) {
        this.dietName = dietName;
        this.isChecked = isChecked;
    }

    public String getDietName() {
        return dietName;
    }

    public void setDietName(String dietName) {
        this.dietName = dietName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
