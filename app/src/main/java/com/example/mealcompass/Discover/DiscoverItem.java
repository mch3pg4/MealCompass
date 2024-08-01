package com.example.mealcompass.Discover;

public class DiscoverItem {
    private final int discoverImage;
    private final String authorName;
    private final String timePosted;
    private final String discoverArticleTitle;
    private final String discoverArticleDescription;

    public DiscoverItem(int discoverImage, String authorName, String timePosted, String discoverArticleTitle, String discoverArticleDescription) {
        this.discoverImage = discoverImage;
        this.authorName = authorName;
        this.timePosted = timePosted;
        this.discoverArticleTitle = discoverArticleTitle;
        this.discoverArticleDescription = discoverArticleDescription;
    }

    public int getDiscoverImage() {
        return discoverImage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public String getDiscoverArticleTitle() {
        return discoverArticleTitle;
    }

    public String getDiscoverArticleDescription() {
        return discoverArticleDescription;
    }
}
