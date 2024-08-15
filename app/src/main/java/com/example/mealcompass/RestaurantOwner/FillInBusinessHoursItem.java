package com.example.mealcompass.RestaurantOwner;

public class FillInBusinessHoursItem {
    private String day;
    private String openingHour;
    private String closingHour;
    private String splitOpeningHour;
    private String splitClosingHour;
    private boolean isClosed;
    private boolean isSplitHours;

    // Constructor
    public FillInBusinessHoursItem(String day, String openingHour, String closingHour, String splitOpeningHour, String splitClosingHour, boolean isClosed, boolean isSplitHours) {
        this.day = day;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.splitOpeningHour = splitOpeningHour;
        this.splitClosingHour = splitClosingHour;
        this.isClosed = isClosed;
        this.isSplitHours = isSplitHours;
    }

    // Getters and Setters
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }

    public String getSplitOpeningHour() {
        return splitOpeningHour;
    }

    public void setSplitOpeningHour(String splitOpeningHour) {
        this.splitOpeningHour = splitOpeningHour;
    }

    public String getSplitClosingHour() {
        return splitClosingHour;
    }

    public void setSplitClosingHour(String splitClosingHour) {
        this.splitClosingHour = splitClosingHour;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public boolean isSplitHours() {
        return isSplitHours;
    }

    public void setSplitHours(boolean splitHours) {
        isSplitHours = splitHours;
    }
}

