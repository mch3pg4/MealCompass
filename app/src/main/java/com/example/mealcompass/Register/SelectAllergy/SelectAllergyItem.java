package com.example.mealcompass.Register.SelectAllergy;

public class SelectAllergyItem {

    private String allergyName;
    private int allergyImage;
    private boolean isChecked;

    public SelectAllergyItem(String allergyName, int allergyImage, boolean isChecked) {
        this.allergyName = allergyName;
        this.allergyImage = allergyImage;
        this.isChecked = isChecked;
    }

    public String getAllergyName() {
        return allergyName;
    }

    public void setAllergyName(String allergyName) {
        this.allergyName = allergyName;
    }

    public int getAllergyImage() {
        return allergyImage;
    }

    public void setAllergyImage(int allergyImage) {
        this.allergyImage = allergyImage;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
            isChecked = checked;
        }
}
